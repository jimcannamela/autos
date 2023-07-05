package com.galvanize.autos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AutosControllerTests {

	@Autowired
	MockMvc mockMvc;

	// GET: /api/autos
		// Returns list of all autos in the database - status 200
	@Test
	void getListOfAllAutos_ReturnList_Status200() throws Exception {
		mockMvc.perform(get("/api/autos"))
				.andExpect(status().isOk())
				.andExpect(content().string("All Autos"));
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
