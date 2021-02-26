'use strict';

const React = require('react');

export default class InputSelect extends React.Component {
    constructor(props) {
        super(props);
        this.state ={
            borderUnset: true,
        };
    }
    spendingPageShowHandle() {
        this.setState((state) => {
            return {
                borderUnset: !state.borderUnset
            }
        });
        return this.props.spendingPageShow();
    }
    incomePageShowHandle() {
        this.setState((state) => {
            return {
                borderUnset: !state.borderUnset
            }
        });
        return this.props.incomePageShow();
    }
    render() {
        return (
            <div className="input-select">
                <button className={this.state.borderUnset ? "border-unset" : ""} onClick={() => {this.spendingPageShowHandle();}}>支出</button>
                <button className={!this.state.borderUnset ? "border-unset" : ""} onClick={() => {this.incomePageShowHandle();}}>収入</button>
            </div>
        )
    }
}