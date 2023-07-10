package com.galvanize.autos;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutosApiApplicationTests {
	@Autowired
	AutosRepository autosRepository;
	@Autowired // only applies to testRestTemplate
	TestRestTemplate testRestTemplate;
	Random r = new Random();
	List<Automobile> testAutos;
	@BeforeEach
	public void setUp() {
		this.testAutos = new ArrayList<>();
		Automobile automobile;
		String[] colors = {"Red", "Yellow", "Blue", "Green", "Purple", "Orange", "Black", "Brown", "White", "Gray"};
		for (int i = 0; i < 50; i++) {
			if (i % 3 == 0) {
				automobile = new Automobile(1967, "Chevrolet", "Camaro", "AABBCC"+(i*13));
				automobile.setColor(colors[r.nextInt(10)]);
			} else if (i % 2 == 0) {
				automobile = new Automobile(2000, "Subaru", "Outback", "SUUBBR"+(i*13));
				automobile.setColor(colors[r.nextInt(10)]);
			} else {
				automobile = new Automobile(2020, "Dodge", "Chalenger", "XYZZY"+(i*13));
				automobile.setColor(colors[r.nextInt(10)]);
			}
			this.testAutos.add(automobile);
		}
		autosRepository.saveAll(this.testAutos);
	}

	@AfterEach
	void tearDown() {
		autosRepository.deleteAll();
	}

	//    Happy Path Tests
	@Test
	void contextLoads() {
	}

	@Test
	void getAutos_exists_returnsAutosList(){
		// Set up

		// Execution
		ResponseEntity<AutosList> response = testRestTemplate.getForEntity( "/api/autos", AutosList.class);

		// Assertions
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().isEmpty()).isFalse();
		for (Automobile automobile: response.getBody().getAutomobiles()) {
			System.out.println(automobile);
		}
	}

	@Test
	void getAutos_search_returnsAutosList() {
		// Set up
		int seq = r.nextInt(50);
		String color = testAutos.get(seq).getColor();
		String make = testAutos.get(seq).getMake();

		// Execution
		ResponseEntity<AutosList> response = testRestTemplate.getForEntity(
				String.format("/api/autos?color=%s&make=%s", color, make), AutosList.class);

		// Assertions
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().isEmpty()).isFalse();
		assertThat(response.getBody().getAutomobiles().size()).isGreaterThanOrEqualTo(1);
		for (Automobile automobile: response.getBody().getAutomobiles()) {
			System.out.println(automobile);
		}
	}

	@Test
	void addAuto_returnsNewAutoDetails() {
		// Set up
		Automobile automobile = new Automobile();
		automobile.setVin("XYZ9876");
		automobile.setYear(1967);
		automobile.setMake("Chevrolet");
		automobile.setModel("Corvette");
		automobile.setColor("Red");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Automobile> request = new HttpEntity<>(automobile, headers);

		// Execution
		ResponseEntity<Automobile> response = testRestTemplate.postForEntity("/api/autos", request, Automobile.class);

		// Assertions
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getVin()).isEqualTo(automobile.getVin());
	}

	@Test
	void getAutoByColor_search_returnsAutosList() {
		// Set up
		int seq = r.nextInt(50);
		String color = testAutos.get(seq).getColor();

		// Execution
		ResponseEntity<AutosList> response = testRestTemplate.getForEntity(
				String.format("/api/autos?color=%s", color), AutosList.class);

		// Assertions
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().isEmpty()).isFalse();
		assertThat(response.getBody().getAutomobiles().size()).isGreaterThanOrEqualTo(1);
		for (Automobile automobile: response.getBody().getAutomobiles()) {
			System.out.println(automobile);
		}
	}

	@Test
	void getAutoByMake_search_returnsAutosList() {
		// Set up
		int seq = r.nextInt(50);
		String make = testAutos.get(seq).getMake();

		// Execution
		ResponseEntity<AutosList> response = testRestTemplate.getForEntity(
				String.format("/api/autos?make=%s", make), AutosList.class);

		// Assertions
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().isEmpty()).isFalse();
		assertThat(response.getBody().getAutomobiles().size()).isGreaterThanOrEqualTo(1);
		for (Automobile automobile: response.getBody().getAutomobiles()) {
			System.out.println(automobile);
		}
	}

	@Test
	void getAutoBVin_search_returnsAutomobile() {
		// Set up
		int seq = r.nextInt(50);
		String vin = testAutos.get(seq).getVin();
		System.out.println("Vin to search: "+vin);

		// Execution
		ResponseEntity<Automobile> response = testRestTemplate.getForEntity(
				String.format("/api/autos/%s", vin), Automobile.class);

		// Assertions
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		System.out.println(response.getBody());
	}

//	@Test
//	void patchAutoBVin_update_returnsAutomobile() {
	// get a vin
	// retrieve the automobile for that vin
	// put the automobile?
//		// Set up
//		int seq = r.nextInt(50);
//		String vin = testAutos.get(seq).getVin();
//		System.out.println("Vin to search: "+vin);
//
//		// Execution
//		ResponseEntity<Automobile> response = testRestTemplate.patchForObject(String.format("/api/autos/%s", vin), Automobile.class);
//
//		// Assertions
//		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(response.getBody()).isNotNull();
//		System.out.println(response.getBody());
//	}

	@Test
	void deleteAutoByVin_returnsAccepted() {
		// Set up
		int seq = r.nextInt(50);
		String vin = testAutos.get(seq).getVin();
		System.out.println("Vin to delete: "+vin);

		// Execution
		ResponseEntity<Void> response = testRestTemplate.exchange(String.format("/api/autos/%s", vin), HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

		// Assertions
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
	}


	//
	//     Not So Happy Path Tests
	//

	// Test for no autos in inventory
	@Test
	void getAutos_noAutosInInventory_returns404 (){
		// Set up
		autosRepository.deleteAll();
		// Execution
		ResponseEntity<AutosList> response = testRestTemplate.getForEntity( "/api/autos", AutosList.class);

		// Assertions
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isNull();
		assertThat(response.getBody().isEmpty()).isTrue();
	}
	// Test for no autos returned by search for color and make

	// Test for no autos returned by search for color

	// Test for no autos returned by search for make

	// Test for no auto found by search by vin

	// Test for bad request on add

	// Test for no auto found for update

	// Test for no auto found for delete



}
