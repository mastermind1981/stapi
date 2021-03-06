package com.cezarykluczynski.stapi.server.comicCollection.reader

import com.cezarykluczynski.stapi.client.v1.rest.model.ComicCollectionBase
import com.cezarykluczynski.stapi.client.v1.rest.model.ComicCollectionBaseResponse
import com.cezarykluczynski.stapi.client.v1.rest.model.ComicCollectionFull
import com.cezarykluczynski.stapi.client.v1.rest.model.ComicCollectionFullResponse
import com.cezarykluczynski.stapi.client.v1.rest.model.ResponsePage
import com.cezarykluczynski.stapi.model.comicCollection.entity.ComicCollection
import com.cezarykluczynski.stapi.server.comicCollection.dto.ComicCollectionRestBeanParams
import com.cezarykluczynski.stapi.server.comicCollection.mapper.ComicCollectionBaseRestMapper
import com.cezarykluczynski.stapi.server.comicCollection.mapper.ComicCollectionFullRestMapper
import com.cezarykluczynski.stapi.server.comicCollection.query.ComicCollectionRestQuery
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper
import com.cezarykluczynski.stapi.server.common.validator.exceptions.MissingUIDException
import com.google.common.collect.Lists
import org.springframework.data.domain.Page
import spock.lang.Specification

class ComicCollectionRestReaderTest extends Specification {

	private static final String UID = 'UID'

	private ComicCollectionRestQuery comicCollectionRestQueryBuilderMock

	private ComicCollectionBaseRestMapper comicCollectionBaseRestMapperMock

	private ComicCollectionFullRestMapper comicCollectionFullRestMapperMock

	private PageMapper pageMapperMock

	private ComicCollectionRestReader comicCollectionRestReader

	void setup() {
		comicCollectionRestQueryBuilderMock = Mock()
		comicCollectionBaseRestMapperMock = Mock()
		comicCollectionFullRestMapperMock = Mock()
		pageMapperMock = Mock()
		comicCollectionRestReader = new ComicCollectionRestReader(comicCollectionRestQueryBuilderMock, comicCollectionBaseRestMapperMock,
				comicCollectionFullRestMapperMock, pageMapperMock)
	}

	void "passed request to queryBuilder, then to mapper, and returns result"() {
		given:
		ComicCollectionBase comicCollectionBase = Mock()
		ComicCollection comicCollection = Mock()
		ComicCollectionRestBeanParams comicCollectionRestBeanParams = Mock()
		List<ComicCollectionBase> comicCollectionBaseList = Lists.newArrayList(comicCollectionBase)
		List<ComicCollection> comicCollectionList = Lists.newArrayList(comicCollection)
		Page<ComicCollection> comicCollectionPage = Mock()
		ResponsePage responsePage = Mock()

		when:
		ComicCollectionBaseResponse comicCollectionResponseOutput = comicCollectionRestReader.readBase(comicCollectionRestBeanParams)

		then:
		1 * comicCollectionRestQueryBuilderMock.query(comicCollectionRestBeanParams) >> comicCollectionPage
		1 * pageMapperMock.fromPageToRestResponsePage(comicCollectionPage) >> responsePage
		1 * comicCollectionPage.content >> comicCollectionList
		1 * comicCollectionBaseRestMapperMock.mapBase(comicCollectionList) >> comicCollectionBaseList
		0 * _
		comicCollectionResponseOutput.comicCollections == comicCollectionBaseList
		comicCollectionResponseOutput.page == responsePage
	}

	void "passed UID to queryBuilder, then to mapper, and returns result"() {
		given:
		ComicCollectionFull comicCollectionFull = Mock()
		ComicCollection comicCollection = Mock()
		List<ComicCollection> comicCollectionList = Lists.newArrayList(comicCollection)
		Page<ComicCollection> comicCollectionPage = Mock()

		when:
		ComicCollectionFullResponse comicCollectionResponseOutput = comicCollectionRestReader.readFull(UID)

		then:
		1 * comicCollectionRestQueryBuilderMock.query(_ as ComicCollectionRestBeanParams) >> {
				ComicCollectionRestBeanParams comicCollectionRestBeanParams ->
			assert comicCollectionRestBeanParams.uid == UID
			comicCollectionPage
		}
		1 * comicCollectionPage.content >> comicCollectionList
		1 * comicCollectionFullRestMapperMock.mapFull(comicCollection) >> comicCollectionFull
		0 * _
		comicCollectionResponseOutput.comicCollection == comicCollectionFull
	}

	void "requires UID in full request"() {
		when:
		comicCollectionRestReader.readFull(null)

		then:
		thrown(MissingUIDException)
	}

}
