'use strict';

import axios from 'axios';
import { withRouter, Link　} from 'react-router-dom';
import {Button} from '@material-ui/core';
import Redirect from '../../../Redirect';

const React = require('react');

const date = new Date();
const currentYear = date.getFullYear();
const currentMonth = date.getMonth() + 1;
const years = [currentYear-2,currentYear-1, currentYear, currentYear+1,currentYear+2];
const months = [1,2,3,4,5,6,7,8,9,10,11,12];

function OptionList(props) {
    return <option value={props.value}>{props.value}</option>
}

class MonthryReport extends React.Component {
    constructor(props) {
        super(props);
        console.log("MonthryReport: constructor()");
        this.state ={
            accountId: 0,
            year: currentYear,
            month: currentMonth,
            totalMonthlySpending: 0,
            totalMonthryIncome: 0,
            monthlyBalance: 0
        };
        this.yearChangeHandle = this.yearChangeHandle.bind(this);
        this.monthChangeHandle = this.monthChangeHandle.bind(this);
        this.getMonthryReport = this.getMonthryReport.bind(this);
    }

    //各合計値取得
    getMonthryReport(id = this.state.accountId, year = this.state.year, month = this.state.month) {
        //パラメータ設定
        const params = {
            accountId: id,
            year: year,
            month: month,
        };
        console.log("get MonthryReport!")
        axios.post("/gettotal",params)
        .then(response2 => {
            const res2 = response2.data;
            const status = response2.status;
            if(status == 200) {
                console.log(JSON.stringify(res2));
                //取得したがサーバー内で問題があった場合
                if(res2.message) {
                    const message = res2.message;
                    alert(message);
                    Redirect(500);
                } else if (res2.errorCode) {
                    const errorCode = res2.errorCode;
                    
                    Redirect(errorCode);
                } else {
                    //succsess
                    console.log("MonthryReport OK!")
                    this.setState({
                        accountId: res2.accountId,
                        totalMonthlySpending: res2.totalMonthlySpending,
                        totalMonthryIncome: res2.totalMonthryIncome,
                        monthlyBalance: res2.monthlyBalance,
                    });
                }
            } else {
                //id取得 2xx系
                console.log("2xx :" + status);
                Redirect(status)
            }
        }).catch(error => {
            //Id取得エラー
            const err = error.response;
            if(err.status) {
                
                const status = err.status;
                Redirect(status)
            } else {
                console.log("error:" + error);
                Redirect(500);
            }
        });
    }


    yearChangeHandle(event) {
        const value = event.target.value;
        const year = Number(value);
        const state = this.state;
        let check = false;
        if(!(years.includes(year))) {
            check = true;
            alert(value + "は選択できません");
        }
        if(!check) {
            this.setState({
                year: year
            });
            this.getMonthryReport(state.id,year,state.month);
        }
    }
    
    monthChangeHandle(event) {
        const value = event.target.value;
        const month = Number(value);
        const state = this.state;
        let check = false;
        if(!(months.includes(month))) {
            check = true;
            alert(value + "は選択できません");
        }
        if(!check) {
            this.setState({
                month: month
            });
            this.getMonthryReport(state.id,state.year,month);
        }
    }



    render() {
        console.log("MonthryReport: render()");
        const monthsList = months.map(
            months => <OptionList key={months.toString()} value={months} />
        )
        const yearsList = years.map(
            years => <OptionList key={years.toString()} value={years} />
        )
        return (
            <div className="monthry-report">
                <div className="monthry-report-content">
                   <div className="report-month-select-area">
                        <select defaultValue={currentYear} onChange={this.yearChangeHandle}>
                            {yearsList}
                        </select>
                       <div style={{padding: "0px 10px"}}>年</div>
                       <select defaultValue={currentMonth} onChange={this.monthChangeHandle}>
                            {monthsList}
                       </select>
                       <div style={{padding: "0px 10px"}}>月 (合計)</div>
                   </div>
                   <div className="report-area">
                       <div className="report-amount-area">
                           <div className="report-content">
                               <div>収入</div>
                               <div>{this.state.totalMonthryIncome}円</div>
                           </div>
                           <div className="report-content">
                               <div>支出</div>
                               <div>{this.state.totalMonthlySpending}円</div>
                           </div>
                           <div className="report-content">
                               <div>収支</div>
                               <div>{this.state.monthlyBalance}円</div>
                           </div>
                       </div>
                       <div className="window-move-area">
                            <div>
                               <Link to={`/monthryincomeratio/${this.state.year}/${this.state.month}`} >
                                    <Button variant="contained" color="primary">収入割合</Button>
                               </Link>
                           </div>
                           <div>
                               <Link to={`/monthryspendingratio/${this.state.year}/${this.state.month}`} >
                                    <Button variant="contained" color="secondary">支出割合</Button>
                               </Link>
                           </div>
                       </div>
                   </div>
                </div>
            </div>
        )
    }
    componentDidMount() {
        console.log("MonthryReport: componentDidMount()");
        //ID取得
        axios.get("/getid")
        .then(response1 => {
            const res1 = response1.data;
            //取得200
            const status = response1.status;
            if(status == 200) {
                //ApiError
                if(res1.errorCode) {
                    const errorCode = res1.errorCode;
                    
                    Redirect(errorCode)
                } else {
                    //succsess
                    console.log(JSON.stringify(res1));
                    this.getMonthryReport(res1.id);
                }
            } else {
                //id取得 2xx系
                console.log("2xx :" + status);
                Redirect(status)
            } 
        }).catch(error => {
            //Id取得エラー
            const err = error.response1;
            if(err.status) {
                
                const status = err.status;
                Redirect(status)
            } else {
                console.log("error:" + error);
                Redirect(500);
            }
        })
    }
}
export default withRouter(MonthryReport)