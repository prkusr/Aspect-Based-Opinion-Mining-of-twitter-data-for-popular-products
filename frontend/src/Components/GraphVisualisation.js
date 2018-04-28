import React, { Component } from "react";

export default class GraphVisualisation extends Component {
	render() {
		const heightFake = {
			height: "100px"
		};

		return (
			<div className="row">
				<div className="col-sm-3">
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

				<div className="col-sm-3">
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

				<div className="col-sm-3">
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

				<div className="col-sm-3">
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
		);
	}
}
