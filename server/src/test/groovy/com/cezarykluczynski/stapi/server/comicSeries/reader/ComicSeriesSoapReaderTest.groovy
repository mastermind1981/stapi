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
import com.cezarykluczynski.stapi.server.common.validator.exceptions.MissingUIDException
import com.google.common.collect.Lists
import org.springframework.data.domain.Page
import spock.lang.Specification

class ComicSeriesSoapReaderTest extends Specification {

	private static final String UID = 'UID'

	private ComicSeriesSoapQuery comicSeriesSoapQueryBuilderMock

	private ComicSeriesBaseSoapMapper comicSeriesBaseSoapMapperMock

	private ComicSeriesFullSoapMapper comicSeriesFullSoapMapperMock

	private PageMapper pageMapperMock

	private ComicSeriesSoapReader comicSeriesSoapReader

	void setup() {
		comicSeriesSoapQueryBuilderMock = Mock()
		comicSeriesBaseSoapMapperMock = Mock()
		comicSeriesFullSoapMapperMock = Mock()
		pageMapperMock = Mock()
		comicSeriesSoapReader = new ComicSeriesSoapReader(comicSeriesSoapQueryBuilderMock, comicSeriesBaseSoapMapperMock,
				comicSeriesFullSoapMapperMock, pageMapperMock)
	}

	void "passed base request to queryBuilder, then to mapper, and returns result"() {
		given:
		List<ComicSeries> comicSeriesList = Lists.newArrayList()
		Page<ComicSeries> comicSeriesPage = Mock()
		List<ComicSeriesBase> soapComicSeriesList = Lists.newArrayList(new ComicSeriesBase(uid: UID))
		ComicSeriesBaseRequest comicSeriesBaseRequest = Mock()
		ResponsePage responsePage = Mock()

		when:
		ComicSeriesBaseResponse comicSeriesResponse = comicSeriesSoapReader.readBase(comicSeriesBaseRequest)

		then:
		1 * comicSeriesSoapQueryBuilderMock.query(comicSeriesBaseRequest) >> comicSeriesPage
		1 * comicSeriesPage.content >> comicSeriesList
		1 * pageMapperMock.fromPageToSoapResponsePage(comicSeriesPage) >> responsePage
		1 * comicSeriesBaseSoapMapperMock.mapBase(comicSeriesList) >> soapComicSeriesList
		comicSeriesResponse.comicSeries[0].uid == UID
		comicSeriesResponse.page == responsePage
	}

	void "passed full request to queryBuilder, then to mapper, and returns result"() {
		given:
		ComicSeriesFull comicSeriesFull = new ComicSeriesFull(uid: UID)
		ComicSeries comicSeries = Mock()
		Page<ComicSeries> comicSeriesPage = Mock()
		ComicSeriesFullRequest comicSeriesFullRequest = new ComicSeriesFullRequest(uid: UID)

		when:
		ComicSeriesFullResponse comicSeriesFullResponse = comicSeriesSoapReader.readFull(comicSeriesFullRequest)

		then:
		1 * comicSeriesSoapQueryBuilderMock.query(comicSeriesFullRequest) >> comicSeriesPage
		1 * comicSeriesPage.content >> Lists.newArrayList(comicSeries)
		1 * comicSeriesFullSoapMapperMock.mapFull(comicSeries) >> comicSeriesFull
		comicSeriesFullResponse.comicSeries.uid == UID
	}

	void "requires UID in full request"() {
		given:
		ComicSeriesFullRequest comicSeriesFullRequest = Mock()

		when:
		comicSeriesSoapReader.readFull(comicSeriesFullRequest)

		then:
		thrown(MissingUIDException)
	}

}
