package com.galvanize.autos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autos")
public class AutosController {

	AutosService autosService;

	public AutosController(AutosService autosService) {
		this.autosService = autosService;
	}

	@GetMapping("")
	public ResponseEntity<AutosList> getAutolist(){
		AutosList autosList = autosService.getAllAutos();
		return autosList.isEmpty() ? ResponseEntity.noContent().build() :
				ResponseEntity.ok(autosList);
	}
}
