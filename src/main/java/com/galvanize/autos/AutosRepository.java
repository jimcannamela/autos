package com.galvanize.autos;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AutosRepository extends JpaRepository<Automobile, Long> {
}
