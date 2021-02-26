'use strict';

import axios from 'axios';
import { withRouter } from 'react-router-dom';
import Redirect from '../../Redirect';

const React = require('react');


class AddNewSpendingCategoryForm extends React.Component {
    constructor(props) {
        console.log("AddNewCategoryForm: constructor()")
        super(props);
        this.state={
            spendingCategoryList: this.props.spendingCategoryList,
            accountId: this.props.accountId,
            params:{
                spendingCategoryName:'',
                spendingCategoryColor:'#FFFFFF',
            },
        }
        this.spendingCategoryNameChangehandle = this.spendingCategoryNameChangehandle.bind(this);
        this.spendingCategoryColorChangehandle = this.spendingCategoryColorChangehandle.bind(this);
        this.addNewCategory = this.addNewCategory.bind(this);
        this.chengeSpendingList = this.chengeSpendingList.bind(this);
    }
    spendingCategoryNameChangehandle(event){
        const value = event.target.value;
        this.setState((state) => {
            return {
                params:{
                    spendingCategoryName: value,
                    spendingCategoryColor: state.params.spendingCategoryColor,
                }
            }
        });
    }
    spendingCategoryColorChangehandle(event) {
        const value = event.target.value;
        this.setState((state) => {
            return {
                params:{
                    spendingCategoryName: state.params.spendingCategoryName,
                    spendingCategoryColor: value,
                }
            }
        });
    }
    chengeSpendingList(list) {
        return this.props.changeSpendingList(list)
    }
    //新規追加
    addNewCategory() {
        const registedList = this.props.spendingCategoryList;
        let registedNameList = [];
        let count = registedList.length;
        for(let idx = 0; idx < count; idx++) {
            registedNameList += registedList[idx].spendingCategoryName
        }
        const accountId = this.state.accountId;
        const spendingCategoryName = this.state.params.spendingCategoryName;
        const spendingCategoryColor = this.state.params.spendingCategoryColor;

        var reg = new RegExp(/[!"#$%&'()\*\+\-\.,\/:;<=>?@\[\\\]^_`{|}~]/g);

        let checkFlag = false;

        if(accountId === null, accountId === '') {
            checkFlag = true;
            alert('IDの情報がありません');
        } else if(spendingCategoryName === null || spendingCategoryName === ''){
            checkFlag = true;
            alert('カテゴリ名を入力してください');
        } else if(spendingCategoryName.length > 20) {
            checkFlag = true;
            alert('カテゴリ名は20文字以内で入力してください')
        } else if(reg.test(spendingCategoryName)) {
            checkFlag = true;
            alert('カテゴリ名に記号は使えません');
        } else if(registedNameList.includes(spendingCategoryColor)) {
            checkFlag = true;
            alert('カテゴリ名が登録済です');
        } else if(spendingCategoryColor === '#FFFFFF') {
            const result = window.confirm('カラーを選択していませんがよろしいですか？');
            if(!result) {
                checkFlag = true;
            }
        }

       

        if(!checkFlag) {
            const params = {
                accountId: Number(accountId),
                spendingCategoryName: spendingCategoryName,
                spendingCategoryColor: spendingCategoryColor,
            };
            const url = "http://localhost:8080/addnewrequest";
            axios.post(url, params)
            .then(response => {
                const res = response.data;
                if("message" in res) {
                    alert(res.message)
                } else {
                    console.log(JSON.stringify(res));
                    const message = '以下の内容を登録しました。\n' + 'カテゴリ名: ' + spendingCategoryName;
                    alert(message);
                    this.chengeSpendingList(res);
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
                        className="spending-category-edit-input-form"
                        onChange={this.spendingCategoryNameChangehandle} />
                    </div>
                    <div className="after-area">
                        <div>カラー</div>
                        <input 
                        type="color"
                        style={{width:"50%"}}
                        value={this.state.params.spendingCategoryColor}
                        onChange={this.spendingCategoryColorChangehandle} />
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
export default withRouter(AddNewSpendingCategoryForm)