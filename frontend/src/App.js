import React, { Component } from 'react';
// import logo from './logo.svg';
import './App.css';
import MapComponent from './Components/MapComponent'
import {BrowserRouter as Router } from 'react-router-dom'

class App extends Component {
  render() {
    return (
      <Router>
         
            <MapComponent />
       
      </Router>
    );
  }
}

export default App;