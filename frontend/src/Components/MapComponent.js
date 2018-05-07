import React, {Component} from "react";
import {GoogleApiWrapper} from "google-maps-react";
import SparkWrapper from "./SparkWrapper";

class MapComponent extends Component {
    render() {
        const scrollAndHeight = {
            overflowY: "scroll",
            maxHeight: "80%",
            minHeight: "80%",
            top: "20%",
            left: "20%"
        };

        return (
            <div>
                <SparkWrapper google={this.props.google} style={scrollAndHeight}/>
            </div>
        );
    }
}

export default GoogleApiWrapper({
    apiKey: process.env.REACT_APP_GOOGLE_MAP_KEY,
    libraries: ["visualization"]
})(MapComponent);
