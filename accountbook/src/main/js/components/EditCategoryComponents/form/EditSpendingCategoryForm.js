'use strict';

const React = require('react');
import axios from 'axios';
import { withRouter } from 'react-router-dom';
import Redirect from '../../Redirect';

class EditSpendingCategoryForm extends React.Component {
    constructor(props) {
        console.log("EditCategoryForm: constructor()")
        super(props);
        this.state={
            accountId: this.props.accountId,
            changeBeforeSpendingCategoryColor: '#FFFFFF',
            changeBeforeSpendingCategoryName: 'default',
            AfterChangeSpendingCategoryName:'',
            AfterChangeSpendingCategoryColor:'#FFFFFF',
        }

        this.changeBeforeSpendingCategoryNameChangehandle = this.changeBeforeSpendingCategoryNameChangehandle.bind(this);
        this.spendingCategoryNameChangehandle = this.spendingCategoryNameChangehandle.bind(this);
        this.spendingCategoryColorChangehandle = this.spendingCategoryColorChangehandle.bind(this);
        this.changeCategoryName = this.changeCategoryName.bind(this);
        this.chengeSpendingList = this.chengeSpendingList.bind(this);
    }

    chengeSpendingList(list) {
        return this.props.changeSpendingList(list)
    }

    changeCategoryName() {
        const accountId = this.state.accountId;
        const beforeName = this.state.changeBeforeSpendingCategoryName;
        const afterName = this.state.AfterChangeSpendingCategoryName;
        const afterColor = this.state.AfterChangeSpendingCategoryColor;
        const count = this.props.spendingCategoryList.length;
        let registedList = [];
        for(let idx = 0; idx < count; idx++) {
            registedList.push(this.props.spendingCategoryList[idx].spendingCategoryName)
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
            const url = "http://localhost:8080/changecategory";
            axios.post(url, params)
            .then(response => {
                const res = response.data;
                if("message" in res) {
                    alert(res.message)
                }else {
                    console.log(JSON.stringify(res));
                    const message = 'カテゴリ名を変更しました。\n' + 'カテゴリ名(変更前): ' + beforeName + '\n' + '変更後' + afterName;
                    alert(message);
                    //親コンポーネントの値を戻す
                    this.chengeSpendingList(res);
                    //変更したデータがステートに残るため初期値をセット
                    this.setState({
                        changeBeforeSpendingCategoryColor:"#FFFFFF",
                        changeBeforeSpendingCategoryName: "default",
                        AfterChangeSpendingCategoryName: '',
                        AfterChangeSpendingCategoryColor:'#FFFFFF',
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
        const List = this.props.spendingCategoryList;
        
        const CategoryList = (props) => {
            return props.list.map((category) => {
                return (
                    <option
                     key={category.spendingCategoryName}
                     value={category.spendingCategoryName + '-' + category.spendingCategoryColor}
                    >
                     {category.spendingCategoryName}
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
                         onChange={this.changeBeforeSpendingCategoryNameChangehandle}
                         value={state.changeBeforeSpendingCategoryName + '-' + state.changeBeforeSpendingCategoryColor}>変更対象
                        <option key="default" value="default-#FFFFFF">未選択</option> 
                        <CategoryList list={List} />
                        </select>
                        <input 
                        type="color"
                        value={state.changeBeforeSpendingCategoryColor} readOnly
                        />
                    </div>
                    <div className="after-area">
                        <div>変更後</div>
                        <input 
                        type="text"
                        className="after-category-name"
                        onChange={this.spendingCategoryNameChangehandle} 
                        value={state.AfterChangeSpendingCategoryName}
                        />
                        <input　
                        type="color"
                        value={state.AfterChangeSpendingCategoryColor}
                        onChange={this.spendingCategoryColorChangehandle}
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
    changeBeforeSpendingCategoryNameChangehandle(event){
        const value = event.target.value;
        const [name, color] = value.split('-');
        this.setState({
            changeBeforeSpendingCategoryColor: color,
            changeBeforeSpendingCategoryName: name,
            AfterChangeSpendingCategoryColor: color,
            }
        );
    }
    spendingCategoryNameChangehandle(event) {
        const value = event.target.value;
        this.setState({        
            AfterChangeSpendingCategoryName: value,
        });
    }
    spendingCategoryColorChangehandle(event) {
        const value = event.target.value;
        this.setState({
             AfterChangeSpendingCategoryColor: value,   
        });
    }
}
export default withRouter(EditSpendingCategoryForm)
