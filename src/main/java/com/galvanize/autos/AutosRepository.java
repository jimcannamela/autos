package com.galvanize.autos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutosRepository extends JpaRepository<Automobile, Long> {
	List<Automobile> findByColorContainsAndMakeContains(String color, String make);
	Optional<Automobile> findByVin(String vin);
}
