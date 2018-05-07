import React, {Component} from "react";
import {Doughnut, Polar} from "react-chartjs-2";

export default class SparkPanel extends Component {
    constructor(props) {
        super(props);
        this.state = {visible: false};
    }


    show() {
        // this.state.visible = true;
        // this.setState({visible: true});
    }

    handleClick = () => {
        this.props.test(this.props.val);
    }

    render() {
        let v = this.props.val;
        let val = v.charAt(0).toUpperCase() + v.slice(1);

        return (
                <div className="category" value={this.props.val} onClick={this.handleClick}>
                    {val}
                </div>

        );
    }
}
