package com.galvanize.autos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
	void testGetAutos() {
	}

	@Test
	void getAutosWithColor() {
	}

	@Test
	void getAutosWithMake() {
	}

	@Test
	void addAuto() {
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