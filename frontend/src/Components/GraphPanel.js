import React, { Component } from "react";
import { Doughnut, Polar } from "react-chartjs-2";

export default class GraphPanel extends Component {
	constructor(props) {
		super(props);
		this.state = { visible : false };
	}


	show() {
		// this.state.visible = true;
		this.setState({ visible : true });
	}

	render() {
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

		// let visible =
		let disp = { display:  "block" };

		return (
			
				
				<div className="graph-panel" style={disp}>
					<div className="category">
						Yayyy
					</div>
				

				
					<div className="category">
						Yayyy
					</div>
				

				
					<div className="category">
						Yayyy
					</div>
				

				
					<div className="category">
						Yayyy
					</div>
				</div>
			
		);
	}
}
