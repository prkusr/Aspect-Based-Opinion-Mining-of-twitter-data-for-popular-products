import React, { Component } from "react";
import { Route } from "react-router-dom";
import TweetMap from "./TweetMap";
// import { GoogleApiWrapper } from "google-maps-react";
import GraphVisualisation from "./GraphVisualisation";

export default class SparkAppBody extends Component {
	addToMap(m, k, v) {
		m[k] = m[k] || [];
		m[k].push(v);
	}

	render() {
		let position = [];
		let categoryMap = {};

		let totalTweets = this.props.opinions.length;
		let opinionTweets = 0;

		// let positiveOpinionTweets = 0;
		// let negativeOpinionTweets = 0;
		// let neutral = 0;

		this.props.opinions.map(o => {
			if (o.isOpinion) {
				position.push({
					lat: o.location[1],
					lng: o.location[0],
					tweet: o.text
				});

				opinionTweets++;

				o.aspects.map(aspect => {
					// if (aspect.sentiment > 0) positiveOpinionTweets++;
					// else if (aspect.sentiment > 0) negativeOpinionTweets++;
					// else neutral++;

					this.addToMap(categoryMap, aspect.category, {
						tweet: o.text,
						sentiment: aspect.sentiment,
						lat: o.location[1],
						lng: o.location[0]
					});
					return true;
				});
			}
			return true;
		});

		return (
			<div className="container-fluid">
				<div className="center-block card">
					<Route
						path="/"
						render={props => (
							<TweetMap
								google={this.props.google}
								positions={position}
								{...props}
							/>
						)}
					/>
				</div>

				<div
					className="center-block card"
					style={{ background: "#f1f1f1" }}
				>
					<GraphVisualisation opinion={categoryMap} />
				</div>
			</div>
		);
	}
}
