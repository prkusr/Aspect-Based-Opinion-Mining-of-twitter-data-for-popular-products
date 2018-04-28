import React, { Component } from "react";
// import { Route } from 'react-router-dom'
// import EarthquakesForm from './EarthquakesForm'
// import TweetMap from './TweetMap'
import SparkSearchBar from "./SparkSearchBar";
import { fetchPoints } from "../RESTApi/consumer";
import { GoogleApiWrapper } from "google-maps-react";

class MapComponent extends Component {
  state = {
    positions: [],
    search: "busted"
  };

  componentDidMount = () => {
    // fetchPoints()
    //   .then((point) => {
    //    // console.log(point)
    //      this.setState({ positions: point }, )
    //   })
    this.setState({ positions: fetchPoints() });
  };

  render() {
      const h1Style = {
          fontSize: "60px"
      };

    return (
      <div className="MapComponent">
        <div className="header">
          <h1 style={h1Style}>SparkBusters</h1>
          <h2> Twitter Aspect Based Opinion Mining and Sentiment Analysis</h2>

          <SparkSearchBar />
        </div>
        <div className="wrapper">loading map...</div>
      </div>
    );
  }
}

export default GoogleApiWrapper({
  //apiKey: process.env.googleMapsApiKey,
  libraries: ["visualization"]
})(MapComponent);