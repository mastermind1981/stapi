package com.cezarykluczynski.stapi.etl.astronomicalObject.creation.processor

import com.cezarykluczynski.stapi.etl.template.planet.dto.PlanetTemplate
import com.cezarykluczynski.stapi.etl.template.planet.dto.enums.AstronomicalObjectType as EtlAstronomicalObjectType
import com.cezarykluczynski.stapi.etl.template.planet.mapper.AstronomicalObjectTypeMapper
import com.cezarykluczynski.stapi.model.astronomicalObject.entity.AstronomicalObject
import com.cezarykluczynski.stapi.model.astronomicalObject.entity.enums.AstronomicalObjectType as ModelAstronomicalObjectType
import com.cezarykluczynski.stapi.model.common.service.UidGenerator
import com.cezarykluczynski.stapi.model.page.entity.Page
import spock.lang.Specification

class PlanetTemplateProcessorTest extends Specification {

	private static final String NAME = 'NAME'
	private static final String UID = 'UID'
	private static final EtlAstronomicalObjectType ETL_ASTRONOMICAL_OBJECT_TYPE = EtlAstronomicalObjectType.GALAXY
	private static final ModelAstronomicalObjectType MODEL_ASTRONOMICAL_OBJECT_TYPE = ModelAstronomicalObjectType.GALAXY

	private final Page page = Mock()

	private UidGenerator uidGeneratorMock

	private AstronomicalObjectTypeMapper astronomicalObjectTypeMapperMock

	private PlanetTemplateProcessor planetTemplateProcessor

	void setup() {
		uidGeneratorMock = Mock()
		astronomicalObjectTypeMapperMock = Mock()
		planetTemplateProcessor = new PlanetTemplateProcessor(uidGeneratorMock, astronomicalObjectTypeMapperMock)
	}

	void "returns null when PlanetTemplate is a product of redirect"() {
		given:
		PlanetTemplate planetTemplate = new PlanetTemplate(productOfRedirect: true)

		when:
		AstronomicalObject astronomicalObject = planetTemplateProcessor.process(planetTemplate)

		then:
		astronomicalObject == null
	}

	void "maps PlanetTemplate to AstronomicalObject"() {
		given:
		PlanetTemplate planetTemplate = new PlanetTemplate(
				name: NAME,
				page: page,
				astronomicalObjectType: ETL_ASTRONOMICAL_OBJECT_TYPE)

		when:
		AstronomicalObject astronomicalObject = planetTemplateProcessor.process(planetTemplate)

		then:
		1 * uidGeneratorMock.generateFromPage(page, AstronomicalObject) >> UID
		1 * astronomicalObjectTypeMapperMock.fromEtlToModel(ETL_ASTRONOMICAL_OBJECT_TYPE) >> MODEL_ASTRONOMICAL_OBJECT_TYPE
		0 * _
		astronomicalObject.name == NAME
		astronomicalObject.page == page
		astronomicalObject.uid == UID
		astronomicalObject.astronomicalObjectType == MODEL_ASTRONOMICAL_OBJECT_TYPE
	}

}
