'use strict';

import axios from 'axios';
import { withRouter, Link　} from 'react-router-dom';
import {Button} from '@material-ui/core';
import Redirect from '../../../Redirect';

const React = require('react');

const date = new Date();
const currentYear = date.getFullYear();
const years = [currentYear-2,currentYear-1, currentYear, currentYear+1,currentYear+2];

function OptionList(props) {
    return <option value={props.value}>{props.value}</option>
}

class YearReport extends React.Component {
    constructor(props) {
        super(props);
        console.log("YearReport: constructor()");
        this.state ={
            accountId: 0,
            year: currentYear,
            totalYearSpending: 0,
            totalYearIncome: 0,
            yearBalance: 0
        };
        this.yearChangeHandle = this.yearChangeHandle.bind(this);
        this.getYearReport = this.getYearReport.bind(this);
    }

    //各合計値取得
    getYearReport(id = this.state.accountId, year = this.state.year) {
        //パラメータ設定
        const params = {
            accountId: id,
            year: year,
        };
        console.log("get YearReport!")
        axios.post("/yeargettotal",params)
        .then(response2 => {
            const res2 = response2.data;
            const status = response2.status;
            if(status == 200) {
                console.log(JSON.stringify(res2));
                //取得したがサーバー内で問題があった場合
                if(res2.message) {
                    const message = res2.message;
                    alert(message);
                    location.href=`http://localhost:8080/api/error/500`;
                } else if (res2.errorCode) {
                    const errorCode = res2.errorCode;
                    
                    Redirect(errorCode)
                } else {
                    //succsess
                    console.log("YearReport OK!")
                    this.setState({
                        accountId: res2.accountId,
                        totalYearSpending: res2.totalYearSpending,
                        totalYearIncome: res2.totalYearIncome,
                        yearBalance: res2.yearBalance,
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
                location.href=`http://localhost:8080/api/error/500`;
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
            this.getYearReport(state.id,year);
        }
    }



    render() {
        console.log("YearReport: render()");
        const yearsList = years.map(
            years => <OptionList key={years.toString()} value={years} />
        )
        return (
            <div className="year-report">
                <div className="year-report-content">
                   <div className="report-month-select-area">
                        <select defaultValue={currentYear} onChange={this.yearChangeHandle}>
                            {yearsList}
                        </select>
                       <div style={{padding: "0px 10px"}}>年（合計）</div>
                   </div>
                   <div className="report-area">
                       <div className="report-amount-area">
                           <div className="report-content">
                               <div>収入</div>
                               <div>{this.state.totalYearIncome}円</div>
                           </div>
                           <div className="report-content">
                               <div>支出</div>
                               <div>{this.state.totalYearSpending}円</div>
                           </div>
                           <div className="report-content">
                               <div>収支</div>
                               <div>{this.state.yearBalance}円</div>
                           </div>
                       </div>
                       <div className="window-move-area">
                            <div>
                               <Link to={`/yearincomeratio/${this.state.year}`} >
                                    <Button variant="contained" color="primary">収入割合</Button>
                               </Link>
                            </div>
                           <div>
                               <Link to={`/yearspendingratio/${this.state.year}`} >
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
        console.log("YearReport: componentDidMount()");
        console.log("get id!")
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
                    console.log("getid OK!")
                    this.getYearReport(res1.id);
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
                location.href=`http://localhost:8080/api/error/500`;
            }
        })
    }
}
export default withRouter(YearReport)