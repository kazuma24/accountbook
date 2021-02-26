'use strict';

const React = require('react');
import axios from 'axios';
import { withRouter　} from 'react-router-dom';
import Redirect from '../../Redirect';


class DeleteIncomeCategoryForm extends React.Component {
    constructor(props) {
        super(props);
        this.state={
            accountId: this.props.accountId,
            deleteIncomeCategoryName: '',
        }
        this.deleteIncomeCategoryNameChangehandle = this.deleteIncomeCategoryNameChangehandle.bind(this);
        this.chengeIncomeList = this.chengeIncomeList.bind(this);
        this.deleteCategory = this.deleteCategory.bind(this);
    }
    chengeIncomeList(list) {
        return this.props.changeIncomeList(list)
    }
    deleteCategory(){
        const accountId = this.state.accountId;
        const deleteCategory = this.state.deleteIncomeCategoryName;

        let checkFlag = false;
        
        if(deleteCategory === null || deleteCategory === '') {
            checkFlag = true;
            alert('削除するカテゴリを選択してください')
        } else {
            let result = window.confirm(`${deleteCategory}に関わるデータも削除されますがよろしいですか？`);
            if(!result) {
                checkFlag = true;
            }
        }

        if(!checkFlag) {
            const params = {
                accountId: Number(accountId),
                deleteCategory: deleteCategory,
            };
            const url = "http://localhost:8080/deletecategoryincome";
            axios.post(url, params)
            .then(response => {
                const res = response.data;
                if("message" in res) {
                    alert(res.message)
                } else {
                    console.log(JSON.stringify(res));
                    const message = `${deleteCategory}を削除しました`;
                    alert(message);
                    this.chengeIncomeList(res);
                    this.setState({
                        deleteIncomeCategoryName:""
                    });
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
        const List = this.props.incomeCategoryList;
        const CategoryList = (props) => {
            return props.list.map((category) => {
                return (
                    <option
                     key={category.incomeCategoryName}
                     value={category.incomeCategoryName}
                    >
                     {category.incomeCategoryName}
                    </option>
                );
            });
        }
        return (
            <div className="form-content">
               <div style={{display:"flex"}}>
                    <div className="befor-area">
                            <select
                            className="befor-select"
                            onChange={this.deleteIncomeCategoryNameChangehandle}
                            value={this.state.deleteIncomeCategoryName}>
                            <option key="default" value="">未選択</option> 
                            <CategoryList list={List} />
                            </select>
                        </div>
                    <div className="delete-after-area">
                        <button className="red-button" onClick={this.deleteCategory}>削除</button>
                    </div>
               </div>
            </div>
        )
    }

    //関数
    deleteIncomeCategoryNameChangehandle(event){
        const value = event.target.value;
        this.setState({
            deleteIncomeCategoryName: value,
           }
        );
    }
 
}
export default withRouter(DeleteIncomeCategoryForm)