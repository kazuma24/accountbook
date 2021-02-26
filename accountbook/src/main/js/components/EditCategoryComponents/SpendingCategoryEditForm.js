'use strict';

const React = require('react');
import AddNewSpendingCategoryForm from './form/AddNewSpendingCategoryForm';
import EditSpendingCategoryForm from './form/EditSpendingCategoryForm';
import DeleteSpendingCategoryForm from './form/DeleteSpendingCategoryForm';


export default class SpendingCategoryEditForm extends React.Component {
    constructor(props) {
        console.log("SpendingCategoryEditForm: constructor()")
        super(props);
        this.chengeSpendingList = this.chengeSpendingList.bind(this);
    }

    chengeSpendingList(list) {
        return this.props.changeSpendingList(list)
    }
    
    render() {
        console.log("SpendingCategoryEditForm: render()")
        const spendingCategoryList = this.props.spendingCategoryList;
        const accountId = this.props.accountId;
        return (
            <React.Fragment>
                <div className="addnew-category-form">
                    <h3 className="content-title">新規カテゴリ追加</h3>
                    <AddNewSpendingCategoryForm 
                     spendingCategoryList={spendingCategoryList} 
                     accountId={accountId} 
                     changeSpendingList={this.chengeSpendingList} 
                    />
                </div>
                <div className="edit-category-form">
                    <h3 className="content-title">カテゴリ名編集</h3>
                    <EditSpendingCategoryForm 
                     spendingCategoryList={spendingCategoryList} 
                     accountId={accountId} 
                     changeSpendingList={this.chengeSpendingList} 
                    />
                </div>
                <div className="delete-category-form">
                    <h3 className="content-title">カテゴリ削除</h3>
                    <DeleteSpendingCategoryForm 
                     spendingCategoryList={spendingCategoryList} 
                     accountId={accountId} 
                     changeSpendingList={this.chengeSpendingList}    
                    />
                </div>
            </React.Fragment>
        )
    }
}