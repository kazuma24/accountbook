'use strict';

const React = require('react');


import InputSelect from './InputSelect';
import IncomeInput from './InputComponents/IncomeInput';
import SpendingInput from './InputComponents/SpendingInput';



export default class Input extends React.Component {
    
    constructor(props) {
        super(props);
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

    /* 関数 */
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