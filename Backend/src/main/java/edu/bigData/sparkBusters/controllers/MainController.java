package edu.bigData.sparkBusters.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.bigData.sparkBusters.model.GoogleMapEntry;

@CrossOrigin(origins = "http://localhost:8000")
@RestController
public class MainController {

	@GetMapping("/ping")
	public @ResponseBody List<GoogleMapEntry> getPositions(@RequestParam(value = "size", defaultValue = "100") int size) {
		// return new ArrayList<GoogleMapEntry>() {
		// private static final long serialVersionUID = 1L;
		// {
		// add(GoogleMapEntry.builder().weight(1245).lat(14.25).lng(45.4455).build());
		// add(GoogleMapEntry.builder().weight(546).lat(24.2465).lng(25.124).build());
		// add(GoogleMapEntry.builder().weight(789).lat(44.2536).lng(75.5643).build());
		// }
		// };
		return GoogleMapEntry.getRandomPoints(size);
	}
}
