'use strict';

const React = require('react');
import { withRouter } from 'react-router-dom';

class Other extends React.Component {
    
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <div className="other-menu">
                <ul>
                    <li>
                        <form role="form" id="logout" action="/logout" method="post">
	                       <button className="gray-button" type="submit">ログアウト</button>
	                    </form>
                    </li>
                    <li>
                        <a href="/deleteaccount">
                            <button className="gray-button" type="button">アカウント削除</button>
                        </a>    
                    </li>
                    <li>
                        <a href="/inquiry">
                            <button className="gray-button" type="button">お問い合わせ</button>
                        </a>
                    </li>
                </ul>
            </div>
        )
    }
}
export default withRouter(Other)