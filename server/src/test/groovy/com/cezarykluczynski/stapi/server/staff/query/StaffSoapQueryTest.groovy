package com.cezarykluczynski.stapi.server.staff.query

import com.cezarykluczynski.stapi.client.v1.soap.RequestPage
import com.cezarykluczynski.stapi.client.v1.soap.StaffBaseRequest
import com.cezarykluczynski.stapi.client.v1.soap.StaffFullRequest
import com.cezarykluczynski.stapi.model.staff.dto.StaffRequestDTO
import com.cezarykluczynski.stapi.model.staff.repository.StaffRepository
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper
import com.cezarykluczynski.stapi.server.staff.mapper.StaffBaseSoapMapper
import com.cezarykluczynski.stapi.server.staff.mapper.StaffFullSoapMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

class StaffSoapQueryTest extends Specification {

	private StaffBaseSoapMapper staffBaseSoapMapperMock

	private StaffFullSoapMapper staffFullSoapMapperMock

	private PageMapper pageMapperMock

	private StaffRepository staffRepositoryMock

	private StaffSoapQuery staffSoapQuery

	void setup() {
		staffBaseSoapMapperMock = Mock(StaffBaseSoapMapper)
		staffFullSoapMapperMock = Mock(StaffFullSoapMapper)
		pageMapperMock = Mock(PageMapper)
		staffRepositoryMock = Mock(StaffRepository)
		staffSoapQuery = new StaffSoapQuery(staffBaseSoapMapperMock, staffFullSoapMapperMock, pageMapperMock, staffRepositoryMock)
	}

	void "maps StaffBaseRequest to StaffRequestDTO and to PageRequest, then calls repository, then returns result"() {
		given:
		RequestPage requestPage = Mock(RequestPage)
		PageRequest pageRequest = Mock(PageRequest)
		StaffBaseRequest staffRequest = Mock(StaffBaseRequest)
		staffRequest.page >> requestPage
		StaffRequestDTO staffRequestDTO = Mock(StaffRequestDTO)
		Page page = Mock(Page)

		when:
		Page pageOutput = staffSoapQuery.query(staffRequest)

		then:
		1 * staffBaseSoapMapperMock.mapBase(staffRequest) >> staffRequestDTO
		1 * pageMapperMock.fromRequestPageToPageRequest(requestPage) >> pageRequest
		1 * staffRepositoryMock.findMatching(staffRequestDTO, pageRequest) >> page
		pageOutput == page
	}

	void "maps StaffFullRequest to StaffRequestDTO, then calls repository, then returns result"() {
		given:
		PageRequest pageRequest = Mock(PageRequest)
		StaffFullRequest staffRequest = Mock(StaffFullRequest)
		StaffRequestDTO staffRequestDTO = Mock(StaffRequestDTO)
		Page page = Mock(Page)

		when:
		Page pageOutput = staffSoapQuery.query(staffRequest)

		then:
		1 * staffFullSoapMapperMock.mapFull(staffRequest) >> staffRequestDTO
		1 * pageMapperMock.defaultPageRequest >> pageRequest
		1 * staffRepositoryMock.findMatching(staffRequestDTO, pageRequest) >> page
		pageOutput == page
	}

}
