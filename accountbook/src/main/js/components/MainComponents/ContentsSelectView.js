'use strict';

const React = require('react');

export default class ContentsSelectView extends React.Component {
    constructor(props) {
        super(props);
        this.state ={
            inputClassFlag: true,
            calendarClassFlag: false,
            reportClassFlag: false,
            otherClassFlag: false
        };
    }
    inputPageShowHndle() {
        this.setState({
            inputClassFlag: true,
            calendarClassFlag: false,
            reportClassFlag: false,
            otherClassFlag: false
        });
        return this.props.inputPageShow();
    }
    calenderPageShowHandle() {
        this.setState({
            inputClassFlag: false,
            calendarClassFlag: true,
            reportClassFlag: false,
            otherClassFlag: false
        });
        return this.props.calederPageShow();
    }
    reportPageShowHandle() {
        this.setState({
            inputClassFlag: false,
            calendarClassFlag: false,
            reportClassFlag: true,
            otherClassFlag: false
        });
        return this.props.reportPageShow();
    }
    otherPageShowHandle() {
        this.setState({
            inputClassFlag: false,
            calendarClassFlag: false,
            reportClassFlag: false,
            otherClassFlag: true
        });
        return this.props.otherPageShow();
    }
    render() {
        return (
            <div className="select-view">
                <div className="select-view-button-area">
                    <button className={this.state.inputClassFlag ? "selected" : ""} onClick={() => {this.inputPageShowHndle();}}>入力</button>
                    <button className={this.state.calendarClassFlag ? "selected" : ""} onClick={() => {this.calenderPageShowHandle();}}>カレンダー</button>
                    <button className={this.state.reportClassFlag ? "selected" : ""} onClick={() => {this.reportPageShowHandle();}}>レポート</button>
                    <button className={this.state.otherClassFlag ? "selected" : ""} onClick={() => {this.otherPageShowHandle();}}>その他</button>
                </div>
            </div>
        )
    }
}