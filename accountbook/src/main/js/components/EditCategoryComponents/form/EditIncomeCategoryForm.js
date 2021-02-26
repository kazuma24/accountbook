'use strict';

const React = require('react');
import axios from 'axios';
import { withRouter } from 'react-router-dom';
import Redirect from '../../Redirect';



class EditIncomeCategoryForm extends React.Component {
    constructor(props) {
        console.log("EditCategoryForm: constructor()")
        super(props);
        this.state={
            accountId: this.props.accountId,
            changeBeforeIncomeCategoryColor: '#FFFFFF',
            changeBeforeIncomeCategoryName: 'default',
            AfterChangeIncomeCategoryName:'',
            AfterChangeIncomeCategoryColor:'#FFFFFF',
        }

        this.changeBeforeIncomeCategoryNameChangehandle = this.changeBeforeIncomeCategoryNameChangehandle.bind(this);
        this.incomeCategoryNameChangehandle = this.incomeCategoryNameChangehandle.bind(this);
        this.incomeCategoryColorChangehandle = this.incomeCategoryColorChangehandle.bind(this);
        this.changeCategoryName = this.changeCategoryName.bind(this);
        this.chengeIncomeList = this.chengeIncomeList.bind(this);
    }

    chengeIncomeList(list) {
        return this.props.changeIncomeList(list)
    }

    changeCategoryName() {
        const accountId = this.state.accountId;
        const beforeName = this.state.changeBeforeIncomeCategoryName;
        const afterName = this.state.AfterChangeIncomeCategoryName;
        const afterColor = this.state.AfterChangeIncomeCategoryColor;
        const count = this.props.incomeCategoryList.length;
        let registedList = [];
        for(let idx = 0; idx < count; idx++) {
            registedList.push(this.props.incomeCategoryList[idx].incomeCategoryName)
        }

        var reg = new RegExp(/[!"#$%&'()\*\+\-\.,\/:;<=>?@\[\\\]^_`{|}~]/g);

        let checkFlag = false;
        if(accountId === null || accountId === '') {
            checkFlag = true;
            alert('IDの情報がありません');
        }

        if(beforeName === null || beforeName === '' || beforeName === 'default') {
            checkFlag = true;
            alert('変更前のカテゴリが未選択です');
        }

        if(afterName === null || afterName === '') {
            checkFlag = true;
            alert('変更後のカテゴリ名を入力してください');
        }

        if(afterColor === null || afterColor === '') {
            checkFlag = true;
            alert('変更後のカラーを選択してください');
        }
        if(reg.test(beforeName) || reg.test(afterName)) {
            checkFlag = true;
            alert('名前に記号は使えません');
        }

        if(registedList.includes(afterName)) {
            checkFlag = true;
            alert(`変更後のカテゴリ名(${afterName})は既に存在してます`);
        }

        if(!checkFlag) {
            const params = {
                accountId: Number(accountId),
                beforeName: beforeName,
                afterName: afterName,
                afterColor: afterColor,
            };
            const url = "http://localhost:8080/changecategoryincome";
            axios.post(url, params)
            .then(response => {
                const res = response.data;
                if("message" in res) {
                    alert(res.message)
                } else {
                    console.log(JSON.stringify(res));
                    const message = 'カテゴリ名を変更しました。\n' + 'カテゴリ名(変更前): ' + beforeName + '\n' + '変更後' + afterName;
                    alert(message);
                    //親コンポーネントの値を戻す
                    this.chengeIncomeList(res);
                    //変更したデータがステートに残るため初期値をセット
                    this.setState({
                        changeBeforeIncomeCategoryColor: '#FFFFFF',
                        changeBeforeIncomeCategoryName: 'default',
                        AfterChangeIncomeCategoryName:'',
                        AfterChangeIncomeCategoryColor:'#FFFFFF',
                    })
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
        console.log("EditCategoryForm: render()")
        const List = this.props.incomeCategoryList;
        
        const CategoryList = (props) => {
            return props.list.map((category) => {
                return (
                    <option
                     key={category.incomeCategoryName}
                     value={category.incomeCategoryName + '-' + category.incomeCategoryColor}
                    >
                     {category.incomeCategoryName}
                    </option>
                );
            });
        }
        const state = this.state;
        return (
            <div className="form-content">
                <div style={{display:"flex"}}>
                    <div className="befor-area">
                        <div>変更前</div>
                        <select
                         className="befor-select"
                         onChange={this.changeBeforeIncomeCategoryNameChangehandle}
                         value={state.changeBeforeIncomeCategoryName + '-' + state.changeBeforeIncomeCategoryColor}>変更対象
                        <option key="default" value="default-#FFFFFF">未選択</option> 
                        <CategoryList list={List} />
                        </select>
                        <input 
                        type="color"
                        value={state.changeBeforeIncomeCategoryColor} readOnly
                        />
                    </div>
                    <div className="after-area">
                        <div>変更後</div>
                        <input 
                        type="text"
                        className="after-category-name"
                        onChange={this.incomeCategoryNameChangehandle}
                        value={state.AfterChangeIncomeCategoryName}
                        />
                        <input　
                        type="color"
                        value={state.AfterChangeIncomeCategoryColor}
                        onChange={this.incomeCategoryColorChangehandle}
                    　　/>
                        <div className="edit-button-area">
                            <button
                             className="prengreen-button"
                             onClick={this.changeCategoryName}>変更</button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }

    //関数
    changeBeforeIncomeCategoryNameChangehandle(event){
        const value = event.target.value;
        const [name, color] = value.split('-');
        this.setState({
            changeBeforeIncomeCategoryColor: color,
            changeBeforeIncomeCategoryName: name,
            AfterChangeIncomeCategoryColor: color,
            }
        );
    }
    incomeCategoryNameChangehandle(event) {
        const value = event.target.value;
        this.setState({        
            AfterChangeIncomeCategoryName: value,
        });
    }
    incomeCategoryColorChangehandle(event) {
        const value = event.target.value;
        this.setState({
             AfterChangeIncomeCategoryColor: value,   
        });
    }
}
export default withRouter(EditIncomeCategoryForm)
