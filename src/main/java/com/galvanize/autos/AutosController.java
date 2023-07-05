package com.galvanize.autos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autos")
public class AutosController {

	AutosService autosService;

	public AutosController(AutosService autosService) {
		this.autosService = autosService;
	}

	@GetMapping("")
	public ResponseEntity<AutosList> getAutos(@RequestParam(value="color", required = false) String color,
											  @RequestParam(value="make", required = false) String make){
		AutosList autosList;
		if (color == null && make == null) {
			autosList = autosService.getAutos();
		} else if (make == null) {
			autosList = autosService.getAutos(color);
		} else if (color == null) {
			autosList = autosService.getAutos(make);
		} else {
			autosList = autosService.getAutos(color, make);
		}

		return autosList.isEmpty() ? ResponseEntity.noContent().build() :
				ResponseEntity.ok(autosList);
	}
}
