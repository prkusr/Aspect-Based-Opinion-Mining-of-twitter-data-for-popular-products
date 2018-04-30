import React, { Component } from "react";
import InfoSubPanel from './InfoSubPanel';

export default class InfoPanel extends Component {
	render() {
		const opinion = this.props.opinion;
		const title1 = opinion.category;
		return (

			
			<div className="col-sm-5-c thumbnail">
				<div className="row">
					<center>
						<h3> {title1} </h3>
					</center>
				</div>
				
				<InfoSubPanel type={true} tweets={opinion.info}/>
				<InfoSubPanel type={false} tweets={opinion.info}/>
				
			</div>

			
			
		);
	}
}
