'use strict';

const React = require('react');


import InputSelect from './InputSelect';
import IncomeInput from './IncomeInput';
import SpendingInput from './SpendingInput';



export default class Input extends React.Component {
    
    constructor(props) {
        super(props);
        console.log("Input.js(constructor)");
        this.state = {
            page: {
                spending: true,
                income: false,
            },
        };
    }
    render() {
        console.log("Input.js(render)");
        const state = this.state.page;
        return (
            <React.Fragment>
                <InputSelect 
                    spendingPageShow={() => {this.spendingPageShow();}}
                    incomePageShow={() => {this.incomePageShow();}}
                /> 
                {state.spending && <SpendingInput /> }
                {state.income && <IncomeInput />}
            </React.Fragment>
        )
    }

    /*関数*/
    spendingPageShow() {
        this.setState({
            page: {
                spending: true,
                income: false
            },
        });
    }
    incomePageShow() {
        this.setState({
            page: {
                spending: false,
                income: true,
            },
        });
    } 
}