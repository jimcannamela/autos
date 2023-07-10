package com.galvanize.autos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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
		when(autosRepository.findByColorAndMake(anyString(), anyString()))
				.thenReturn(Arrays.asList(automobile));
		AutosList autosList = autosService.getAutos("Yellow", "Chevrolet");
		assertThat(autosList).isNotNull();
		assertThat(autosList.isEmpty()).isFalse();
	}

	@Test
	void getAutos_search_notFound_returnNull() {
		when(autosRepository.findByColorAndMake(anyString(), anyString()))
				.thenReturn(Arrays.asList());
		AutosList autosList = autosService.getAutos("Yellow", "Chevrolet");
		assertThat(autosList).isNull();
	}

	@Test
	void getAutosWithColor() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		automobile.setColor("Yellow");
		when(autosRepository.findByColor(anyString())).thenReturn(Arrays.asList(automobile));
		AutosList autosList = autosService.getAutosWithColor("Yellow");
		assertThat(autosList).isNotNull();
		assertThat(autosList.isEmpty()).isFalse();
	}

	@Test
	void getAutosWithColor_notFound_returnNull() {
		when(autosRepository.findByColor(anyString()))
				.thenReturn(Arrays.asList());
		AutosList autosList = autosService.getAutosWithColor("Black");
		assertThat(autosList).isNull();
	}
	@Test
	void getAutosWithMake() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		when(autosRepository.findByMake(anyString())).thenReturn(Arrays.asList(automobile));
		AutosList autosList = autosService.getAutosWithMake("Chevrolet");
		assertThat(autosList).isNotNull();
		assertThat(autosList.isEmpty()).isFalse();
	}
	@Test
	void getAutosWithMake_notFound_returnNull() {
		when(autosRepository.findByMake(anyString()))
				.thenReturn(Arrays.asList());
		AutosList autosList = autosService.getAutosWithMake("Ford");
		assertThat(autosList).isNull();
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

	@Test
	void getAuto_withVin_notFound_returnsNull() {
		when(autosRepository.findByVin(anyString()))
				.thenThrow(AutoNotFoundException.class);
		assertThatThrownBy(() -> autosService.getAuto(anyString())).isInstanceOf(AutoNotFoundException.class);
	}

	@Test
	void updateAuto() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		automobile.setColor("Yellow");
		when(autosRepository.findByVin(anyString())).thenReturn(Optional.of(automobile));
		when(autosRepository.save(any(Automobile.class))).thenReturn(automobile);
		Automobile automobile1 = autosService.updateAuto(automobile.getVin(),"Purple", "People Eater");
		assertThat(automobile1).isNotNull();
		assertThat(automobile1.getColor()).isEqualTo("Purple");
		assertThat(automobile1.getOwner()).isEqualTo("People Eater");
	}
	@Test
	void updateAuto_VinDoesNotExist_ThrowsAutoNotFoundException() {
		when(autosRepository.findByVin(anyString())).thenReturn(null);

		assertThatThrownBy(() -> autosService.updateAuto("CAM1966", "Purple", "People Eater")).isInstanceOf(AutoNotFoundException.class);
	}

	@Test
	void updateAuto_NoVIN_ThrowsInvalidAutoException() {
		assertThatThrownBy(() -> autosService.updateAuto(null, "Purple", "People Eater")).isInstanceOf(InvalidAutoException.class);
	}

	@Test
	void updateAuto_NoColorOrOwner_ThrowsInvalidAutoException() {
		assertThatThrownBy(() -> autosService.updateAuto("CAM1966", null, null)).isInstanceOf(InvalidAutoException.class);
	}

	@Test
	void updateAuto_NoVinOrColorOrOwner_ThrowsInvalidAutoException() {
		assertThatThrownBy(() -> autosService.updateAuto(null, null, null)).isInstanceOf(InvalidAutoException.class);
	}


	@Test
	void deleteAuto_byVin() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		automobile.setColor("Yellow");
		when(autosRepository.findByVin(anyString())).thenReturn(Optional.of(automobile));

		autosService.deleteAuto(automobile.getVin());

		verify(autosRepository).delete(any(Automobile.class));
	}

	@Test
	void deleteAuto_byVin_notExists() {
		Automobile automobile = new Automobile(1966, "Chevrolet", "Camaro", "CAM1966" );
		automobile.setColor("Yellow");
		when(autosRepository.findByVin(anyString())).thenReturn(Optional.empty());

		assertThatExceptionOfType(AutoNotFoundException.class)
				.isThrownBy(() -> {
			autosService.deleteAuto("AUTO DOES NOT EXIST");
		});
	}
}