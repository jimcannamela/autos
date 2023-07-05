package com.galvanize.autos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(AutosController.class)
public class AutosControllerTests {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	AutosService autosService;

	// GET: /api/autos
		// Returns list of all autos in the database - status 200
	@Test
	void getListOfAllAutos_ReturnList_Status200() throws Exception {
		// Setup
		List<Automobile> automobiles = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			automobiles.add(new Automobile(1966+i,"Chevrolet", "Camaro", "ABVC"+i));
		}
		when(autosService.getAllAutos()).thenReturn(new AutosList(automobiles));
		// Execution
		mockMvc.perform(get("/api/autos"))
		// Assertions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.automobiles", hasSize(5)));
	}

		// No autos in database - return message "No automobile information found" - status 204
		// ?color=yellow - return list of yellow cars - status 200
		// ?make=chevrolet - return list of chevrolet cars - status 200
		// ?make=yellow&make=chevrolet - return list of yellow chevrolet cars - status 200
		// ?color=black - return not found - status 204 ( we don't carry non GM products )
		// ?make=ford - return not found - status 204 ( we don't carry non GM products )
		// ?make=black&make=ford - return not found - status 204 ( we don't carry non GM products )
		// ?model=mustang - return bad request - status 400

	// POST: /api/autos
		// Auto successfully added to database - return auto information, and message - status 200
		// Duplicate auto information - return "Automobile already exists in database - status

	// GET: /api/autos/{vin}
		// Auto information returned - status 200
		// Auto not found - return message "Auto not found" - status 204

	// PATCH: /api/autos/{vin}
		// Auto successfully updated - return updated auto, and message - status 200
		// Auto not found - return message "Auto not found" - status 204

	// DELETE: /api/autos/{vin}
		// Auto successfully deleted - status 202
		// Auto not found - return message "Auto not found" - status 204

}
