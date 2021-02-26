'use strict';

import axios from 'axios';
import Redirect from '../../Redirect';

const React = require('react');


export default class AddNewCategoryForm extends React.Component {
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
            errorMessage: '',
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
        const registedList = this.state.spendingCategoryList;
        const accountId = this.state.accountId;
        const spendingCategoryName = this.state.params.spendingCategoryName;
        const spendingCategoryColor = this.state.params.spendingCategoryColor;
        let checkFlag = false;

        if(accountId === null, accountId === '') {
            checkFlag = true;
            this.setState({
                errorMessage: 'Idの情報がありません'
            });
        }
        //カテゴリチェック
        if(spendingCategoryName === null || spendingCategoryName === '') {
            checkFlag = true;
            this.setState({
                errorMessage: 'カテゴリ名を入力してください'
            });
        }
        //既に登録されているカテゴリ名かチェック
        if(registedList.includes(spendingCategoryName)) {
            checkFlag = true;
            this.setState({
                errorMessage: 'カテゴリ名が登録済です'
            });
        }
        //カラーチェック
        if(spendingCategoryColor === '#FFFFFF') {
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
            axios.post("http://localhost:8080/addnewrequest", params)
            .then(response => {
                const res = response.data;
                console.log(JSON.stringify(res));
                const message = '以下の内容を登録しました。\n' + 'カテゴリ名: ' + spendingCategoryName;
                alert(message);
                this.chengeSpendingList(res);
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
                        maxLength="30"
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