import React, { Component } from "react";
import ReactDOM from "react-dom";
import SparkAppBody from "./SparkAppBody";
import { fetchPoints } from "../RESTApi/consumer";

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

		const color = {
			color: "black"
		};
		
		const body = this.state.opinions.length > 0 && 
					<SparkAppBody google={this.props.google} opinions={this.state.opinions}/>
				
		return (
			<div>
				<div className="header">
					<h1 style={h1Style}>SparkBusters</h1>
					<h2 style={color}>
						Twitter Aspect Based Opinion Mining and Sentiment
						Analysis
					</h2>

					<div className="container">
						<div className="row">
							<div className="col-md-10 col-md-offset-1">
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
				{body}
			</div>
		);
	}
}
