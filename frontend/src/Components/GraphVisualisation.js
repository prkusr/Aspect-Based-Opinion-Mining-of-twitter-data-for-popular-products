import React, { Component } from "react";

export default class GraphVisualisation extends Component {
	render() {
		const vSpace ={
			   width: "2.5%"
		};

		const m_15 ={
			marginLeft: "10%",
		};

		return (
			<div className="row top-buffer" style={m_15}>
				<div className="col-sm-5 thumbnail" >
					<div className="col-sm-6">
						<div className="thumbnail">
							<img src="http://placehold.it/750x325" alt="..." />
							<div className="caption">
								<h3>Thumbnail label</h3>
								<p>...</p>
								<p>
									<a
										href="#"
										className="btn btn-primary"
										role="button"
									>
										Button
									</a>{" "}
									<a
										href="#"
										className="btn btn-default"
										role="button"
									>
										Button
									</a>
								</p>
							</div>
						</div>
					</div>

					<div className="col-sm-6">
						<div className="thumbnail">
							<img src="http://placehold.it/750x325" alt="..." />
							<div className="caption">
								<h3>Thumbnail label</h3>
								<p>...</p>
								<p>
									<a
										href="#"
										className="btn btn-primary"
										role="button"
									>
										Button
									</a>{" "}
									<a
										href="#"
										className="btn btn-default"
										role="button"
									>
										Button
									</a>
								</p>
							</div>
						</div>
					</div>
				</div>
			<div className="col-sm-custom">
			</div> 
			<div className="col-sm-5 thumbnail">
					<div className="col-sm-6">
						<div className="thumbnail">
							<img src="http://placehold.it/750x325" alt="..." />
							<div className="caption">
								<h3>Thumbnail label</h3>
								<p>...</p>
								<p>
									<a
										href="#"
										className="btn btn-primary"
										role="button"
									>
										Button
									</a>{" "}
									<a
										href="#"
										className="btn btn-default"
										role="button"
									>
										Button
									</a>
								</p>
							</div>
						</div>
					</div>

					<div className="col-sm-6">
						<div className="thumbnail">
							<img src="http://placehold.it/750x325" alt="..." />
							<div className="caption">
								<h3>Thumbnail label</h3>
								<p>...</p>
								<p>
									<a
										href="#"
										className="btn btn-primary"
										role="button"
									>
										Button
									</a>{" "}
									<a
										href="#"
										className="btn btn-default"
										role="button"
									>
										Button
									</a>
								</p>
							</div>
						</div>
					</div>
				</div>

			</div>
		);
	}
}
