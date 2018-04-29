import React, { Component } from "react";

export default class InfoPanel extends Component {
	render() {
		const title1 = this.props.title1 ? this.props.title1 : "";
		const title2 = this.props.title2 ? this.props.title2 : "";

		return (

			<div>
			<div className="col-sm-5-c thumbnail">
				<div className="row">
					<center>
						{" "}
						<h3> {title1} </h3>
					</center>
				</div>
				<div className="col-sm-6">
					<div className="thumbnail">
						<img src="http://placehold.it/750x325" alt="..." />
						<div className="caption">
							<h3>{title1}</h3>
							<p>...</p>
						</div>
					</div>
				</div>

				<div className="col-sm-6">
					<div className="thumbnail">
						<img src="http://placehold.it/750x325" alt="..." />
						<div className="caption">
							<h3>{title1}</h3>
							<p>...</p>
						</div>
					</div>
				</div>
			</div>

			<div className="col-sm-custom" />

			<div className="col-sm-5-c thumbnail">
				<div className="row">
					<center>
						{" "}
						<h3> {title2} </h3>
					</center>
				</div>
				<div className="col-sm-6">
					<div className="thumbnail">
						<img src="http://placehold.it/750x325" alt="..." />
						<div className="caption">
							<h3>{title2}</h3>
							<p>...</p>
						</div>
					</div>
				</div>

				<div className="col-sm-6">
					<div className="thumbnail">
						<img src="http://placehold.it/750x325" alt="..." />
						<div className="caption">
							<h3>{title2}</h3>
							<p>...</p>
						</div>
					</div>
				</div>
			</div>

			</div>
		);
	}
}
