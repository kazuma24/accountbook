'use strict';

import axios from 'axios';
import { withRouter } from 'react-router-dom';
import Redirect from '../../Redirect';

const React = require('react');


class AddNewIncomeCategoryForm extends React.Component {
    constructor(props) {
        console.log("AddNewCategoryForm: constructor()")
        super(props);
        this.state={
            incomeCategoryList: this.props.incomeCategoryList,
            accountId: this.props.accountId,
            params:{
                incomeCategoryName:'',
                incomeCategoryColor:'#FFFFFF',
            },
        }
        this.incomeCategoryNameChangehandle = this.incomeCategoryNameChangehandle.bind(this);
        this.incomeCategoryColorChangehandle = this.incomeCategoryColorChangehandle.bind(this);
        this.addNewCategory = this.addNewCategory.bind(this);
        this.chengeIncomeList = this.chengeIncomeList.bind(this);
    }
    incomeCategoryNameChangehandle(event){
        const value = event.target.value;
        this.setState((state) => {
            return {
                params:{
                    incomeCategoryName: value,
                    incomeCategoryColor: state.params.incomeCategoryColor,
                }
            }
        });
    }
    incomeCategoryColorChangehandle(event) {
        const value = event.target.value;
        this.setState((state) => {
            return {
                params:{
                    incomeCategoryName: state.params.incomeCategoryName,
                    incomeCategoryColor: value,
                }
            }
        });
    }
    chengeIncomeList(list) {
        return this.props.changeIncomeList(list)
    }
    //新規追加
    addNewCategory() {
        const registedList = this.props.incomeCategoryList;
        let registedNameList = [];
        let count = registedList.length;
        for(let idx = 0; idx < count; idx++) {
            registedNameList += registedList[idx].incomeCategoryName
        }
        const accountId = this.state.accountId;
        const incomeCategoryName = this.state.params.incomeCategoryName;
        const incomeCategoryColor = this.state.params.incomeCategoryColor;

        var reg = new RegExp(/[!"#$%&'()\*\+\-\.,\/:;<=>?@\[\\\]^_`{|}~]/g);

        let checkFlag = false;

        if(accountId === null, accountId === '') {
            checkFlag = true;
            alert('IDの情報がありません');
        } else if(incomeCategoryName === null || incomeCategoryName === ''){
            checkFlag = true;
            alert('カテゴリ名を入力してください');
        } else if(incomeCategoryName.length > 20) {
            checkFlag = true;
            alert('カテゴリ名は20文字以内で入力してください')
        } else if(reg.test(incomeCategoryName)) {
            checkFlag = true;
            alert('カテゴリ名に記号は使えません');
        } else if(registedNameList.includes(incomeCategoryColor)) {
            checkFlag = true;
            alert('カテゴリ名が登録済です');
        } else if(incomeCategoryColor === '#FFFFFF') {
            const result = window.confirm('カラーを選択していませんがよろしいですか？');
            if(!result) {
                checkFlag = true;
            }
        }

       

        if(!checkFlag) {
            const params = {
                accountId: Number(accountId),
                incomeCategoryName: incomeCategoryName,
                incomeCategoryColor: incomeCategoryColor,
            };
            const url = "http://localhost:8080/addnewrequestincome";
            axios.post(url, params)
            .then(response => {
                const res = response.data;
                if("message" in res) {
                    alert(res.message)
                } else {
                    console.log(JSON.stringify(res));
                    const message = '以下の内容を登録しました。\n' + 'カテゴリ名: ' + incomeCategoryName;
                    alert(message);
                    this.chengeIncomeList(res);
                }
            }).catch(error => {
                const err = error.response;
                if(err) {
                    const errorCode = err.status;
                    Redirect(errorCode);
                }else {
                    Redirect(500);
                }
            })
        }


    }
    
    render() {
        return (
            <div className="form-content">
                <div style={{display:"flex"}}>
                    <div className="befor-area">
                        <div>名前</div>
                        <input 
                        type="text"
                        maxLength="20"
                        className="income-category-edit-input-form"
                        onChange={this.incomeCategoryNameChangehandle} />
                    </div>
                    <div className="after-area">
                        <div>カラー</div>
                        <input 
                        type="color"
                        style={{width:"50%"}}
                        value={this.state.params.incomeCategoryColor}
                        onChange={this.incomeCategoryColorChangehandle} />
                        <div className="edit-button-area">
                            <button
                            type="button"
                            className="prymari-button"
                            onClick={this.addNewCategory}
                            >
                            追加
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
export default withRouter(AddNewIncomeCategoryForm)