import React, { Component } from "react";
import { GoogleApiWrapper } from "google-maps-react";
import SparkWrapper from "./SparkWrapper";

class MapComponent extends Component {

  render() {

    return (
      <div className="MapComponent">
        <SparkWrapper
          google={this.props.google}
        />
      </div>
    );
  }
}

export default GoogleApiWrapper({
  apiKey: process.env.googleMapsApiKey,
  libraries: ["visualization"]
})(MapComponent);
