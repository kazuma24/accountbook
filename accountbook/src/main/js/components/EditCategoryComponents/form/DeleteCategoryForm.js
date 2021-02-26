'use strict';

const React = require('react');


export default class DeleteCategoryForm extends React.Component {
    constructor(props) {
        super(props);
        this.state={
            accountId: this.props.accountId,
            deleteSpendingCategoryName: '',
        }
        this.deleteSpendingCategoryNameChangehandle = this.deleteSpendingCategoryNameChangehandle.bind(this);
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
                        <button className="red-button">削除</button>
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