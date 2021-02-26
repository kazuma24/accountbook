'use strict';

const React = require('react');
import AddNewIncomeCategoryForm from './form/AddNewIncomeCategoryForm';
import DeleteIncomeCategoryForm from './form/DeleteIncomeCategoryForm';
import EditIncomeCategoryForm from './form/EditIncomeCategoryForm';

export default class IncomeCategoryEditForm extends React.Component {
    constructor(props) {
        console.log("IncomeCategoryEditForm: constructor()")
        super(props);
        this.chengeIncomeList = this.chengeIncomeList.bind(this);
    }

    chengeIncomeList(list) {
        return this.props.changeIncomeList(list)
    }
    
    render() {
        console.log("IncomeCategoryEditForm: render()")
        const incomeCategoryList = this.props.incomeCategoryList;
        const accountId = this.props.accountId;
        return (
            <React.Fragment>
                <div className="addnew-category-form">
                    <h3 className="content-title">新規カテゴリ追加</h3>
                    <AddNewIncomeCategoryForm
                     incomeCategoryList={incomeCategoryList} 
                     accountId={accountId} 
                     changeIncomeList={this.chengeIncomeList} 
                    />
                </div>
                <div className="edit-category-form">
                    <h3 className="content-title">カテゴリ名編集</h3>
                    <EditIncomeCategoryForm
                     incomeCategoryList={incomeCategoryList} 
                     accountId={accountId} 
                     changeIncomeList={this.chengeIncomeList} 
                    />
                </div>
                <div className="delete-category-form">
                    <h3 className="content-title">カテゴリ削除</h3>
                    <DeleteIncomeCategoryForm
                     incomeCategoryList={incomeCategoryList} 
                     accountId={accountId} 
                     changeIncomeList={this.chengeIncomeList}    
                    />
                </div>
            </React.Fragment>
        )
    }
}