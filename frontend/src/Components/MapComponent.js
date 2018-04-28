import React, { Component } from "react";
// import { Route } from 'react-router-dom'
// import EarthquakesForm from './EarthquakesForm'
// import TweetMap from './TweetMap'
import SparkSearchBar from "./SparkSearchBar";
import SparkAppBody from "./SparkAppBody";
import { fetchPoints } from "../RESTApi/consumer";
import { GoogleApiWrapper } from "google-maps-react";


class MapComponent extends Component {
  
  render() {
      const h1Style = {
          fontSize: "60px",
      };

      const color ={
        color: "black"
      };

    return (
      <div className="MapComponent">
        <div className="header">
          <h1 style={h1Style ,color}>SparkBusters</h1>
          <h2 style={color}> Twitter Aspect Based Opinion Mining and Sentiment Analysis</h2>
          <SparkSearchBar />
        </div>
        <SparkAppBody google={this.props.google}/>
      </div>
    );
  }
}

export default GoogleApiWrapper({
  apiKey: process.env.googleMapsApiKey,
  libraries: ["visualization"]
})(MapComponent);