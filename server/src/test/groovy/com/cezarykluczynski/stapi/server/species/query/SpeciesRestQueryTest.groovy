package com.cezarykluczynski.stapi.server.species.query

import com.cezarykluczynski.stapi.model.species.dto.SpeciesRequestDTO
import com.cezarykluczynski.stapi.model.species.repository.SpeciesRepository
import com.cezarykluczynski.stapi.server.species.dto.SpeciesRestBeanParams
import com.cezarykluczynski.stapi.server.species.mapper.SpeciesRestMapper
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

class SpeciesRestQueryTest extends Specification {

	private SpeciesRestMapper speciesRestMapperMock

	private PageMapper pageMapperMock

	private SpeciesRepository speciesRepositoryMock

	private SpeciesRestQuery speciesRestQuery

	void setup() {
		speciesRestMapperMock = Mock(SpeciesRestMapper)
		pageMapperMock = Mock(PageMapper)
		speciesRepositoryMock = Mock(SpeciesRepository)
		speciesRestQuery = new SpeciesRestQuery(speciesRestMapperMock, pageMapperMock,
				speciesRepositoryMock)
	}

	void "maps SpeciesRestBeanParams to SpeciesRequestDTO and to PageRequest, then calls repository, then returns result"() {
		given:
		PageRequest pageRequest = Mock(PageRequest)
		SpeciesRestBeanParams speciesRestBeanParams = Mock(SpeciesRestBeanParams) {

		}
		SpeciesRequestDTO speciesRequestDTO = Mock(SpeciesRequestDTO)
		Page page = Mock(Page)

		when:
		Page pageOutput = speciesRestQuery.query(speciesRestBeanParams)

		then:
		1 * speciesRestMapperMock.map(speciesRestBeanParams) >> speciesRequestDTO
		1 * pageMapperMock.fromPageSortBeanParamsToPageRequest(speciesRestBeanParams) >> pageRequest
		1 * speciesRepositoryMock.findMatching(speciesRequestDTO, pageRequest) >> page
		pageOutput == page
	}

}