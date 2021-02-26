'use strict';

const React = require('react');

import {
	BrowserRouter as Router,
	Switch,
	Route,
} from 'react-router-dom';

import ContentsSelectView from './MainComponents/ContentsSelectView';
import Input from './MainComponents/SelectedComponents/InputComponents/Input';
import Calender from './MainComponents/SelectedComponents/Calender/calender';
import Report from './MainComponents/SelectedComponents/ReportComponents/Report';
import Other from './MainComponents/SelectedComponents/OtherComponents/Other';
import MonthrySpendingRatio from './MonthryRatioComponents/MonthrySpendingRatio';
import MonthryIncomeRatio from './MonthryRatioComponents/MonthryIncomeRatio';
import YearSpendingRatio from './YearRatioComponents/YearSpendingRatio';
import YearIncomeRatio from './YearRatioComponents/YearIncomeRatio';
import SpendingCategoryEdit from './EditCategoryComponents/SpendingCategoryEdit';
import IncomeCategoryEdit from './EditCategoryComponents/IncomeCategoryEdit';



export default class MainView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            page: {
                input: true,
                calender: false,
                report: false,
                other:false,
            },
        };
    }
    inputPageShow() {
        this.setState({
            page: {
                input: true,
                calender: false,
                report: false,
                other: false,
            },
        });
    }
    calederPageShow() {
        this.setState({
            page: {
                input: false,
                calender: true,
                report: false,
                other: false,
            },
        });
    }
    reportPageShow() {
        this.setState({
            page: {
                input: false,
                calender: false,
                report: true,
                other: false,
            },
        });
    }
    otherPageShow() {
        this.setState({
            page: {
                input: false,
                calender: false,
                report: false,
                other: true,
            },
        });
    }
    render() {
        const state = this.state.page;
        return (
            <div className="main-view">
            <Router>
               <Switch>
				<Route exact path="/main" render={() => (
					<React.Fragment>
						<ContentsSelectView
                         inputPageShow={() => {this.inputPageShow();}}
                         calederPageShow={() => {this.calederPageShow();}}
                         reportPageShow={() => {this.reportPageShow();}}
                         otherPageShow={() => {this.otherPageShow();}} 
                        />
                        {state.input && <Input />}
                        {state.calender && <Calender />}
                        {state.report && <Report />}
                        {state.other && <Other />}
					</React.Fragment>
				)} />
                <Route exact path="/spendingedit/:accountId" component={SpendingCategoryEdit} />
                <Route exact path="/incomeedit/:accountId" component={IncomeCategoryEdit} />
                <Route exact path="/monthryspendingratio/:year/:month" component={MonthrySpendingRatio} />
                <Route exact path="/monthryincomeratio/:year/:month" component={MonthryIncomeRatio} />
                <Route exact path="/yearspendingratio/:year" component={YearSpendingRatio} />
                <Route exact path="/yearincomeratio/:year" component={YearIncomeRatio} />
			   </Switch>
            </Router>
            </div>
        )
    }
}
