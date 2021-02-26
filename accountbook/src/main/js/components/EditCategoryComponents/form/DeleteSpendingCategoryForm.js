'use strict';

const React = require('react');
import axios from 'axios';
import { withRouter　} from 'react-router-dom';
import Redirect from '../../Redirect';


class DeleteSpendingCategoryForm extends React.Component {
    constructor(props) {
        super(props);
        this.state={
            accountId: this.props.accountId,
            deleteSpendingCategoryName: '',
        }
        this.deleteSpendingCategoryNameChangehandle = this.deleteSpendingCategoryNameChangehandle.bind(this);
        this.chengeSpendingList = this.chengeSpendingList.bind(this);
        this.deleteCategory = this.deleteCategory.bind(this);
    }
    chengeSpendingList(list) {
        return this.props.changeSpendingList(list)
    }
    deleteCategory(){
        const accountId = this.state.accountId;
        const deleteCategory = this.state.deleteSpendingCategoryName;

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
            const url = "http://localhost:8080/deletecategory";
            axios.post(url, params)
            .then(response => {
                const res = response.data;
                if("message" in res) {
                    alert(res.message)
                }else {
                    console.log(JSON.stringify(res));
                    const message = `${deleteCategory}を削除しました`;
                    alert(message);
                    this.chengeSpendingList(res);
                    this.setState({
                        deleteSpendingCategoryName: ""
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
        const List = this.props.spendingCategoryList;
        const CategoryList = (props) => {
            return props.list.map((category) => {
                return (
                    <option
                     key={category.spendingCategoryName}
                     value={category.spendingCategoryName}
                    >
                     {category.spendingCategoryName}
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
                            onChange={this.deleteSpendingCategoryNameChangehandle}
                            value={this.state.deleteSpendingCategoryName}>
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
    deleteSpendingCategoryNameChangehandle(event){
        const value = event.target.value;
        this.setState({
            deleteSpendingCategoryName: value,
           }
        );
    }
 
}
export default withRouter(DeleteSpendingCategoryForm)