import React, { Component } from 'react';
import { Route } from 'react-router-dom'
// import EarthquakesForm from './EarthquakesForm'
import TweetMap from './TweetMap'
import { fetchPoints } from '../RESTApi/consumer'
import { GoogleApiWrapper } from 'google-maps-react'

class MapComponent extends Component{

	state = {
		positions: []
	}

	componentDidMount = () => {
    fetchPoints()
      .then((point) => {
      	// console.log(point)
        	this.setState({ positions: point }, )
      })
  }

  render() {
    return (
      <div className="MapComponent">
        <h1> SparkBusters</h1>
	        <div className="wrapper">
	          <Route path="/" render={(props) => <TweetMap google={this.props.google} positions={this.state.positions} {...props}/>}/>
	        </div>
      </div>
    );
  }


}

export default GoogleApiWrapper({
	apiKey: '',
	libraries: ['visualization']
})(MapComponent)
