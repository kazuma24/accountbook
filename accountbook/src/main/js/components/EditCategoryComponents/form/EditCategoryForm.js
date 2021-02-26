'use strict';

const React = require('react');


export default class EditCategoryForm extends React.Component {
    constructor(props) {
        console.log("EditCategoryForm: constructor()")
        super(props);
        this.state={
            accountId: this.props.accountId,
            changeBeforeSpendingCategoryColor: '#FFFFFF',
            changeBeforeSpendingCategoryName: '',
            AfterChangeSpendingCategoryName:'',
            AfterChangeSpendingCategoryColor:'#FFFFFF',
        }

        this.changeBeforeSpendingCategoryNameChangehandle = this.changeBeforeSpendingCategoryNameChangehandle.bind(this);
        this.spendingCategoryNameChangehandle = this.spendingCategoryNameChangehandle.bind(this);
        this.spendingCategoryColorChangehandle = this.spendingCategoryColorChangehandle.bind(this);
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
                        <option key="default" value=" -#FFFFFF">未選択</option> 
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
                        />
                        <input　
                        type="color"
                        value={state.AfterChangeSpendingCategoryColor}
                        onChange={this.spendingCategoryColorChangehandle}
                    　　/>
                        <div className="edit-button-area">
                            <button
                             className="prengreen-button">変更</button>
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