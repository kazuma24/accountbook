'use strict';

const React = require('react');


export default class SpendingCategoryList extends React.Component {
    constructor(props) {
        super(props);
    }
    
    render() {
        const List = this.props.spendingCategoryList;
        const CategoryList = (props) => {
            return props.list.map((category) => {
                return (
                    <div
                     className="registed-category"
                     key={category.spendingCategoryName}
                     style={{background: category.spendingCategoryColor}}
                    >
                     {category.spendingCategoryName}
                    </div>
                );
            });
        }
        
        return (
            <div className="category-list-view">
                <h3 className="content-title">支出カテゴリ一覧</h3>
                <CategoryList list={List} />
            </div>
        )
    }

 
}