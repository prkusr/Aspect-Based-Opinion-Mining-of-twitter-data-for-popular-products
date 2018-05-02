import React, {Component} from "react";
import {Route} from "react-router-dom";
import TweetMap from "./TweetMap";
import GraphVisualisation from "./GraphVisualisation";
import GraphPanel from "./GraphPanel";
import SparkPanel from "./SparkPanel";
import SparkSearchBar from "./SparkSearchBar";

export default class SparkAppBody extends Component {
    constructor(props) {
        super(props);
        this.state = {category: this.props.category};
    }

    updateCategory(c) {
        this.setState({category: c});
    }

    render() {
        const h1Style = {
            maxHeight: "85%",
            minHeight: "80%"
        };


        let categories = [];
        let keys = Object.keys(this.props.categoryMap);
        let cLen = [];

        let cat = this.state.category || keys[0];
        let opinions = this.props.categoryMap[cat];


        for (let i = 0; i < keys.length; i++) {
            categories.push(<SparkPanel key={i} val={keys[i]} test={this.updateCategory.bind(this)}/>)
            cLen.push(this.props.categoryMap[keys[i]].length);
        }

        let pos = 0;
        let neg = 0;
        let neutral = 0;

        if (this.props.isSearched) {
            opinions.map(o => {
                if (o.sentiment == 0) neutral++;
                else if (o.sentiment > 0) pos++;
                else neg++;

                return true;
            });

        }
        console.log(this.props.categoryMap[this.state.category]);
        return (
            <div id="bodyWrapper">
                {/*<div className="MapComponent">*/}
                {/*<Route*/}
                {/*path="/"*/}
                {/*render={props => (*/}
                {/*<TweetMap google={this.props.google} opinions={opinions} {...props} />*/}
                {/*)}*/}
                {/*/>*/}
                {/*</div>*/}
                <div id="app-sidebar" style={h1Style}>
                    <SparkSearchBar fetchPositions={this.props.fetchPositions.bind(this)}/>
                    {this.props.isSearched && <div>
                        <div className="graph-panel">
                            <h1> Opinion categories </h1>
                            {categories}

                        </div>
                        <GraphPanel title={"Sentiment"} labels={["Positive", "Negative", "Neutral"]}
                                    gData={[pos, neg, neutral]}/>
                        <GraphPanel title={"Opinion"} labels={["Opinion", "Not an opinion"]}
                                    gData={[this.props.stats[0], this.props.stats[1] - this.props.stats[0]]}/>
                        <GraphPanel title={"Category Info"} labels={keys} gData={cLen}/>
                    </div>
                    }
                </div>
            </div>
        );
    }
}
