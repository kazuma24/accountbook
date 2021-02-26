'use strict';

const React = require('react');


import SpendingCategoryList from './SpendingCategoryList';
import SpendingCategoryEditForm from './SpendingCategoryEditForm';
import axios from 'axios';
import Redirect from '../Redirect';

export default class SpendingCategoryEdit extends React.Component {
    constructor(props) {
        console.log("SpendingCategoryEdit: constructor()")
        super(props);
        this.state = {
            spendingCategoryList: [],
            accountId: this.props.match.params.accountId,
        }
        this.changeSpendingList = this.changeSpendingList.bind(this);
    }

    changeSpendingList(list) {
        this.setState({
            spendingCategoryList :list
        });
    }

    componentWillMount() {
        console.log("SpendingCategoryEdit: componentWillMount()")
        const spendingCategoryFetch = async () => {
            try {
                const response = await axios.get(`/spendingcategorylist/${this.state.accountId}`);
                console.log(response.data);
                const data = response.data;
                if("errorCode" in data) {
                    const errorCode = data.errorCode;
                    Redirect(errorCode)
                } else {
                    console.log(JSON.stringify(data));
                    this.setState({
                        spendingCategoryList: data
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
        spendingCategoryFetch();
    }

    render() {
        console.log("SpendingCategoryEdit: render()")
        const state = this.state;
        return (
            <div className="spending-category-edit">
                <div className="category-list">
                   <SpendingCategoryList 
                    spendingCategoryList={state.spendingCategoryList} 
                    />
                </div>
                <div className="category-edit-form">
                    <SpendingCategoryEditForm 
                     spendingCategoryList={state.spendingCategoryList}
                     accountId={state.accountId}
                     changeSpendingList={this.changeSpendingList} />
                </div>
            </div>
        )
    }

    

    
}