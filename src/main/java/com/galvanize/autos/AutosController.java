package com.galvanize.autos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
			System.out.println("Getting Autos");
			autosList = autosService.getAutos();
		} else if (make == null) {
			autosList = autosService.getAutosWithColor(color);
		} else if (color == null) {
			autosList = autosService.getAutosWithMake(make);
		} else {
			autosList = autosService.getAutos(color, make);
		}
		System.out.println(autosList);
		return Objects.isNull(autosList) || autosList.isEmpty() ? ResponseEntity.noContent().build() :
				ResponseEntity.ok(autosList);
	}

	@GetMapping("{vin}")
	public ResponseEntity<Automobile> getAutoByVin(@PathVariable String vin){
		Automobile automobile;
		automobile = autosService.getAuto(vin);
		return automobile == null ? ResponseEntity.noContent().build() :
				ResponseEntity.ok(automobile);
	};

	@PostMapping("")
	public ResponseEntity<Automobile> addAuto(@RequestBody Automobile newAutomobile) {
		Automobile automobileToAdd = autosService.addAuto(newAutomobile);
		if (newAutomobile != null) {
			return ResponseEntity.ok(automobileToAdd);
			//	The following code does the same thing.
			//	return ResponseEntity.status(200).body(automobileToAdd);
		} else {
			return ResponseEntity.badRequest().body(automobileToAdd);
		}
	}

	@PatchMapping("{vin}")
	public ResponseEntity<Automobile> updateAuto(@PathVariable String vin,
								 @RequestBody UpdateOwnerRequest update) {
		Automobile automobile;
		try {
			automobile = autosService.updateAuto(vin, update.getColor(), update.getOwner());
			automobile.setColor(update.getColor());
			automobile.setOwner(update.getOwner());
		} catch (AutoNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(automobile);
	}

	@DeleteMapping("{vin}")
	public ResponseEntity deleteAuto(@PathVariable String vin) {
		try {
			autosService.deleteAuto(vin);
		} catch (AutoNotFoundException e) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.accepted().build();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void invalidAutoExceptionHandler(InvalidAutoException e){
	}
}
