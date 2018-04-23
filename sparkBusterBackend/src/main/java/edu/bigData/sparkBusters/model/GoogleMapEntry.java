package edu.bigData.sparkBusters.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GoogleMapEntry {
	private double weight;
	private double lat;
	private double lng;
}
