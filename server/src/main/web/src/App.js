import React, { Component } from 'react';
import './components/App.css';
import { BrowserRouter as Router, Route, Link } from 'react-router-dom';
import { Home } from './components/home/Home.js';
import { ApiBrowser } from './components/apiBrowser/ApiBrowser.js';
import { Documentation } from './components/documentation/Documentation.js';
import { Statistics } from './components/statistics/Statistics.js';

class App extends Component {

	render() {
		return (
			<div className='App'>
				<Router>
					<div>
						<div className='navbar navbar-default navbar-fixed-top'>
							<div className='container-fluid'>
								<div className='collapse navbar-collapse'>
										<ul className='nav navbar-nav'>
											<li><Link to='/'>Home</Link></li>
											<li><Link to='/api-browser'>API Browser</Link></li>
											<li><Link to='/documentation'>Documentation</Link></li>
											<li><Link to='/statistics'>Statistics</Link></li>
										</ul>
										<ul className='nav navbar-nav navbar-right'>
											<li className='navigation__github-star'>
												<a className='github-button' href='https://github.com/cezarykluczynski/stapi' data-icon='octicon-star'
													data-style='mega' data-show-count='true'
													aria-label='Star cezarykluczynski/stapi on GitHub'>Star</a>
											</li>
											<li><a href='https://github.com/cezarykluczynski/stapi'>STAPI on GitHub</a></li>
										</ul>
								</div>
							</div>
						</div>
						<div className='container content'>
							<Route exact path='/' component={Home}/>
							<Route exact path='/api-browser' component={ApiBrowser}/>
							<Route exact path='/documentation' component={Documentation}/>
							<Route exact path='/statistics' component={Statistics}/>
						</div>
					</div>
				</Router>
			</div>
		);
	}
}

export default App;
