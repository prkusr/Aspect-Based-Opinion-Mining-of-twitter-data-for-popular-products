import React, {Component} from "react";
import {Route} from "react-router-dom";
import TweetMap from "./TweetMap";
import GraphVisualisation from "./GraphVisualisation";
import {Doughnut, Polar, Pie} from "react-chartjs-2";
import GraphPanel from "./GraphPanel";
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
            maxHeight: "700px",
            minHeight: "700px"
        };


        let opinions = this.props.categoryMap[this.state.category];
        let categories = [];
        let keys = Object.keys(this.props.categoryMap);
        let cLen = [];

        for (let i = 0; i < keys.length; i++) {
            categories.push(<GraphPanel key={i} val={keys[i]} test={this.updateCategory.bind(this)}/>)
            cLen.push(this.props.categoryMap[keys[i]].length);
        }

        const gData = {
            datasets: [
                {
                    data: cLen,
                    backgroundColor: [
                        "#FF6384",
                        "#4BC0C0",
                        "#FFCE56",
                        "#4d4f53",
                        "#36A2EB",
                        "#71b37b",
                        "#f38b79",
                        "#ac79f2"

                    ],
                    label: "Categories" // for legend
                }
            ],
            labels: keys
        };

        console.log(cLen);
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
                        <div className="graph-panel">
                            <h1> Category Data </h1>
                            <Pie
                                data={gData}
                                width={400}
                                height={400}
                                options={{
                                    maintainAspectRatio: true,
                                    legend: {
                                        labels: {
                                            fontColor: "white",
                                            fontSize: 16
                                        }
                                    }
                                }}

                            />
                        </div>
                    </div>
                    }
                </div>
            </div>
        );
    }
}
