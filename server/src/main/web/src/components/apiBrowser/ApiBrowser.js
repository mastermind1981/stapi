import React, { Component } from 'react';
import './ApiBrowser.css';
import { SearchStateService } from '../../service/SearchStateService.js';
import { RestApi } from '../../service/rest/RestApi.js';
import { ApiBrowserResponse } from './ApiBrowserResponse.js';

export class ApiBrowser extends Component {

	constructor() {
		super();
		this.state = {};
		var self = this;
		this.restApi = RestApi.getInstance();
		this.restApi.whenReady(() => {
			const urls = self.restApi.getUrls();
			self.setState({
				urls: urls,
				symbol: urls[0].symbol
			});
			SearchStateService.markValid();
			self.forceUpdate();
		});
		this.restApi.onError(error => {
			SearchStateService.markInvalid(error);
		});
		this.search = this.search.bind(this);
		this.changeSelection = this.changeSelection.bind(this);
		this.updatePhrase = this.updatePhrase.bind(this);
	}

	render() {
		return (
			<div className='api-browser'>
				<div className="row">
					<form role="search" className="form-horizontal" onSubmit={this.search}>
						<div className="col-md-5">
							<input type="text" className="form-control" placeholder="Search the API"
								onChange={this.updatePhrase} disabled={this.isDisabled()} />
						</div>
						<div className="col-md-5">
							<select className="form-control" onChange={this.changeSelection} disabled={this.isDisabled()}>
								{this.createOptions()}
							</select>
						</div>
						<div className="col-md-2">
							<button type="submit" className="btn btn-default" disabled={this.isDisabled()}>Submit</button>
						</div>
					</form>
				</div>
				<div className="row">
					<ApiBrowserResponse />
				</div>
			</div>
		);
	}

	createOptions() {
		let items = [];
		if (!this.state.urls) {
			return items;
		}

		for (let i = 0; i < this.state.urls.length; i++) {
			let url = this.state.urls[i];
			items.push(<option key={url.symbol} value={url.symbol}>{url.name}</option>);
		}
		return items;
	}

	changeSelection(event) {
		this.setState({
			symbol: event.target.value
		});

		if (this.state.phrase) {
			this.search();
		}
	}

	updatePhrase(event) {
		this.setState({
			phrase: event.target.value
		});
	}

	search(event) {
		event && event.preventDefault();

		SearchStateService.push({
			phrase: this.state.phrase,
			symbol: this.state.symbol
		});
	}

	isDisabled() {
		return SearchStateService.isInvalid();
	}

}
