import React, { Component } from "react";
import { Route } from "react-router-dom";
import TweetMap from "./TweetMap";
// import { GoogleApiWrapper } from "google-maps-react";
import { fetchPoints } from "../RESTApi/consumer";
import GraphVisualisation from "./GraphVisualisation";

export default class SparkAppBody extends Component {
	// state = {
	// 	positions: []
	// };

	

	render() {
		return (
			<div className="container-fluid">
				{/*
				<div className="center-block card">
					<Route
						path="/"
						render={props => (
							<TweetMap
								google={this.props.google}
								positions={this.props.positions}
								{...props}
							/>
						)}
					/>
				</div>
				*/}
				<div
					className="center-block card"
					style={{ background: "#f1f1f1" }}
				>
					<GraphVisualisation opinion={this.props.positions}/>
				</div>
			</div>
		);
	}
}
