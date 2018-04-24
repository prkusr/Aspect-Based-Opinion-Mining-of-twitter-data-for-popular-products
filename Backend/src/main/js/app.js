const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');


class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {ping: []};
	}

	componentDidMount() {
		client({method: 'GET', path: '/ping'}).done(response => {
			this.setState({ping: response.entity});
		});
	}

	render() {
		return (
			<Ping ping={this.state.ping} />
		)
	}
}


class PingList extends React.Component{
	render() {
		var pings = this.props.ping.map(ping =>
			<Ping key={ping.lat} ping={ping}/>
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>Weight</th>
						<th>Lat</th>
						<th>Lng</th>
					</tr>
					{pings}
				</tbody>
			</table>
		)
	}
}

class Ping extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.ping.weight}</td>
				<td>{this.props.ping.lat}</td>
				<td>{this.props.ping.lng}</td>
			</tr>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)