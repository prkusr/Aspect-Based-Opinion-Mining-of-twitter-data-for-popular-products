import React, {Component} from "react";

export default class SparkPanel extends Component {

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
