package com.cezarykluczynski.stapi.server.comicSeries.reader

import com.cezarykluczynski.stapi.client.v1.soap.ComicSeriesBase
import com.cezarykluczynski.stapi.client.v1.soap.ComicSeriesBaseRequest
import com.cezarykluczynski.stapi.client.v1.soap.ComicSeriesBaseResponse
import com.cezarykluczynski.stapi.client.v1.soap.ComicSeriesFull
import com.cezarykluczynski.stapi.client.v1.soap.ComicSeriesFullRequest
import com.cezarykluczynski.stapi.client.v1.soap.ComicSeriesFullResponse
import com.cezarykluczynski.stapi.client.v1.soap.ResponsePage
import com.cezarykluczynski.stapi.model.comicSeries.entity.ComicSeries
import com.cezarykluczynski.stapi.server.comicSeries.mapper.ComicSeriesBaseSoapMapper
import com.cezarykluczynski.stapi.server.comicSeries.mapper.ComicSeriesFullSoapMapper
import com.cezarykluczynski.stapi.server.comicSeries.query.ComicSeriesSoapQuery
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper
import com.google.common.collect.Lists
import org.springframework.data.domain.Page
import spock.lang.Specification

class ComicSeriesSoapReaderTest extends Specification {

	private static final String GUID = 'GUID'

	private ComicSeriesSoapQuery comicSeriesSoapQueryBuilderMock

	private ComicSeriesBaseSoapMapper comicSeriesBaseSoapMapperMock

	private ComicSeriesFullSoapMapper comicSeriesFullSoapMapperMock

	private PageMapper pageMapperMock

	private ComicSeriesSoapReader comicSeriesSoapReader

	void setup() {
		comicSeriesSoapQueryBuilderMock = Mock(ComicSeriesSoapQuery)
		comicSeriesBaseSoapMapperMock = Mock(ComicSeriesBaseSoapMapper)
		comicSeriesFullSoapMapperMock = Mock(ComicSeriesFullSoapMapper)
		pageMapperMock = Mock(PageMapper)
		comicSeriesSoapReader = new ComicSeriesSoapReader(comicSeriesSoapQueryBuilderMock, comicSeriesBaseSoapMapperMock,
				comicSeriesFullSoapMapperMock, pageMapperMock)
	}

	void "passed base request to queryBuilder, then to mapper, and returns result"() {
		given:
		List<ComicSeries> comicSeriesList = Lists.newArrayList()
		Page<ComicSeries> comicSeriesPage = Mock(Page)
		List<ComicSeriesBase> soapComicSeriesList = Lists.newArrayList(new ComicSeriesBase(guid: GUID))
		ComicSeriesBaseRequest comicSeriesBaseRequest = Mock(ComicSeriesBaseRequest)
		ResponsePage responsePage = Mock(ResponsePage)

		when:
		ComicSeriesBaseResponse comicSeriesResponse = comicSeriesSoapReader.readBase(comicSeriesBaseRequest)

		then:
		1 * comicSeriesSoapQueryBuilderMock.query(comicSeriesBaseRequest) >> comicSeriesPage
		1 * comicSeriesPage.content >> comicSeriesList
		1 * pageMapperMock.fromPageToSoapResponsePage(comicSeriesPage) >> responsePage
		1 * comicSeriesBaseSoapMapperMock.mapBase(comicSeriesList) >> soapComicSeriesList
		comicSeriesResponse.comicSeries[0].guid == GUID
		comicSeriesResponse.page == responsePage
	}

	void "passed full request to queryBuilder, then to mapper, and returns result"() {
		given:
		ComicSeriesFull comicSeriesFull = new ComicSeriesFull(guid: GUID)
		ComicSeries comicSeries = Mock(ComicSeries)
		Page<ComicSeries> comicSeriesPage = Mock(Page)
		ComicSeriesFullRequest comicSeriesFullRequest = Mock(ComicSeriesFullRequest)

		when:
		ComicSeriesFullResponse comicSeriesFullResponse = comicSeriesSoapReader.readFull(comicSeriesFullRequest)

		then:
		1 * comicSeriesSoapQueryBuilderMock.query(comicSeriesFullRequest) >> comicSeriesPage
		1 * comicSeriesPage.content >> Lists.newArrayList(comicSeries)
		1 * comicSeriesFullSoapMapperMock.mapFull(comicSeries) >> comicSeriesFull
		comicSeriesFullResponse.comicSeries.guid == GUID
	}

}
