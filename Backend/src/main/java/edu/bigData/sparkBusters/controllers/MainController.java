package edu.bigData.sparkBusters.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.bigData.sparkBusters.model.GoogleMapEntry;

@Controller
public class MainController {

	@RequestMapping("/ping")
	public @ResponseBody List<GoogleMapEntry> getPositions() {
		return new ArrayList<GoogleMapEntry>() {
			private static final long serialVersionUID = 1L;
			{
				add(GoogleMapEntry.builder().weight(1245).lat(14.25).lng(45.4455).build());
				add(GoogleMapEntry.builder().weight(546).lat(24.2465).lng(25.124).build());
				add(GoogleMapEntry.builder().weight(789).lat(44.2536).lng(75.5643).build());
			}
		};
	}
}
