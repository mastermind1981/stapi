package com.cezarykluczynski.stapi.etl.template.comicSeries.processor

import spock.lang.Specification

class ComicSeriesTemplateNumberOfIssuesFixedValueProviderTest extends Specification {

	private static final String EXISTING_TITLE = 'Star Trek: Deep Space Nine (IDW)'
	private static final String NONEXISTING_TITLE = 'NONEXISTING_TITLE'

	private ComicSeriesTemplateNumberOfIssuesFixedValueProvider comicSeriesTemplateNumberOfIssuesFixedValueProvider

	void setup() {
		comicSeriesTemplateNumberOfIssuesFixedValueProvider = new ComicSeriesTemplateNumberOfIssuesFixedValueProvider()
	}

	void "provides correct range"() {
		expect:
		comicSeriesTemplateNumberOfIssuesFixedValueProvider.getSearchedValue(EXISTING_TITLE).found
		comicSeriesTemplateNumberOfIssuesFixedValueProvider.getSearchedValue(EXISTING_TITLE).value == 1
	}

	void "provides missing range"() {
		expect:
		!comicSeriesTemplateNumberOfIssuesFixedValueProvider.getSearchedValue(NONEXISTING_TITLE).found
		comicSeriesTemplateNumberOfIssuesFixedValueProvider.getSearchedValue(NONEXISTING_TITLE).value == null
	}

}