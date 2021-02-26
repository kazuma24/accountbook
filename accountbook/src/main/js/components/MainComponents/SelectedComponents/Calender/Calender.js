'use strict';

const React = require('react');
const axios = require('axios');
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import Redirect from '../../../Redirect';

const date = new Date();
const currentYear = date.getFullYear();
const currentMonth = date.getMonth() + 1;

export default class Calender extends React.Component {
    constructor(props) {
        console.log("calendar.js: constructor()")
        super(props);
        this.state = {
            spendingForTheDayData: [],
            incomeForTheDayData: [],
            year:currentYear,
            month:currentMonth,

        };
        this.getTileContent = this.getTileContent.bind(this);
        this.dateChange = this.dateChange.bind(this);
        this.getDate = this.getDate.bind(this);
    }
    // カレンダーのタイルに表示する内容を取得する関数定義
    getTileContent (calendar) {
        console.log("calendar.js: getTitleContent()")
        // 日付フォーマット処理(YYYYMMDD)
        let year = calendar.date.getFullYear();
        let month = calendar.date.getMonth() + 1;
        let day = calendar.date.getDate();
        month = ('0' + month).slice(-2);
        day = ('0' + day).slice(-2);
        let formatDate = year + month + day;

        // タイルに表示する内容の初期化
        let spending = "";
        let income = "";

        // 取得したjsonデータを読み込みカレンダーの日付と一致する場合にタイル内容設定(支出)
        this.state.spendingForTheDayData.forEach(element => {
        let trimedSpendingDate = element.startDate.replace(/\s+/g, "");
        if(formatDate === trimedSpendingDate) {
            spending = element.total;
        }
        });

        // 取得したjsonデータを読み込みカレンダーの日付と一致する場合にタイル内容設定(収入)
        this.state.incomeForTheDayData.forEach(element => {
        let trimedIncomeDate = element.startDate.replace(/\s+/g, "");
        if(formatDate === trimedIncomeDate) {
            income = element.total;
        }
        });

        return (
            <React.Fragment>
                <p style={{color:"blue"}}>{income == "" ? "0" : "+" + income + "円"}</p>
                <p style={{color:"red"}}>{spending == "" ? "0" : "-" + spending + "円"}</p>
            </React.Fragment>
        );
    }

    //カレンダー変更時データ取得関数
    getDate(year = this.state.year, month = this.state.month) {
        console.log("calendar.js getDate()")
        const spendingData = new Promise((resolve, reject) => {
            const params = {
                year: year,
                month: month
            };
            axios.post('/spendingcalendardata',params)
            .then(res => resolve(res))
            .catch(err => reject(err))
        });
        const incomeData = new Promise((resolve, reject) => {
            const params = {
                year: year,
                month: month
            };
            axios.post('/incomecalendardata',params)
            .then((res) => resolve(res))
            .catch((err) => reject(err))
        });
        Promise.all([spendingData, incomeData])
        .then(comp => {
            console.log("res: comp[0]  " + JSON.stringify(comp[0]));
            console.log("res: comp[1]  " + JSON.stringify(comp[1]));
            const spendingDataStatus = comp[0].status;
            const incomeDataStatus = comp[1].status;
            if(spendingDataStatus == 200 && incomeDataStatus == 200) {
                const spendingData = comp[0].data;
                const incomeData = comp[1].data;
                //200だがリソース取得失敗
                if(spendingData.errorCode || incomeData.errorCode) {
                    let errorCode = '';
                    if(spendingData.errorCode) {
                        errorCode = spendingData.errorCode
                    } else {
                        errorCode = incomeData.errorCode
                    }
                    
                    Redirect(errorCode)
                } else {
                    console.log("setState OK");
                    this.setState({
                        spendingForTheDayData: spendingData,
                        incomeForTheDayData: incomeData,
                        year: year,
                        month: month
                    });
                }
            } else {
                //2xx
                let code = '';
                if(spendingDataStatus != 200) {
                   code = spendingDataStatus
                } else {
                   code = incomeDataStatus
                }
                location.href=`http://localhost:8080/api/error/${code}`;
            }
        }).catch(err => {
            console.log("res: err" + err);
            //TODO
        })
    }

