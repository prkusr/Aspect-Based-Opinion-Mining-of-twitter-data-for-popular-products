import React, { Component } from "react";
import ReactDOM from "react-dom";
import SparkAppBody from "./SparkAppBody";
import { fetchPoints } from "../RESTApi/consumer";
// import {PieChart}  from "react-chartjs/Pie";
import { Doughnut,Polar } from "react-chartjs-2";

export default class SparkWrapper extends Component {
	constructor(props) {
		super(props);
		this.state = { opinions: [] };
	}

	fetchPositions(q) {
		const searchRef = this.refs.search;
		const node = ReactDOM.findDOMNode(searchRef);
		// return fetchPoints(node.value).then(r => {
		// 	this.setState({ opinions: r });
		// });
		//console.log(fetchPoints(node.value))
		return this.setState({ opinions: fetchPoints(node.value) });
	}

	render() {
		const h1Style = {
			fontSize: "70px",
			color: "black"
		};

		const width_p22 = {
			width: "22%",
			marginLeft: "1.25%",
			// position:"fixed"
		};

		// const body = this.state.opinions.length > 0 &&
		// <SparkAppBody google={this.props.google} opinions={this.state.opinions}/>
		const data = {
			datasets: [
				{
					data: [11, 16, 7, 3, 14],
					backgroundColor: [
						"#FF6384",
						"#4BC0C0",
						"#FFCE56",
						"#E7E9ED",
						"#36A2EB"
					],
					label: "My dataset" // for legend
				}
			],
			labels: ["Red", "Green", "Yellow", "Grey", "Blue"]
		};

		return (
			<div>
				<div className="row">
					{/*<h1 style={h1Style}>SparkBusters</h1>
					<h2 style={color}>
						Twitter Aspect Based Opinion Mining and Sentiment
						Analysis
					</h2>
					*/}
					<div className="col-md-3 col-md-offset-1" style={width_p22}>
						<div className="row">
							<div className="col-md-12">
								<div id="custom-search-input">
									<div className="input-group col-md-12">
										<input
											ref="search"
											type="text"
											className="form-control input-lg"
											placeholder="Enter a product name"
										/>
										<span className="input-group-btn">
											<button
												className="btn btn-info btn-lg"
												type="button"
												onClick={this.fetchPositions.bind(
													this
												)}
											>
												<i className="glyphicon glyphicon-search" />
											</button>
										</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div className="graph-panel">
				<Polar data={data} width={640}
					height={260}
					options={{
						maintainAspectRatio: false
					}}/>
				</div>

				<div className="graph-panel">
				<Polar data={data} width={640}
					height={260}
					options={{
						maintainAspectRatio: false
					}}/>
				</div>

				<div className="graph-panel">
				<Polar data={data} width={640}
					height={260}
					options={{
						maintainAspectRatio: false
					}}/>
				</div>

				<div className="graph-panel">
				<Polar data={data} width={640}
					height={260}
					options={{
						maintainAspectRatio: false
					}}/>
				</div>
				{/*{body} */}
			</div>
		);
	}
}
