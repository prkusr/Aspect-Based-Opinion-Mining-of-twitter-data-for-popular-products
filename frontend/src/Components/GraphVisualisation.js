import React, { Component } from "react";
import InfoPanel from './InfoPanel';

export default class GraphVisualisation extends Component {
	render() {
		const opinion = this.props.opinion;
		let t;
		let t2; 
		let info = [];


		if (opinion[0] != undefined) {
			console.log(opinion);
			
			for(let i = 0; i < opinion.length ; i += 2 ){
				t = opinion[i].category;
				t2 = opinion[i+1].category;
			}
		}
		// console.log(t);
		const vSpace = {
			width: "2.5%"
		};

		const m_15 = {
			marginLeft: "4%"
		};

		return (
			<div className="row top-buffer" style={m_15}>
				<InfoPanel title1={t} title2={t2}/>
			</div>
		);
	}
}
