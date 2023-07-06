package com.galvanize.autos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutosServiceTests {

	private AutosService autosService;

	@Mock
	AutosRepository autosRepository;

	@BeforeEach
	void setUp() {
		autosService = new AutosService(autosRepository);
	}

	@Test
	void getAutos() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		when(autosRepository.findAll()).thenReturn(Arrays.asList(automobile));
		AutosList autosList = autosService.getAutos();
		assertThat(autosList).isNotNull();
		assertThat(autosList.isEmpty()).isFalse();
	}

	@Test
	void getAutos_search_returnsList() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		when(autosRepository.findByColorContainsAndMakeContains(anyString(), anyString()))
				.thenReturn(Arrays.asList(automobile));
		AutosList autosList = autosService.getAutos("Yellow", "Chevrolet");
		assertThat(autosList).isNotNull();
		assertThat(autosList.isEmpty()).isFalse();
	}

	@Test
	void getAutosWithColor() {
	}

	@Test
	void getAutosWithMake() {
	}

	@Test
	void addAuto_valid_returnsAuto() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		automobile.setColor("Yellow");
		when(autosRepository.save(any(Automobile.class)))
				.thenReturn((automobile));
		Automobile automobile1 = autosService.addAuto(automobile);
		assertThat(automobile).isNotNull();
		assertThat(automobile.getMake()).isEqualTo("Chevrolet");
	}

	@Test
	void getAuto() {
	}

	@Test
	void updateAuto() {
	}

	@Test
	void deleteAuto() {
	}
}