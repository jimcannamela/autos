package com.galvanize.autos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autos")
public class AutosController {

	AutosService autosService;

	public AutosController(AutosService autosService) {
		this.autosService = autosService;
	}

	@GetMapping("")
	public ResponseEntity<AutosList> getAutos(@RequestParam(required = false) String color,
											  @RequestParam(required = false) String make){
		AutosList autosList;
		if (color == null && make == null) {
			autosList = autosService.getAutos();
		} else if (make == null) {
			autosList = autosService.getAutosWithColor(color);
		} else if (color == null) {
			autosList = autosService.getAutosWithMake(make);
		} else {
			autosList = autosService.getAutos(color, make);
		}

		return autosList.isEmpty() ? ResponseEntity.noContent().build() :
				ResponseEntity.ok(autosList);
	}

	@PostMapping("")
	public Automobile addAuto(@RequestBody Automobile automobile) {
		return autosService.addAuto(automobile);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void invalidAutoExceptionHandler(InvalidAutoException e){
	}
}
