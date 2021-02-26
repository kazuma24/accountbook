'use strict';


const React = require('react');
const axios = require('axios');

import { withRouter, Link} from 'react-router-dom';
import { Button, TextField } from '@material-ui/core';
import Redirect from '../../../Redirect';

const date = new Date();
var yyyy = date.getFullYear();
var mm = ("0"+(date.getMonth()+1)).slice(-2);
var dd = ("0"+ date.getDate()).slice(-2);
const incomeDefaultDate = yyyy + '-' + mm + '-' + dd;



class IncomeInput extends React.Component {
    constructor(props) {
        super(props);
        console.log("IncomeInput(constructor)");
        this.state ={
            incomeCategoryData: [],
            accountId: 0,
            param: {
                incomeDate: incomeDefaultDate,
                incomeAmount: '',
                incomeCategoyName: '',
            },
            errorMessages: {
                incomeDate: '',
                incomeAmount: '',
                incomeCategoyName: '',
            },
        };
        this.handleChangeIncomeAmount = this.handleChangeIncomeAmount.bind(this);
        this.handleChangeIncomeDate = this.handleChangeIncomeDate.bind(this);
        this.registerIncomeAmount = this.registerIncomeAmount.bind(this);
    }
    componentWillMount() {
        console.log("IncomeInput(componentWillMount)");
            axios.get("/incomecategorycheck")
            .then(response => {
                const res = response.data;
                const status = response.status;
                console.log("/categorycheck : res " + JSON.stringify(res));
                if(status == 200) {
                     //200Error
                    if(res.errorCode) {
                        const errorCode = res.errorCode;
                        
                        Redirect(errorCode)
                    } else {
                        //succsess
                        console.log("IncomeInput(componentWillMount) OK")
                        this.setState({
                            incomeCategoryData: res,
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
                    Redirect(status)
                } else {
                    console.log("error:" + error);
                    location.href=`http://localhost:8080/api/error/500`;
                }
            })

    }
    registerIncomeAmount() {
        console.log("IncomeInput:registerIncomeAmount()");

        const {incomeDate, incomeAmount, incomeCategoyName} = this.state.param;
        
        let incomecategoryLength = this.state.incomeCategoryData.length;
        let incomeCategoryNameList = [];
        for(let idx = 0; idx < incomecategoryLength; idx++) {
            incomeCategoryNameList.push(this.state.incomeCategoryData[idx].incomeCategoryName)
        }

        const RegularAmountNotation = new RegExp(/^[0-9]+$/);
        const RegularDateNotation = new RegExp(/^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$/);

        this.setState({
            errorMessages: {
                incomeDate: '',
                incomeAmount: '',
                incomeCategoyName: '',
            }
        })

        let checkFlag = false;
        //日付未入力
        if(incomeDate === '' || incomeDate === null) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        incomeDate: '日付が未入力です',
                        incomeAmount: state.errorMessages.incomeAmount,
                        incomeCategoyName: state.errorMessages.incomeCategoyName,
                    },
                }
            });
        } else if(!(RegularDateNotation.test(incomeDate))) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        incomeDate: '日付の形式が無効です',
                        incomeAmount: state.errorMessages.incomeAmount,
                        incomeCategoyName: state.errorMessages.incomeCategoyName,
                    },
                }
            });
        }

        //収入金額未入力
        if(incomeAmount === '' || incomeAmount === null) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        incomeDate: state.errorMessages.incomeDate,
                        incomeAmount: '金額が未入力です',
                        incomeCategoyName: state.errorMessages.incomeCategoyName,
                    },
                }
            });
        //収入金額半角英数字以外    
        } else if(!(RegularAmountNotation.test(incomeAmount))) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        incomeDate: state.errorMessages.incomeDate,
                        incomeAmount: '金額は半角数字で入力してください',
                        incomeCategoyName: state.errorMessages.incomeCategoyName,
                    },
                }
            });
        //収入金額範囲外    
        } else if(incomeAmount < 0 || incomeAmount > 10000000) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        incomeDate: state.errorMessages.incomeDate,
                        incomeAmount: '金額は1~10000000円まで入力可能です',
                        incomeCategoyName: state.errorMessages.incomeCategoyName,
                    },
                }
            });
        }
        
        //カテゴリ未入力
        if(incomeCategoyName === '' || incomeCategoyName === null) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        incomeDate: state.errorMessages.incomeDate,
                        incomeAmount: state.errorMessages.incomeAmount,
                        incomeCategoyName: 'カテゴリが未選択です',
                    },
                }
            });
        //カテゴリ範囲外    
        } else if(!(incomeCategoryNameList.includes(incomeCategoyName))) {
            checkFlag = true;
            this.setState((state) => {
                return {
                    errorMessages: {
                        incomeDate: state.errorMessages.incomeDate,
                        incomeAmount: state.errorMessages.incomeAmount,
                        incomeCategoyName: 'カテゴリが無効です',
                    },
                }
            });
        }
        //IDがセットされていない場合 TODO
        if(this.state.accountId === 0) {
            checkFlag = true;
        }

        //チェックOK
        if(!checkFlag) {
            //ID設定
            const accountId = this.state.accountId;
            //日付分割
            const [incomeYear, incomeMonth, incomeDay] = incomeDate.split('-');
            
            const params = {
                accountId: accountId,
                incomeAmount: Number(incomeAmount),
                incomeCategoryName: incomeCategoyName,
                incomeYear: Number(incomeYear),
                incomeMonth: Number(incomeMonth),
                incomeDay: Number(incomeDay)
            };
            const incomeRegisterRequest = async () => {
                try {
                    const response = await axios.post("/incomeRegisterRequest", params);
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
                            const date = data.incomeYear + '年' + data.incomeMonth + '月' + data.incomeDay + '日';
                            const category = data.incomeCategoryName;
                            const amount = data.incomeAmount;
                            const message = '以下の内容で登録しました。\n' + '日付: ' + date + '\n' + 'カテゴリ: ' + category + '\n' + '金額: ' + amount + '円';
                            alert(message);
                        }
                    } else {
                        //2xx系
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
            incomeRegisterRequest();
        }
    }

    render() {
        console.log("IncomeInput(render)");
        const errorMessages = this.state.errorMessages;
        const IncomeCategoryButton = (props) => {

            return props.value.map((category) => {
                return (
                    <button 
                      type="button"
                      className="income-category-button"
                      value={category.incomeCategoryName} 
                      key={category.incomeCategoryName}
                      onClick={props.onClick}
                      style={{backgroundColor: category.incomeCategoryColor}}
                    >
                       {category.incomeCategoryName}
                    </button>
                );
            });
        }
        
        return (
            <div className="income-input">
                <div>
                    <div className="income-input-date">
                        <TextField id="date" label="日付" type="date" 
                        defaultValue={this.state.param.incomeDate === '' ? incomeDefaultDate : this.state.param.incomeDate}
                        onChange={this.handleChangeIncomeDate}
                        />
                        <div className="income-input-error-message">{errorMessages.incomeDate ? errorMessages.incomeDate : '  ' }</div>
                        <TextField 
                           id="standard-basic" 
                           label="収入金額(円)" 
                           onChange={this.handleChangeIncomeAmount}
                        />
                        <div className="income-input-error-message">{errorMessages.incomeAmount ? errorMessages.incomeAmount : '  ' }</div>
                        <TextField 
                           id="standard-basic"  
                           disabled 
                           label={this.state.param.incomeCategoyName === '' ? 'カテゴリを選んでください' : this.state.param.incomeCategoyName}
                        />
                        <div className="income-input-error-message">{errorMessages.incomeCategoyName ? errorMessages.incomeCategoyName : '  ' }</div>
                        <div>
                            <Button variant="contained" color="primary" onClick={this.registerIncomeAmount}>
                              収入を登録
                            </Button>
                        </div>
                    </div>
                    <div className="income-input-category">
                        <div>カテゴリ</div>
                        <div className="income-category-button-area">
                            <IncomeCategoryButton
                             value={this.state.incomeCategoryData}
                             onClick={e => this.handleClickIncomeCategory(e)} 
                            />
                            
                        </div>
                        <div style={{paddingLeft:'5px'}}>
                            <Link to={"/incomeedit/" + this.state.accountId}>
                                <Button variant="contained">追加・編集</Button>
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
    
    //関数
    handleChangeIncomeDate(event) {
        const date = event.target.value;
        console.log("handleChangeIncomeDate:" + date);
        this.setState((state) => {
            return {
                param:{
                    incomeDate: date,
                    incomeAmount: state.param.incomeAmount,
                    incomeCategoyName: state.param.incomeCategoyName,
                }
            }
        })
    }
    handleClickIncomeCategory(event) {
        const date = event.target.value;
        console.log("handleClickIncomeCategory:" + date);
        this.setState((state) => {
            return {
                param:{
                    incomeDate: state.param.incomeDate,
                    incomeAmount: state.param.incomeAmount,
                    incomeCategoyName: date,
                }
            }
        })
        
    }
    handleChangeIncomeAmount(event) {
        const date = event.target.value;
        console.log("handleChangeIncomeAmount:" + date);
        this.setState((state) => {
            return {
                param:{
                    incomeDate: state.param.incomeDate,
                    incomeAmount: date,
                    incomeCategoyName: state.param.incomeCategoyName,
                }
            }
        })
    }
}
export default withRouter(IncomeInput)