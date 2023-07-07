package com.galvanize.autos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;


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

	// Get list of all autos - autos exist in repository - return list
	@Test
	void getAutos_returnList() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		when(autosRepository.findAll()).thenReturn(Arrays.asList(automobile));
		AutosList autosList = autosService.getAutos();
		assertThat(autosList).isNotNull();
		assertThat(autosList.isEmpty()).isFalse();
	}

	// Get list of all autos - no autos in repository - return null - display error
	@Test
	void getAutos_returnNull() {
		when(autosRepository.findAll()).thenReturn(Arrays.asList());
		AutosList autosList = autosService.getAutos();
		assertThat(autosList).isNull();
	}
	@Test
	void getAutos_search_returnsList() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		automobile.setColor("Yellow");
		when(autosRepository.findByColorContainsAndMakeContains(anyString(), anyString()))
				.thenReturn(Arrays.asList(automobile));
		AutosList autosList = autosService.getAutos("Yellow", "Chevrolet");
		assertThat(autosList).isNotNull();
		assertThat(autosList.isEmpty()).isFalse();
	}

	@Test
	void getAutos_search_notFound_returnNull() {
		when(autosRepository.findByColorContainsAndMakeContains(anyString(), anyString()))
				.thenReturn(Arrays.asList());
		AutosList autosList = autosService.getAutos("Yellow", "Chevrolet");
		assertThat(autosList).isNull();
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
	void addAuto_badRequest_returnsError() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		automobile.setColor("Yellow");
		when(autosRepository.save(any(Automobile.class)))
				.thenReturn((automobile));
		Automobile automobile1 = autosService.addAuto(automobile);
		assertThat(automobile).isNotNull();
		assertThat(automobile.getMake()).isEqualTo("Chevrolet");
	}

	@Test
	void getAuto_withVin_valid_returnsAuto() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		automobile.setColor("Yellow");
		when(autosRepository.findByVin(anyString()))
				.thenReturn(Optional.of(automobile));
		Automobile automobile1 = autosService.getAuto("CAM1966");
		assertThat(automobile1).isNotNull();
		assertThat(automobile1.getVin()).isEqualTo(automobile.getVin());
	}

//	@Test
//	void getAuto_withVin_notFound_returnsNull() {
//		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
//		when(autosRepository.findByVin(anyString()))
//				.thenReturn(Optional.of(null));
//		Automobile automobile1 = autosService.getAuto("FRED");
//		assertThat(automobile1).isNull();
//	}
	@Test
	void updateAuto() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		automobile.setColor("Yellow");
		when(autosRepository.findByVin(anyString()))
				.thenReturn(Optional.of(automobile));
		when(autosRepository.save(any(Automobile.class))).thenReturn(automobile);
		Automobile automobile1 = autosService.updateAuto(automobile.getVin(),"Purple", "People Eater");
		assertThat(automobile1).isNotNull();
		assertThat(automobile1.getColor()).isEqualTo("Purple");
		assertThat(automobile1.getOwner()).isEqualTo("People Eater");
	}

	@Test
	void deleteAuto() {
	}
}