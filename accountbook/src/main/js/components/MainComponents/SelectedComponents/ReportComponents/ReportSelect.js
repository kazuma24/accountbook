'use strict';

const React = require('react');

export default class ReportSelect extends React.Component {
    constructor(props) {
        super(props);
        this.state ={
            borderUnset: true,
        };
    }
    monthryPageShowHandle() {
        this.setState((state) => {
            return {
                borderUnset: !state.borderUnset
            }
        });
        return this.props.monthryPageShow();
    }
    yearPageShowHandle() {
        this.setState((state) => {
            return {
                borderUnset: !state.borderUnset
            }
        });
        return this.props.yearPageShow();
    }
    render() {
        return (
            <div className="input-select">
                <button className={this.state.borderUnset ? "border-unset" : ""} onClick={() => {this.monthryPageShowHandle();}}>月間</button>
                <button className={!this.state.borderUnset ? "border-unset" : ""} onClick={() => {this.yearPageShowHandle();}}>年間</button>
            </div>
        )
    }
}