'use strict';


const React = require('react');
const axios = require('axios');

import { withRouter, Link } from 'react-router-dom';
import { Button, TextField } from '@material-ui/core';
import Redirect from '../../../Redirect';

const date = new Date();
var yyyy = date.getFullYear();
var mm = ("0"+(date.getMonth()+1)).slice(-2);
var dd = ("0"+ date.getDate()).slice(-2);
const spendingDefaultDate = yyyy + '-' + mm + '-' + dd;



class SpendingInput extends React.Component {
    constructor(props) {
        super(props);
        console.log("SpendingInput(constructor)");
        this.state ={
            spendingCategoryData: [],
            accountId: 0,
            param: {
                spendingDate: spendingDefaultDate,
                spendingAmount: '',
                spendingCategoyName: '',
            },
            errorMessages: {
                spendingDate: '',
                spendingAmount: '',
                spendingCategoyName: '',
            },
        };
        this.handleChangeSpendingAmount = this.handleChangeSpendingAmount.bind(this);
        this.handleChangeSpendingDate = this.handleChangeSpendingDate.bind(this);
        this.registerSpendingAmount = this.registerSpendingAmount.bind(this);
    }
    componentWillMount() {
        console.log("SpendingInput(componentWillMount)");
            axios.get("/categorycheck")
            .then(response => {
                const res = response.data;
                const status = response.status;
                console.log("/categorycheck : res " + JSON.stringify(res));
                if(status == 200) {
                     //接続に成功したがリソースの取得に失敗したとき
                    if(res.errorCode) {
                        const errorCode = res.errorCode;
                        
                        Redirect(errorCode)
                    } else {
                        //接続に成功しリソースの取得も完了時
                        console.log("SpendingInput(componentWillMount) OK")
                        this.setState({
                            spendingCategoryData: res,
                            accountId: res[0].accountId,
                        });
                    }
                } else {
                    //id取得 2xx系
                    console.log("2xx :" + status);
                    Redirect(status)
                }
            })
            .catch(error => {
                const err = error.response;
                if(err.status) {
                    
                    const status = err.status;
                    Redirect(status);
                } else {
                    console.log("error:" + error);
                    location.href=`http://localhost:8080/api/error/500`;
                }
            })
        
    }
    registerSpendingAmount() {
        console.log("SpendingInput:registerSpendingAmount()");

        const {spendingDate, spendingAmount, spendingCategoyName} = this.state.param;
        
        let spendingcategoryLength = this.state.spendingCategoryData.length;
        let spendingCategoryNameList = [];
        for(let idx = 0; idx < spendingcategoryLength; idx++) {
            spendingCategoryNameList.push(this.state.spendingCategoryData[idx].spendingCategoryName)
        }

        const RegularAmountNotation = new RegExp(/^[0-9]+$/);
        const RegularDateNotation = new RegExp(/^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$/);

        this.setState({
            errorMessages: {
                spendingDate: '',
                spendingAmount: '',
                spendingCategoyName: '',
            }
        })

        let checkFlag = false;
        //日付未入力
        if(spendingDate === '' || spendingDate === null) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        spendingDate: '日付が未入力です',
                        spendingAmount: state.errorMessages.spendingAmount,
                        spendingCategoyName: state.errorMessages.spendingCategoyName,
                    },
                }
            });
        } else if(!(RegularDateNotation.test(spendingDate))) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        spendingDate: '日付の形式が無効です',
                        spendingAmount: state.errorMessages.spendingAmount,
                        spendingCategoyName: state.errorMessages.spendingCategoyName,
                    },
                }
            });
        }

        //支出金額未入力
        if(spendingAmount === '' || spendingAmount === null) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        spendingDate: state.errorMessages.spendingDate,
                        spendingAmount: '金額が未入力です',
                        spendingCategoyName: state.errorMessages.spendingCategoyName,
                    },
                }
            });
        //支出金額半角英数字以外    
        } else if(!(RegularAmountNotation.test(spendingAmount))) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        spendingDate: state.errorMessages.spendingDate,
                        spendingAmount: '金額は半角数字で入力してください',
                        spendingCategoyName: state.errorMessages.spendingCategoyName,
                    },
                }
            });
        //支出金額範囲外    
        } else if(spendingAmount < 0 || spendingAmount > 10000000) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        spendingDate: state.errorMessages.spendingDate,
                        spendingAmount: '金額は1~10000000円まで入力可能です',
                        spendingCategoyName: state.errorMessages.spendingCategoyName,
                    },
                }
            });
        }
        
        //カテゴリ未入力
        if(spendingCategoyName === '' || spendingCategoyName === null) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        spendingDate: state.errorMessages.spendingDate,
                        spendingAmount: state.errorMessages.spendingAmount,
                        spendingCategoyName: 'カテゴリが未選択です',
                    },
                }
            });
        //カテゴリ範囲外    
        } else if(!(spendingCategoryNameList.includes(spendingCategoyName))) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        spendingDate: state.errorMessages.spendingDate,
                        spendingAmount: state.errorMessages.spendingAmount,
                        spendingCategoyName: 'カテゴリが無効です',
                    },
                }
            });
        }
        //IDがセットされていない場合 TODO
        if(this.state.accountId === 0) {
            checkFlag = true;
            alert('情報取得に失敗しました。再度ページを更新してください')
        }

        //チェックOK
        if(!checkFlag) {
            //ID設定
            const accountId = this.state.accountId;
            //日付分割
            const [spendingYear, spendingMonth, spendingDay] = spendingDate.split('-');
            
            const params = {
                accountId: accountId,
                spendingAmount: Number(spendingAmount),
                spendingCategoryName: spendingCategoyName,
                spendingYear: Number(spendingYear),
                spendingMonth: Number(spendingMonth),
                spendingDay: Number(spendingDay)
            };
            const spendingRegisterRequest = async () => {
                try {
                    const response = await axios.post("/spendingRegisterRequest", params);
                    console.log(response.data);
                    const data = response.data;
                    const status = response.status;
                    if(status == 200) {
                        //200error
                        if(data.message) {
                            const message = data.message;
                            alert(message);
                        } else if(data.errorCode) {
                            const errorCode = data.errorCode;
                            
                            Redirect(errorCode)
                        } else {
                            //succsess
                            const date = data.spendingYear + '年' + data.spendingMonth + '月' + data.spendingDay + '日';
                            const category = data.spendingCategoryName;
                            const amount = data.spendingAmount;
                            const message = '以下の内容で登録しました。\n' + '日付: ' + date + '\n' + 'カテゴリ: ' + category + '\n' + '金額: ' + amount + '円';
                            alert(message);
                        }
                    } else {
                        //id取得 2xx系
                        console.log("2xx :" + status);
                        Redirect(status)
                    }
                } catch (error) { 
                    const err = error.response;
                    if(err.status) {
                        
                        const status = err.status;
                        Redirect(status)
                    } else {
                        console.log("error:" + error);
                        location.href=`http://localhost:8080/api/error/500`;
                    }
                };
            }
            spendingRegisterRequest();
        }
    }

    render() {
        console.log("SpendingInput(render)");
        const errorMessages = this.state.errorMessages;
        const SpendingCategoryButton = (props) => {

            return props.value.map((category) => {
                return (
                    <button 
                      type="button"
                      className="spending-category-button"
                      value={category.spendingCategoryName} 
                      key={category.spendingCategoryName}
                      onClick={props.onClick}
                      style={{backgroundColor: category.spendingCategoryColor}}
                    >
                       {category.spendingCategoryName}
                    </button>
                );
            });
        }
        
        return (
            <div className="spending-input">
                <div>
                    <div className="spending-input-date">
                        <TextField id="date" label="日付" type="date"
                        defaultValue={this.state.param.spendingDate === ''  ? spendingDefaultDate : this.state.param.spendingDate}
                        onChange={this.handleChangeSpendingDate}
                        />
                        <div className="spending-input-error-message">{errorMessages.spendingDate ? errorMessages.spendingDate : '  ' }</div>
                        <TextField
                           id="standard-basic" 
                           label="支出金額(円)" 
                           onChange={this.handleChangeSpendingAmount} 
                        />
                        <div className="spending-input-error-message">{errorMessages.spendingAmount ? errorMessages.spendingAmount : '  ' }</div>
                        <TextField 
                           id="standard-basic"  
                           disabled 
                           label={this.state.param.spendingCategoyName === '' ? 'カテゴリを選んでください' : this.state.param.spendingCategoyName} 
                        />
                        <div className="spending-input-error-message">{errorMessages.spendingCategoyName ? errorMessages.spendingCategoyName : '  ' }</div>
                        <div>
                            <Button variant="contained" color="primary" onClick={this.registerSpendingAmount}>
                             支出を登録
                            </Button>
                        </div>
                    </div>
                    <div className="spending-input-category">
                        <div>カテゴリ</div>
                        <div className="spending-category-button-area">
                            <SpendingCategoryButton
                             value={this.state.spendingCategoryData}
                             onClick={e => this.handleClickSpendingCategory(e)} 
                            />
                        </div>
                        <div style={{paddingLeft:'5px'}}>
                            <Link to={"/spendingedit/" + this.state.accountId}>
                               <Button variant="contained">追加・編集</Button>
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
    componentDidMount(){
        console.log("SpendingInput(componentDidMount)");
        // this.setState({loading: false});
    }
    
    //関数
    handleChangeSpendingDate(event) {
        const date = event.target.value;
        console.log("handleChangeSpendingDate:" + date);
        this.setState((state) => {
            return {
                param:{
                    spendingDate: date,
                    spendingAmount: state.param.spendingAmount,
                    spendingCategoyName: state.param.spendingCategoyName,
                }
            }
        })
    }
    handleClickSpendingCategory(event) {
        const date = event.target.value;
        console.log("handleClickSpendingCategory:" + date);
        this.setState((state) => {
            return {
                param:{
                    spendingDate: state.param.spendingDate,
                    spendingAmount: state.param.spendingAmount,
                    spendingCategoyName: date,
                }
            }
        })
        
    }
    handleChangeSpendingAmount(event) {
        const date = event.target.value;
        console.log("handleChangeSpendingAmount:" + date);
        this.setState((state) => {
            return {
                param:{
                    spendingDate: state.param.spendingDate,
                    spendingAmount: date,
                    spendingCategoyName: state.param.spendingCategoyName,
                }
            }
        })
    }
}
export default withRouter(SpendingInput)