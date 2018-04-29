import React, { Component } from "react";
import ReactDOM from "react-dom";


export default class SparkSearchBar extends Component {
	constructor(props) {
		super(props);
		this.state = {positions: []};
	}

	handleClick() {
		const searchRef = this.refs.search;
		const node = ReactDOM.findDOMNode(searchRef);
		this.props.fetchPositions(node.value)
			.then( (r) => 
      			this.setState({positions: r})
    	);

	}

	render() {
		return (
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
										onClick={this.handleClick.bind(this)}
									>
										<i className="glyphicon glyphicon-search" />
									</button>
								</span>
							</div>
						</div>
					</div>
				</div>
				<div dangerouslySetInnerHTML={{ __html: JSON.stringify(this.state.positions) }} />
						</div>

		);
	}
}
