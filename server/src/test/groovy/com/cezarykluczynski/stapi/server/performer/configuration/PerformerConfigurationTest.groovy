package com.cezarykluczynski.stapi.server.performer.configuration

import com.cezarykluczynski.stapi.server.performer.endpoint.PerformerRestEndpoint
import com.cezarykluczynski.stapi.server.performer.endpoint.PerformerSoapEndpoint
import com.cezarykluczynski.stapi.server.performer.mapper.PerformerBaseRestMapper
import com.cezarykluczynski.stapi.server.performer.mapper.PerformerBaseSoapMapper
import com.cezarykluczynski.stapi.server.performer.mapper.PerformerFullRestMapper
import com.cezarykluczynski.stapi.server.performer.mapper.PerformerFullSoapMapper
import com.cezarykluczynski.stapi.server.performer.reader.PerformerRestReader
import com.cezarykluczynski.stapi.server.performer.reader.PerformerSoapReader
import org.apache.cxf.bus.spring.SpringBus
import org.apache.cxf.jaxws.EndpointImpl
import org.springframework.context.ApplicationContext
import spock.lang.Specification

import javax.xml.ws.Endpoint

class PerformerConfigurationTest extends Specification {

	private ApplicationContext applicationContextMock

	private PerformerConfiguration performerConfiguration

	void setup() {
		applicationContextMock = Mock(ApplicationContext)
		performerConfiguration = new PerformerConfiguration(applicationContext: applicationContextMock)
	}

	void "Performer SOAP endpoint is created"() {
		given:
		SpringBus springBus = new SpringBus()
		PerformerSoapReader performerSoapReaderMock = Mock(PerformerSoapReader)

		when:
		Endpoint performerSoapEndpoint = performerConfiguration.performerSoapEndpoint()

		then:
		1 * applicationContextMock.getBean(SpringBus) >> springBus
		1 * applicationContextMock.getBean(PerformerSoapReader) >> performerSoapReaderMock
		0 * _
		performerSoapEndpoint != null
		((EndpointImpl) performerSoapEndpoint).implementor instanceof PerformerSoapEndpoint
		((EndpointImpl) performerSoapEndpoint).bus == springBus
		performerSoapEndpoint.published
	}

	void "PerformerRestEndpoint is created"() {
		given:
		PerformerRestReader performerRestMapper = Mock(PerformerRestReader)

		when:
		PerformerRestEndpoint performerRestEndpoint = performerConfiguration.performerRestEndpoint()

		then:
		1 * applicationContextMock.getBean(PerformerRestReader) >> performerRestMapper
		0 * _
		performerRestEndpoint != null
		performerRestEndpoint.performerRestReader == performerRestMapper
	}

	void "PerformerBaseSoapMapper is created"() {
		when:
		PerformerBaseSoapMapper performerBaseSoapMapper = performerConfiguration.performerBaseSoapMapper()

		then:
		performerBaseSoapMapper != null
	}

	void "PerformerFullSoapMapper is created"() {
		when:
		PerformerFullSoapMapper performerFullSoapMapper = performerConfiguration.performerFullSoapMapper()

		then:
		performerFullSoapMapper != null
	}

	void "PerformerBaseRestMapper is created"() {
		when:
		PerformerBaseRestMapper performerBaseRestMapper = performerConfiguration.performerBaseRestMapper()

		then:
		performerBaseRestMapper != null
	}

	void "PerformerFullRestMapper is created"() {
		when:
		PerformerFullRestMapper performerFullRestMapper = performerConfiguration.performerFullRestMapper()

		then:
		performerFullRestMapper != null
	}

}
