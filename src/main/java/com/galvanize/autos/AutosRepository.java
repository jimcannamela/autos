package com.galvanize.autos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutosRepository extends JpaRepository<Automobile, Long> {
	List<Automobile> findByColorContainsAndMakeContains(String color, String make);
}
