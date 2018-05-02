import React, { Component } from "react";
import ReactDOM from "react-dom";

export default class TweetMap extends Component {
	componentDidUpdate(prevProps, prevState) {
		if (
			prevProps.google !== this.props.google ||
			prevProps.positions !== this.props.positions
		) {
			this.renderMap();
		}
	}

	componentDidMount() {
		// if(this.props.positions[0] != undefined)
		this.renderMap();
	}

	renderMap() {
		const mapStyle = 
			 [
				{ elementType: "geometry", stylers: [{ color: "#242f3e" }] },
				{
					elementType: "labels.text.stroke",
					stylers: [{ color: "#242f3e" }]
				},
				{
					elementType: "labels.text.fill",
					stylers: [{ color: "#746855" }]
				},
				{
					featureType: "administrative.locality",
					elementType: "labels.text.fill",
					stylers: [{ color: "#d59563" }]
				},
				{
					featureType: "poi",
					elementType: "labels.text.fill",
					stylers: [{ color: "#d59563" }]
				},
				{
					featureType: "poi.park",
					elementType: "geometry",
					stylers: [{ color: "#263c3f" }]
				},
				{
					featureType: "poi.park",
					elementType: "labels.text.fill",
					stylers: [{ color: "#6b9a76" }]
				},
				{
					featureType: "road",
					elementType: "geometry",
					stylers: [{ color: "#38414e" }]
				},
				{
					featureType: "road",
					elementType: "geometry.stroke",
					stylers: [{ color: "#212a37" }]
				},
				{
					featureType: "road",
					elementType: "labels.text.fill",
					stylers: [{ color: "#9ca5b3" }]
				},
				{
					featureType: "road.highway",
					elementType: "geometry",
					stylers: [{ color: "#746855" }]
				},
				{
					featureType: "road.highway",
					elementType: "geometry.stroke",
					stylers: [{ color: "#1f2835" }]
				},
				{
					featureType: "road.highway",
					elementType: "labels.text.fill",
					stylers: [{ color: "#f3d19c" }]
				},
				{
					featureType: "transit",
					elementType: "geometry",
					stylers: [{ color: "#2f3948" }]
				},
				{
					featureType: "transit.station",
					elementType: "labels.text.fill",
					stylers: [{ color: "#d59563" }]
				},
				{
					featureType: "water",
					elementType: "geometry",
					stylers: [{ color: "#17263c" }]
				},
				{
					featureType: "water",
					elementType: "labels.text.fill",
					stylers: [{ color: "#515c6d" }]
				},
				{
					featureType: "water",
					elementType: "labels.text.stroke",
					stylers: [{ color: "#17263c" }]
				}
			]
		

		if (this.props && this.props.google) {
			const { google } = this.props;
			const maps = google.maps;

			const mapRef = this.refs.map;
			const node = ReactDOM.findDOMNode(mapRef);

			const mapConfig = Object.assign(
				{},
				{
					center: { lat: 39.7392, lng: -104.9903 },
					zoom: 5,
					// gestureHandling: "cooperative",
					mapTypeId: "terrain",
					styles : mapStyle
				}
			);

			this.map = new maps.Map(node, mapConfig);
			var heatmapData = [];

			// this.props.positions.map(position => {
			// 	heatmapData.push({
			// 		location: new google.maps.LatLng(
			// 			position.lat,
			// 			position.lng
			// 		),
			// 		weight: 5
			// 	});

			// const marker = new google.maps.Marker({
			// 	position: {
			// 		lat: position.lat,
			// 		lng: position.lng
			// 	},
			// 	map: this.map,
			// 	title: position.weight,

			// });
			//icon: {
			// url:
			// "https://cdn3.iconfinder.com/data/icons/picons-social/57/03-twitter-32.png"
			// }

			// const infowindow = new google.maps.InfoWindow({
			// 	// We will put in all the tweet and other info here. Need to work on CSS as well
			// 	content: `<h3>${position.weight}</h3>`
			// });

			// marker.addListener("click", function() {
			// 	infowindow.open(this.map, marker);
			// });
			// return true;
			// });

			const heatmap = new google.maps.visualization.HeatmapLayer({
				data: heatmapData,
				radius: 40
			});
			heatmap.setMap(this.map);
		}
	}

	render() {
		const style = {
			width: "100%",
			height: "75vh"
		};

		return (
			<div className="wrapper">
				<center>
					<h2> Heatmap visualization based on opinionated tweets </h2>
				</center>
				<div ref="map" id="googlemaps">
					Loading map........
				</div>
			</div>
		);
	}
}