    //カレンダーの日付が変わった場合
    dateChange(event) {
        console.log("event:" + JSON.stringify(event.activeStartDate));
        const year = event.activeStartDate.getFullYear();
        const month = event.activeStartDate.getMonth() + 1;
        console.log("year " + year + " month " + month);
        //データ取得
        this.getDate(year,month);
    }

    render() {
        console.log("calendar.js: render()");
        const state = this.state;
        return (
            <React.Fragment>
            {/* 支出データあり・収入データあり・現月」 */}
            {/* 支出データあり・収入データあり・現在月以外」 */}

            {/* 支出データあり・収入データなし・現在月」 */}
            {/* 支出データあり・収入データなし・現在月以外」 */}

            {/* 支出データなし・収入データあり・現在月」 */}
            {/* 支出データなし・収入データあり・現在月以外」 */}
            
            {/* 支出データなし・収入データなし・現在月」 */}
            {/* 支出データなし・収入データなし・現在月以外」 */}
            
            {
                
                (state.spendingForTheDayData.length != 0 && state.incomeForTheDayData != 0 && state.month == currentMonth) && 
                <Calendar 
                value={new Date(state.year, state.month, 0)} 
                tileContent={this.getTileContent}
                onActiveStartDateChange={this.dateChange}
                />
            }
            {
                
                (state.spendingForTheDayData.length != 0 && state.incomeForTheDayData != 0 && state.month != currentMonth) && 
                <Calendar 
                value={new Date(state.year, state.month, 0)} 
                tileContent={this.getTileContent}
                onActiveStartDateChange={this.dateChange}
                />
            }
            {
                (state.spendingForTheDayData.length != 0 && state.incomeForTheDayData == 0 && state.month == currentMonth) && 
                <Calendar 
                value={new Date(state.year, state.month, 0)} 
                tileContent={this.getTileContent}
                onActiveStartDateChange={this.dateChange}
                />
            }
            {
                (state.spendingForTheDayData.length != 0 && state.incomeForTheDayData == 0 && state.month != currentMonth) && 
                <Calendar 
                value={new Date(state.year, state.month, 0)} 
                tileContent={this.getTileContent}
                onActiveStartDateChange={this.dateChange}
                />
            }
            {
                (state.spendingForTheDayData.length == 0 && state.incomeForTheDayData != 0 && state.month == currentMonth) && 
                <Calendar 
                value={new Date(state.year, state.month, 0)} 
                tileContent={this.getTileContent}
                onActiveStartDateChange={this.dateChange}
                />
            }
            {
                (state.spendingForTheDayData.length == 0 && state.incomeForTheDayData != 0 && state.month != currentMonth) && 
                <Calendar 
                value={new Date(state.year, state.month, 0)} 
                tileContent={this.getTileContent}
                onActiveStartDateChange={this.dateChange}
                />
            }
            {
                (state.spendingForTheDayData.length == 0 && state.incomeForTheDayData == 0 && state.month == currentMonth) && 
                <Calendar 
                value={new Date(state.year, state.month, 0)} 
                tileContent={this.getTileContent}
                onActiveStartDateChange={this.dateChange}
                />
            }
            {
                (state.spendingForTheDayData.length == 0 && state.incomeForTheDayData == 0 && state.month != currentMonth) && 
                <Calendar 
                value={new Date(state.year, state.month, 0)} 
                tileContent={this.getTileContent}
                onActiveStartDateChange={this.dateChange}
                />
            }
            
            </React.Fragment>
        )
    }
    componentWillMount() {
        console.log("calendar.js componentWillMount()");
        //データ取得
        this.getDate();
    }
}