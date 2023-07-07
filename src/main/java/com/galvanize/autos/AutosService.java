package com.galvanize.autos;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
		List<Automobile> automobiles = autosRepository.findAll();
		if (!automobiles.isEmpty()) {
			return new AutosList(automobiles);
		}
		return null;
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
		return autosRepository.findByVin(vin).orElse(null);
	}

	public Automobile updateAuto(String vin, String color, String owner) {
		Optional<Automobile> optionalAutomobile = autosRepository.findByVin(vin);
		if (optionalAutomobile.isPresent()) {
			optionalAutomobile.get().setColor(color);
			optionalAutomobile.get().setOwner(owner);
			return autosRepository.save(optionalAutomobile.get());
		}
		return null;
	}

	public void deleteAuto(String vin) {
		Optional<Automobile> optionalAutomobile = autosRepository.findByVin(vin);
		if (optionalAutomobile.isPresent()) {
			autosRepository.delete(optionalAutomobile.get());
		} else {
			throw new AutoNotFoundException();
		}
	}
}
