import React, { Component } from "react";

export default class SparkSearchBar extends Component {

	render(){

		return(
			<div className="container">
				<div className="row">
			        <div className="col-md-10 col-md-offset-1">
			            <div id="custom-search-input">
			                <div className="input-group col-md-12">
			                    <input type="text" className="form-control input-lg" placeholder="Enter a product name" />
			                    <span className="input-group-btn">
			                        <button className="btn btn-info btn-lg" type="button">
			                            <i className="glyphicon glyphicon-search"></i>
			                        </button>
			                    </span>
			                </div>
			            </div>
			        </div>
				</div>
			</div>
         );
	}
}
	