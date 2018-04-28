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

	renderMap() {
		if (this.props && this.props.google) {
			const { google } = this.props;
			const maps = google.maps;

			const mapRef = this.refs.map;
			const node = ReactDOM.findDOMNode(mapRef);

			const mapConfig = Object.assign(
				{},
				{
					center: { lat: 0.0, lng: 0.0 },
					zoom: 3,
					gestureHandling: "cooperative",
					mapTypeId: "terrain"
				}
			);

			this.map = new maps.Map(node, mapConfig);
			var heatmapData = [];

			this.props.positions.map(position => {
				heatmapData.push({
					location: new google.maps.LatLng(
						position.lat,
						position.lng
					),
					weight: 5
				});

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
				const infowindow = new google.maps.InfoWindow({
					// We will put in all the tweet and other info here. Need to work on CSS as well
					content: `<h3>${position.weight}</h3>`
				});

				// marker.addListener("click", function() {
				// 	infowindow.open(this.map, marker);
				// });
			});

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
				<h2> Heatmap visualization based on opinionated tweets </h2>
				<div ref="map" style={style}>
					Loading map........
				</div>
			</div>
		);
	}
}
