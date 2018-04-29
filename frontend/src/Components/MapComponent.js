import React, { Component } from "react";
import SparkSearchBar from "./SparkSearchBar";
import SparkAppBody from "./SparkAppBody";
import { fetchPoints } from "../RESTApi/consumer";
import { GoogleApiWrapper } from "google-maps-react";


class MapComponent extends Component {
  
  constructor(props) {
    super(props);
  }


  fetchPositions(q) {
   return fetchPoints(q)
  }

  render() {
    const h1Style = {
      fontSize: "70px",
      color: "black"
    };

    const color = {
      color: "black"
    };

    return (
      <div className="MapComponent">
        <div className="header">
          <h1 style={h1Style}>SparkBusters</h1>
          <h2 style={color}>
            Twitter Aspect Based Opinion Mining and Sentiment Analysis
          </h2>
          <SparkSearchBar fetchPositions={this.fetchPositions} />
        </div>
       {/*<SparkAppBody google={this.props.google} positions={this.state.positions}/>*/}
      </div>
    );
  }
}

export default GoogleApiWrapper({
  apiKey: process.env.googleMapsApiKey,
  libraries: ["visualization"]
})(MapComponent);
