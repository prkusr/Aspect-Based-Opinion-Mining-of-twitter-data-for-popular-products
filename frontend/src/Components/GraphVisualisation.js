import React, { Component } from "react";
import InfoPanel from "./InfoPanel";

export default class GraphVisualisation extends Component {
	render() {
		let categoryMap = this.props.opinion;
		let info = [];
		let keys = Object.keys(categoryMap);

		for (let i = 0; i < keys.length; i++) {
			let op = {
				category: keys[i],
				info: categoryMap[keys[i]]
			};

			info.push(<InfoPanel key={i} opinion={op} />);
			// if(i % 2 == 0){
				info.push(
						<div key={i + keys.length} className="col-sm-custom" />
					);
			// }
			// if (keys.length % 2 == 0 && i + 1 != keys.length) {
				
				// op = {
				// 	category: keys[i + 1],
				// 	info: categoryMap[keys[i + 1]]
				// };
				// info.push(<InfoPanel key={i + 1} opinion={op} />);
			// }
		}

		// for (let i = 0; i < opinion.length; i += 2) {
		// 	info.push(<InfoPanel key={i} opinion={opinion[i]} />);
		// 	info.push(
		// 		<div key={i + opinion.length} className="col-sm-custom" />
		// 	);
		// 	info.push(<InfoPanel key={i + 1} opinion={opinion[i + 1]} />);
		// }

		// const vSpace = {
		// 	width: "2.5%"
		// };

		const m_15 = {
			marginLeft: "4%"
		};

		return (
			<div className="row top-buffer" style={m_15}>
				{info}
			</div>
		);
	}
}
