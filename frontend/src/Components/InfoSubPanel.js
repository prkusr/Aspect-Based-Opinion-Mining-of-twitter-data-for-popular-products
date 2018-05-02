import React, { Component } from "react";

export default class InfoSubPanel extends Component {
	render() {
		// const tweet = JSON.stringify(this.props.tweets)
		const t = this.props.tweets;
		let tweets = [];
		const sentiType = this.props.type ?  "Positive Sentiments" : "Negative Sentiments";
		
		for (let i = 0; i< t.length; i++){
			if(t[i].sentiment > 0 && this.props.type){
				tweets.push(<div key={i}><p> {t[i].tweet}</p><hr/></div>)
			}

			if(t[i].sentiment <= 0 && !this.props.type ){
				tweets.push(<div key={i} ><p > {t[i].tweet}</p><hr/></div>)
			}
		}

		const scrollAndHeight = {
			overflowY: "scroll",
    		maxHeight: "200px",
    		minHeight: "200px"
    	}

		return (
			
			
				<div className="thumbnail" style={scrollAndHeight}>
					<center>
						<h4>{sentiType}</h4>
					</center>
					<hr/>
					{tweets}
					<div className="caption" />
				</div>
		
		);
	}
}
