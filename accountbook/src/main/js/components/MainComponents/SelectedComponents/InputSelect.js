'use strict';

const React = require('react');

export default class InputSelect extends React.Component {
    constructor(props) {
        super(props);
    }
    spendingPageShowHandle() {
        return this.props.spendingPageShow();
    }
    incomePageShowHandle() {
        return this.props.incomePageShow();
    }
    render() {
        return (
            <div className="input-select">
                <button onClick={() => {this.spendingPageShowHandle();}}>支出</button>
                <button onClick={() => {this.incomePageShowHandle();}}>収入</button>
            </div>
        )
    }
}