package com.galvanize.autos;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutosService {

	AutosRepository autosRepository;

	public AutosService(AutosRepository autosRepository) {
		this.autosRepository = autosRepository;
	}

	public AutosList getAutos() {
		// Query select * from autos
		// Put that in a list
		// Return a new AutosList with the list
		return new AutosList(autosRepository.findAll());
	}

	public AutosList getAutos(String color, String make) {
		List<Automobile> automobiles = autosRepository.findByColorContainsAndMakeContains(color, make);
		if (!automobiles.isEmpty()) {
			return new AutosList(automobiles);
		}
		return null;
	}

	public AutosList getAutosWithColor(String color) {
		return null;
	}

	public AutosList getAutosWithMake(String make) {
		return null;
	}

	public Automobile addAuto(Automobile automobile) {
		return autosRepository.save(automobile);
	}

	public Automobile getAuto(String vin) {
		return null;
	}

	public Automobile updateAuto(String vin, String color, String owner) {
		return null;
	}

	public void deleteAuto(String vin) {
	}
}
