package edu.bigData.sparkBusters.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GoogleMapEntry {

	public static final double MIN_LAT = -90.00;
	public static final double MAX_LAT = 90.00;
	public static final double MIN_LNG = -180.00;
	public static final double MAX_LNG = 180.00;

	private double weight;
	private double lat;
	private double lng;
//	private String tweet;
//	private char gender;

	public static boolean isValidLatitude(double lat) {
		return lat >= MIN_LAT && lat <= MAX_LAT;
	}

	public static boolean isValidLongitude(double lng) {
		return lng >= MIN_LNG && lng <= MAX_LNG;
	}

	public static List<GoogleMapEntry> getRandomPoints(long size) {
		if (size < 1)
			return null;

		List<GoogleMapEntry> randomPoints = new ArrayList<>();

		Random random = new Random();
		DoubleStream lat = random.doubles(size, MIN_LAT, MAX_LAT + 0.001);
		DoubleStream lng = random.doubles(size, MIN_LNG, MAX_LNG + 0.001);

		final Iterator<Double> lngIterator = lng.iterator();
		
//		randomPoints = lat.filter((latitude -> lngIterator.hasNext()))
//		   .map(latitude -> {
//			   GoogleMapEntry.builder().
//					   lng(lngIterator.next()).
//					   lat(latitude).
//					   weight(1).build();
//		   }).collect(Collectors.toList());
		
		lat.forEach(latitude -> {
			if(lngIterator.hasNext() && isValidLatitude(latitude)){
				
				final double _lng = lngIterator.next();
				randomPoints.add(GoogleMapEntry.builder().
				   lng(isValidLongitude(_lng) ? _lng : _lng - 5).
				   lat(latitude).
				   weight(random.nextInt(Integer.MAX_VALUE)).
				   build());
			}
		});
		
		return randomPoints;
	}
}
