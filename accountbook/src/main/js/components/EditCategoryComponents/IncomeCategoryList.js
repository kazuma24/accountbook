'use strict';

const React = require('react');


export default class IncomeCategoryList extends React.Component {
    constructor(props) {
        super(props);
    }
    
    render() {
        const List = this.props.incomeCategoryList;
        const CategoryList = (props) => {
            return props.list.map((category) => {
                return (
                    <div
                     className="registed-category"
                     key={category.incomeCategoryName}
                     style={{background: category.incomeCategoryColor}}
                    >
                     {category.incomeCategoryName}
                    </div>
                );
            });
        }
        
        return (
            <div className="category-list-view">
                <h3 className="content-title">収入カテゴリ一覧</h3>
                <CategoryList list={List} />
            </div>
        )
    }

 
}