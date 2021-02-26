'use strict';

const React = require('react');
const axios = require('axios');
import { withRouter, Link　} from 'react-router-dom';
import Redirect from '../Redirect';


function GetDateView(props) {
    const year = props.year;

    return (
        <React.Fragment>
            <div>{year}年</div>
            <div>1月1日~12月31日</div>
        </React.Fragment>
    )
}

class YearSpendingRatio extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            year: this.props.match.params.year,
            totalAmount: 0,
            amountData: []
        };
    }
    render() {
        const state = this.state;
        
        return (
            <div className="ratio">
                <div style={{margin: "40px"}}>
                    <div className="date-area">
                        <GetDateView year={state.year} />
                    </div>
                    <div className="ratio-contents">
                        <table>
                            <thead>
                                <tr>
                                    <th>カテゴリ</th>
                                    <th>支出金額</th>
                                </tr>
                            </thead>
                            <tbody>
                                    {state.amountData.length != 0 && state.amountData.map(data => {
                                        return (
                                            <tr className="report-amount" key={data.incomeCategoryName}>
                                                <td>{data.spendingCategoryName}</td>
                                                <td>{data.spendingAmount}円</td>
                                            </tr>
                                        )
                                    })}
                            </tbody>
                        </table>
                    </div>
                </div>
                <div style={{margin: "40px"}}>
                    <div className="total-amount-area">
                        <div>月間合計支出</div>
                        <div>{this.state.totalAmount}円</div>
                    </div>
                    <div>
                        <Link to={"/main"}>
                        　　メイン画面へ戻る
                        </Link>
                    </div>
                </div>
                
            </div>
        )
    }
    componentDidMount(){
        const params = {
            year: this.state.year,
        };
        let amountData = [];

        axios.post("/yearacquisitionofeachamountdata",params)
        .then(response => {
            const res = response.data;
            const status = response.status;
            
            if(status == 200) {
                //接続に成功したがリソースの取得に失敗したとき
                if(res.errorCode) {
                    const errorCode = res.errorCode;
                    
                    Redirect(errorCode)
                } else {
                    //接続に成功しリソースの取得も完了時
                    console.log("amountData " + res);
                    //金額データがない場合カテゴリリスト分の0を生成
                    if(!Object.keys(res).length) {
                       amountData.push({"SpendingCategoryName":"登録されている支出はありません","spendingAmount":"0"})
                       this.setState({
                         amountData: amountData
                       });
                    } else {
                       amountData = res
                       let total = 0;
                       for (const obj of res) {
                           total += Number(obj.spendingAmount)
                       }
                       this.setState({
                        amountData: amountData,
                        totalAmount: total,
                       });
                    }
                    
                    
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
        });
    }
}
export default withRouter(YearSpendingRatio)