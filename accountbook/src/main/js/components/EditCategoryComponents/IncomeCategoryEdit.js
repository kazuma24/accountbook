'use strict';

const React = require('react');

import IncomeCategoryList from './IncomeCategoryList.js';
import IncomeCategoryEditForm from './IncomeCategoryEditForm';
import axios from 'axios';
import Redirect from '../Redirect';

export default class IncomeCategoryEdit extends React.Component {
    constructor(props) {
        console.log("IncomeCategoryEdit: constructor()")
        super(props);
        this.state = {
            incomeCategoryList: [],
            accountId: this.props.match.params.accountId,
        }
        this.changeIncomeList = this.changeIncomeList.bind(this);
    }

    changeIncomeList(list) {
        this.setState({
            incomeCategoryList :list
        });
    }

    componentWillMount() {
        console.log("IncomeCategoryEdit: componentWillMount()")
        const incomeCategoryFetch = async () => {
            try {
                const response = await axios.get(`/incomecategorylist/${this.state.accountId}`);
                console.log(response.data);
                const data = response.data;
                if("errorCode" in data) {
                    const errorCode = data.errorCode;
                    Redirect(errorCode)
                } else {
                    console.log(JSON.stringify(data));
                    this.setState({
                        incomeCategoryList: data
                    });
                }
            } catch(error) {
                const err = error.response;
                if(err) {
                    const errorCode = err.status;
                    Redirect(errorCode);
                }else {
                    Redirect(500);
                }
            }
        }
        incomeCategoryFetch();
    }

    render() {
        console.log("IncomeCategoryEdit: render()")
        const state = this.state;
        return (
            <div className="income-category-edit">
                <div className="category-list">
                   <IncomeCategoryList 
                    incomeCategoryList={state.incomeCategoryList} 
                    />
                </div>
                <div className="category-edit-form">
                    <IncomeCategoryEditForm 
                     incomeCategoryList={state.incomeCategoryList}
                     accountId={state.accountId}
                     changeIncomeList={this.changeIncomeList} />
                </div>
            </div>
        )
    }

    

    
}