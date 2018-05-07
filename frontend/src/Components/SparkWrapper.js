import React, {Component} from "react";
import {fetchPoints} from "../RESTApi/consumer";
import SparkAppBody from "./SparkAppBody";

export default class SparkWrapper extends Component {
    constructor(props) {
        super(props);
        this.state = {opinions: [], positions: [], isSearched: false};
    }

    fetchPositions(q) {
        // Toggle comments to work with mock data and live processed data.Also change in middleware.
        return this.setState({opinions: fetchPoints(q) , isSearched:true});
        // return fetchPoints(q).then((res) => {
        //     console.log(res);
        //     this.setState({opinions: res, isSearched: true})
        // });
    }


    addToMap(m, k, v) {
        m[k] = m[k] || [];
        m[k].push(v);
    }

    render() {
        let categoryMap = {};
        let totalTweets = this.state.opinions.length;
        let opinionTweets = 0;


        this.state.opinions.map(o => {
            if (o.isOpinion) {

                opinionTweets++;

                o.aspects.map(aspect => {

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

        let category = Object.keys(categoryMap)[0];
        let tweetStats = [opinionTweets, totalTweets];

        return (
            <div id="wrap">
                <SparkAppBody
                    fetchPositions={this.fetchPositions.bind(this)} categoryMap={categoryMap} category={category}
                    google={this.props.google} isSearched={this.state.isSearched} stats={tweetStats}
                />
            </div>
        );
    }
}
