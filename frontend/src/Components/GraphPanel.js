import React, {Component} from "react";
import {Doughnut} from "react-chartjs-2";

export default class GraphPanel extends Component {
    render() {

        const graphData = {
            datasets:
                [
                    {
                        data: this.props.gData,
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
            labels: this.props.labels
        };

        return (
            <div className="graph-panel">
                <h1> {this.props.title} </h1>
                <Doughnut
                    data={graphData}
                    width={100}
                    height={100}
                    options={{
                        maintainAspectRatio: false,
                        legend: {
                            labels: {
                                fontColor: "white",
                                fontSize: 16
                            }
                        }
                    }}

                />
            </div>)
    }
}