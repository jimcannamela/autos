package com.galvanize.autos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AutosController.class)
public class AutosControllerTests {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	AutosService autosService;
	ObjectMapper mapper = new ObjectMapper();

	// GET: /api/autos
		// Returns list of all autos in the database - status 200
	@Test
	void getListOfAllAutos_ReturnList_Status200() throws Exception {
		// Setup
		List<Automobile> automobiles = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			automobiles.add(new Automobile(1966+i,"Chevrolet", "Camaro", "ABVC"+i));
		}
		when(autosService.getAutos()).thenReturn(new AutosList(automobiles));
		// Execution
		mockMvc.perform(get("/api/autos"))
				.andDo(print())
		// Assertions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.automobiles", hasSize(5)));
	}

		// No autos in database - return message "No automobile information found" - status 204
	@Test
	void getListOfAllAutos_NoContent_Status204() throws Exception {
		// Setup
		when(autosService.getAutos()).thenReturn(new AutosList());
		// Execution
		mockMvc.perform(get("/api/autos"))
				.andDo(print())
		// Assertions
				.andExpect(status().isNoContent());
	}


		// ?make=yellow&make=chevrolet - return list of yellow chevrolet cars - status 200
	@Test
	void searchForYellowChevrolets_ReturnAutosList_Status200() throws Exception {
		// Setup
		List<Automobile> automobiles = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			automobiles.add(new Automobile(1966+i,"Chevrolet", "Camaro", "ABVC"+i));
		}
		when(autosService.getAutos(anyString(), anyString())).thenReturn(new AutosList(automobiles));
		// Execution
		mockMvc.perform(get("/api/autos?color=Yellow&make=Chevrolet"))
				.andDo(print())
		// Assertions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.automobiles", hasSize(5)));
	}

		// ?color=yellow - return list of yellow cars - status 200
	@Test
	void searchForYellow_ReturnAutosList_Status200() throws Exception {
		// Setup
		List<Automobile> automobiles = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			automobiles.add(new Automobile(1966+i,"Chevrolet", "Camaro", "ABVC"+i));
		}
		when(autosService.getAutosWithColor(anyString())).thenReturn(new AutosList(automobiles));
		// Execution
		mockMvc.perform(get("/api/autos?color=Yellow"))
				.andDo(print())
		// Assertions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.automobiles", hasSize(5)));
	}
		// ?make=chevrolet - return list of chevrolet cars - status 200
	@Test
	void searchForChevrolets_ReturnAutosList_Status200() throws Exception {
		// Setup
		List<Automobile> automobiles = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			automobiles.add(new Automobile(1966+i,"Chevrolet", "Camaro", "ABVC"+i));
		}
		when(autosService.getAutosWithMake(anyString())).thenReturn(new AutosList(automobiles));
		// Execution
		mockMvc.perform(get("/api/autos?make=Chevrolet"))
				.andDo(print())
		// Assertions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.automobiles", hasSize(5)));
	}

		// ?color=black - return not found - status 204 ( we don't carry non GM products )
	@Test
	void searchForBlack_ReturnNoContent_Status204() throws Exception {
		// Setup
		when(autosService.getAutosWithColor(anyString())).thenReturn(new AutosList());
		// Execution
		mockMvc.perform(get("/api/autos?color=Black"))
				.andDo(print())
		// Assertions
				.andExpect(status().isNoContent());
	}
		// ?make=ford - return not found - status 204 ( we don't carry non GM products )
	@Test
	void searchForFord_ReturnNoContent_Status204() throws Exception {
		// Setup
		when(autosService.getAutosWithMake(anyString())).thenReturn(new AutosList());
		// Execution
		mockMvc.perform(get("/api/autos?make=Ford"))
				.andDo(print())
		// Assertions
				.andExpect(status().isNoContent());
	}
		// ?make=black&make=ford - return not found - status 204 ( we don't carry non GM products )
	@Test
	void searchForBlackFord_ReturnNoContent_Status204() throws Exception {
		// Setup
		when(autosService.getAutos(anyString(), anyString())).thenReturn(new AutosList());
		// Execution
		mockMvc.perform(get("/api/autos?color=Black&make=Ford"))
				.andDo(print())
		// Assertions
				.andExpect(status().isNoContent());
	}
		// ?model=mustang - return bad request - status 400

	// POST: /api/autos
		// Auto successfully added to database - return auto information, and message - status 200
	@Test
	void addAuto_valid_returnsAuto() throws Exception{
		// Setup
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "AABBCC");
		when(autosService.addAuto(any(Automobile.class))).thenReturn(automobile);
		// Execution
		mockMvc.perform(post("/api/autos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(automobile)))
				.andDo(print())
		// Assertions
				.andExpect(status().isOk())
				.andExpect(jsonPath("make").value("Chevrolet"));
	}
		// Duplicate auto information - return "Automobile already exists in database - status
	@Test
	void addAuto_badRequest_returns400() throws Exception{
		// Setup
		String json = "{\"year\":1966,\"make\":\"Chevrolet\",\"model\":\"Camaro\",\"color\":null,\"owner\":null,\"vin\":\"ABVC0\"}";
		when(autosService.addAuto(any(Automobile.class))).thenThrow(InvalidAutoException.class);
		// Execution
		mockMvc.perform(post("/api/autos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andDo(print())
				// Assertions
				.andExpect(status().isBadRequest());
	}
	// GET: /api/autos/{vin}
		// Auto information returned - status 200
	@Test
	void getAutoByVin_returnsAuto_status200() throws Exception{
		// Setup
		Automobile automobile = new Automobile(1967,"Chevrolet", "Camaro", "ABVC1");
		when(autosService.getAuto(anyString())).thenReturn(automobile);
		// Execution
		mockMvc.perform(get("/api/autos/"+automobile.getVin()))
				.andDo(print())
		// Assertions
				.andExpect(status().isOk())
				.andExpect(jsonPath("year").value(1967));


	}
		// Auto not found - return message "Auto not found" - status 204
	@Test
	void getAutoByVin_NotFound_Status204() throws Exception {
		// Setup
		when(autosService.getAuto(anyString())).thenReturn(null);
		// Execution
		mockMvc.perform(get("/api/autos/ABVC9"))
				.andDo(print())
		// Assertions
				.andExpect(status().isNoContent());
	}
	// PATCH: /api/autos/{vin}
		// Auto successfully updated - return updated auto, and message - status 200
	@Test
	void updateAuto_withObject_returnAuto_Status200 () throws Exception {
		Automobile automobile = new Automobile(1967,"Chevrolet", "Camaro", "ABVC1");
		when(autosService.updateAuto(anyString(), anyString(), anyString())).thenReturn(automobile);
		// Execution
		mockMvc.perform(patch("/api/autos/"+automobile.getVin())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"color\":\"Purple\",\"owner\":\"People Eater\"}"))
				.andDo(print())
		// Assertions
				.andExpect(status().isOk())
				.andExpect(jsonPath("color").value("Purple"))
				.andExpect(jsonPath("owner").value("People Eater"));

	}
		// Auto not found - return message "Auto not found" - status 404
		@Test
		void updateAuto_NotFound_Status404() throws Exception {
			// Setup
			doThrow(new AutoNotFoundException()).when(autosService).updateAuto(anyString(), anyString(), anyString());
			// Execution
			mockMvc.perform(patch("/api/autos/ABVC123")
							.contentType(MediaType.APPLICATION_JSON)
							.content("{\"color\":\"Purple\",\"owner\":\"People Eater\"}"))
					// Assertions
					.andExpect(status().isNotFound());
		}
	// Auto update - bad request - status 400
	@Test
	void updateAuto_BadRequest_Status400() throws Exception {
		// Setup
		doThrow(new AutoNotFoundException()).when(autosService).updateAuto(anyString(), anyString(), anyString());
		// Execution
		mockMvc.perform(patch("/api/autos/ABVC123"))
				// Assertions
				.andExpect(status().isBadRequest());
	}
	// DELETE: /api/autos/{vin}
		// Auto successfully deleted - status 202
	@Test
	void deleteAuto_withVin_Status202() throws Exception {
		// Setup
		// Execution
		mockMvc.perform(delete("/api/autos/ABVC123"))
				.andDo(print())
		// Assertions
				.andExpect(status().isAccepted());
		verify(autosService).deleteAuto(anyString());
	}
		// Auto not found - return message "Auto not found" - status 204
	@Test
	void deleteAuto_NotFound_Status204() throws Exception {
		// Setup
		doThrow(new AutoNotFoundException()).when(autosService).deleteAuto(anyString());
		// Execution
		mockMvc.perform(delete("/api/autos/ABVC123"))
				// Assertions
				.andExpect(status().isNoContent());
	}


}
