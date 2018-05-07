import React, {Component} from "react";
import ReactDOM from "react-dom";

export default class TweetMap extends Component {
    componentDidUpdate(prevProps, prevState) {
        if (
            prevProps.google !== this.props.google ||
            prevProps.opinions !== this.props.opinions
        ) {
            this.renderMap();
        }
    }

    componentDidMount() {
        this.renderMap();
    }

    renderMap() {
        // console.log(this.props.opinions);
        const mapStyle =
            [
                {elementType: "geometry", stylers: [{color: "#242f3e"}]},
                {
                    elementType: "labels.text.stroke",
                    stylers: [{color: "#242f3e"}]
                },
                {
                    elementType: "labels.text.fill",
                    stylers: [{color: "#746855"}]
                },
                {
                    featureType: "administrative.locality",
                    elementType: "labels.text.fill",
                    stylers: [{color: "#d59563"}]
                },
                {
                    featureType: "poi",
                    elementType: "labels.text.fill",
                    stylers: [{color: "#d59563"}]
                },
                {
                    featureType: "poi.park",
                    elementType: "geometry",
                    stylers: [{color: "#263c3f"}]
                },
                {
                    featureType: "poi.park",
                    elementType: "labels.text.fill",
                    stylers: [{color: "#6b9a76"}]
                },
                {
                    featureType: "road",
                    elementType: "geometry",
                    stylers: [{color: "#38414e"}]
                },
                {
                    featureType: "road",
                    elementType: "geometry.stroke",
                    stylers: [{color: "#212a37"}]
                },
                {
                    featureType: "road",
                    elementType: "labels.text.fill",
                    stylers: [{color: "#9ca5b3"}]
                },
                {
                    featureType: "road.highway",
                    elementType: "geometry",
                    stylers: [{color: "#746855"}]
                },
                {
                    featureType: "road.highway",
                    elementType: "geometry.stroke",
                    stylers: [{color: "#1f2835"}]
                },
                {
                    featureType: "road.highway",
                    elementType: "labels.text.fill",
                    stylers: [{color: "#f3d19c"}]
                },
                {
                    featureType: "transit",
                    elementType: "geometry",
                    stylers: [{color: "#2f3948"}]
                },
                {
                    featureType: "transit.station",
                    elementType: "labels.text.fill",
                    stylers: [{color: "#d59563"}]
                },
                {
                    featureType: "water",
                    elementType: "geometry",
                    stylers: [{color: "#17263c"}]
                },
                {
                    featureType: "water",
                    elementType: "labels.text.fill",
                    stylers: [{color: "#515c6d"}]
                },
                {
                    featureType: "water",
                    elementType: "labels.text.stroke",
                    stylers: [{color: "#17263c"}]
                }
            ]

        // const icon = "https://cdn3.iconfinder.com/data/icons/picons-social/57/03-twitter-32.png";
        const icon = "/img/tweet.png";

        if (this.props && this.props.google) {
            const {google} = this.props;
            const maps = google.maps;

            const mapRef = this.refs.map;
            const node = ReactDOM.findDOMNode(mapRef);

            const mapConfig = Object.assign(
                {},
                {
                    center: {lat: 39.7392, lng: -104.9903},
                    zoom: 5,
                    gestureHandling: "cooperative",
                    mapTypeId: "terrain",
                    styles: mapStyle
                }
            );

            this.map = new maps.Map(node, mapConfig);
            var heatmapData = [];


            if (this.props.opinions) {
                this.props.opinions.map(position => {
                    heatmapData.push({
                        location: new google.maps.LatLng(
                            position.lat,
                            position.lng
                        ),
                        weight: position.sentiment
                    });


                    const marker = new google.maps.Marker({
                        position: {
                            lat: position.lat,
                            lng: position.lng
                        },
                        map: this.map,
                        title: "Tweet",
                        icon: {
                            url: icon
                        }
                    });

                    const sentimentType = (position.sentiment === 0) ? "Neutral" : (position.sentiment > 0) ? "Positive" : "Negative";
                    const infowindow = new google.maps.InfoWindow({
                        content: `<div style="position: relative;
										width: 400px;
										height: 150px;
										word-wrap: break-word;
										background-color: #fff;
										background-clip: border-box;
										border: 1px solid rgba(37, 161, 242, 0.5);
										border-radius: .25rem;
										box-sizing: inherit;
										font-size: initial;">
								<div style="flex: 1 1 auto;
										padding: 1.25rem;">
									<div style="height: 1.25em;
												width: 1.25em;
												background-image: url(data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%2072%2072%22%3E%3Cpath%20fill%3D%22none%22%20d%3D%22M0%200h72v72H0z%22%2F%3E%3Cpath%20class%3D%22icon%22%20fill%3D%22%231da1f2%22%20d%3D%22M68.812%2015.14c-2.348%201.04-4.87%201.744-7.52%202.06%202.704-1.62%204.78-4.186%205.757-7.243-2.53%201.5-5.33%202.592-8.314%203.176C56.35%2010.59%2052.948%209%2049.182%209c-7.23%200-13.092%205.86-13.092%2013.093%200%201.026.118%202.02.338%202.98C25.543%2024.527%2015.9%2019.318%209.44%2011.396c-1.125%201.936-1.77%204.184-1.77%206.58%200%204.543%202.312%208.552%205.824%2010.9-2.146-.07-4.165-.658-5.93-1.64-.002.056-.002.11-.002.163%200%206.345%204.513%2011.638%2010.504%2012.84-1.1.298-2.256.457-3.45.457-.845%200-1.666-.078-2.464-.23%201.667%205.2%206.5%208.985%2012.23%209.09-4.482%203.51-10.13%205.605-16.26%205.605-1.055%200-2.096-.06-3.122-.184%205.794%203.717%2012.676%205.882%2020.067%205.882%2024.083%200%2037.25-19.95%2037.25-37.25%200-.565-.013-1.133-.038-1.693%202.558-1.847%204.778-4.15%206.532-6.774z%22%2F%3E%3C%2Fsvg%3E);">
							
									</div>
							
									<div>
										<span style="font-weight: bold"> Tweet : </span>${position.tweet}
									</div>
							
									<div style="margin-top: 10px">
										<span style="font-weight: bold"> Sentiment : </span>${sentimentType}
									</div>
								</div>
							
							</div>`
                    });

                    marker.addListener("click", function () {
                        infowindow.open(this.map, marker);
                    });
                    return true;
                });

                const heatmap = new google.maps.visualization.HeatmapLayer({
                    data: heatmapData,
                    radius: 40
                });
                heatmap.setMap(this.map);
            }
        }
    }

    render() {

        return (
            <div className="wrapper">
                {/*<center>*/}
                {/*<h2> Heatmap visualization based on opinionated tweets </h2>*/}
                {/*</center>*/}
                <div ref="map" id="googlemaps">
                    Loading map........
                </div>
            </div>
        );
    }
}
