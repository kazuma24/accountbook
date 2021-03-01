'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

// コンポーネント
import MainView from './components/MainView';
import TitleView from './components/TitleView';

class App extends React.Component {

	constructor(props) {
		super(props);
	}

	render() { 
		return (
			<React.Fragment>
			  <TitleView />
			  <MainView />
			</React.Fragment>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)
