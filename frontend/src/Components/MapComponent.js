import React, {Component} from "react";
import {GoogleApiWrapper} from "google-maps-react";
import SparkWrapper from "./SparkWrapper";
import {Route} from "react-router-dom";
import TweetMap from "./TweetMap";

class MapComponent extends Component {
    render() {
        const p = {};

        const scrollAndHeight = {
            overflowY: "scroll",
            maxHeight: "80%",
            minHeight: "80%",
            top: "20%",
            left: "20%"
        };

        return (
            <div>

                {/*<div className="MapComponent">
          <Route
            path="/"
            render={props => (
              <TweetMap google={this.props.google} positions={p} {...props} />
            )}
          />
        </div>
      */}

                <SparkWrapper google={this.props.google} style={scrollAndHeight}/>

            </div>
        );
    }
}

export default GoogleApiWrapper({
    apiKey: process.env.googleMapsApiKey,
    libraries: ["visualization"]
})(MapComponent);
