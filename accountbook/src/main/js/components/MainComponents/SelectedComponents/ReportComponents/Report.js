'use strict';

const React = require('react');


import ReportSelect from './ReportSelect';
import YearReport from './YearReport';
import MonthryReport from './MonthryReport';



export default class Report extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            page: {
                monthry: true,
                year: false,
            },
        };
    }
    render() {
        console.log("Report.js(render)");
        const state = this.state.page;
        return (
            <React.Fragment>
                <ReportSelect 
                    monthryPageShow={() => {this.monthryPageShow();}}
                    yearPageShow={() => {this.yearPageShow();}}
                /> 
                {state.monthry && <MonthryReport /> }
                {state.year && <YearReport />}
            </React.Fragment>
        )
    }
    /*関数*/
    monthryPageShow() {
        this.setState({
            page: {
                monthry: true,
                year: false
            },
        });
    }
    yearPageShow() {
        this.setState({
            page: {
                monthry: false,
                year: true,
            },
        });
    } 
}