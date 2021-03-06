package com.cezarykluczynski.stapi.etl.template.individual.processor

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair
import com.cezarykluczynski.stapi.etl.template.common.dto.enums.Gender
import com.cezarykluczynski.stapi.etl.template.common.processor.MaritalStatusProcessor
import com.cezarykluczynski.stapi.etl.template.common.processor.gender.PartToGenderProcessor
import com.cezarykluczynski.stapi.etl.template.individual.dto.IndividualLifeBoundaryDTO
import com.cezarykluczynski.stapi.etl.template.individual.dto.IndividualTemplate
import com.cezarykluczynski.stapi.etl.template.individual.dto.IndividualTemplateParameter
import com.cezarykluczynski.stapi.etl.template.individual.processor.species.CharacterSpeciesWikitextProcessor
import com.cezarykluczynski.stapi.model.character.entity.CharacterSpecies
import com.cezarykluczynski.stapi.model.common.entity.enums.BloodType
import com.cezarykluczynski.stapi.model.common.entity.enums.MaritalStatus
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template
import com.cezarykluczynski.stapi.util.ReflectionTestUtils
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification

class IndividualTemplatePartsEnrichingProcessorTest extends Specification {

	private static final String VALUE = 'VALUE'
	private static final Integer HEIGHT = 183
	private static final Integer WEIGHT = 77
	private static final Integer YEAR = 1970
	private static final Integer MONTH = 10
	private static final Integer DAY = 7
	private static final Gender GENDER = Gender.F
	private static final MaritalStatus MARITAL_STATUS = MaritalStatus.MARRIED
	private static final BloodType BLOOD_TYPE = BloodType.B_NEGATIVE

	private PartToGenderProcessor partToGenderProcessorMock

	private IndividualLifeBoundaryProcessor individualLifeBoundaryProcessorMock

	private IndividualTemplateActorLinkingProcessor individualActorLinkingProcessorMock

	private IndividualHeightProcessor individualHeightProcessorMock

	private IndividualWeightProcessor individualWeightProcessorMock

	private IndividualBloodTypeProcessor individualBloodTypeProcessorMock

	private MaritalStatusProcessor maritalStatusProcessorMock

	private CharacterSpeciesWikitextProcessor characterSpeciesWikitextProcessorMock

	private IndividualTemplatePartsEnrichingProcessor individualTemplatePartsEnrichingProcessor

	void setup() {
		partToGenderProcessorMock = Mock()
		individualLifeBoundaryProcessorMock = Mock()
		individualActorLinkingProcessorMock = Mock()
		individualHeightProcessorMock = Mock()
		individualWeightProcessorMock = Mock()
		individualBloodTypeProcessorMock = Mock()
		maritalStatusProcessorMock = Mock()
		characterSpeciesWikitextProcessorMock = Mock()
		individualTemplatePartsEnrichingProcessor = new IndividualTemplatePartsEnrichingProcessor(partToGenderProcessorMock,
				individualLifeBoundaryProcessorMock, individualActorLinkingProcessorMock, individualHeightProcessorMock,
				individualWeightProcessorMock, individualBloodTypeProcessorMock, maritalStatusProcessorMock,
				characterSpeciesWikitextProcessorMock)
	}

	void "sets gender from PartToGenderProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(key: IndividualTemplateParameter.GENDER)
		IndividualTemplate individualTemplate = new IndividualTemplate()

		when:
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), individualTemplate))

		then:
		1 * partToGenderProcessorMock.process(templatePart) >> GENDER
		0 * _
		individualTemplate.gender == GENDER
		ReflectionTestUtils.getNumberOfNotNullFields(individualTemplate) == 4
	}

	void "when actor key is found, part is passed to IndividualActorLinkingProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(key: IndividualTemplateParameter.ACTOR)
		IndividualTemplate individualTemplateInActorLinkingProcessor = null
		IndividualTemplate individualTemplate = new IndividualTemplate()

		when:
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), individualTemplate))

		then:
		1 * individualActorLinkingProcessorMock.enrich(_ as EnrichablePair<Template.Part, IndividualTemplate>) >> {
			EnrichablePair<Template.Part, IndividualTemplate> enrichablePair ->
				assert enrichablePair.input == templatePart
				individualTemplateInActorLinkingProcessor = enrichablePair.output
		}
		0 * _
		ReflectionTestUtils.getNumberOfNotNullFields(individualTemplate) == 3
		individualTemplateInActorLinkingProcessor == individualTemplate
	}

	void "sets height from IndividualHeightProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(
				key: IndividualTemplateParameter.HEIGHT,
				value: VALUE)
		IndividualTemplate individualTemplate = new IndividualTemplate()

		when:
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), individualTemplate))

		then:
		1 * individualHeightProcessorMock.process(VALUE) >> HEIGHT
		0 * _
		individualTemplate.height == HEIGHT
		ReflectionTestUtils.getNumberOfNotNullFields(individualTemplate) == 4
	}

	void "sets weight from IndividualWeightProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(
				key: IndividualTemplateParameter.WEIGHT,
				value: VALUE)
		IndividualTemplate individualTemplate = new IndividualTemplate()

		when:
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), individualTemplate))

		then:
		1 * individualWeightProcessorMock.process(VALUE) >> WEIGHT
		0 * _
		individualTemplate.weight == WEIGHT
		ReflectionTestUtils.getNumberOfNotNullFields(individualTemplate) == 4
	}

	void "does not set serial number when it is not empty"() {
		given:
		Template.Part templatePart = new Template.Part(
				key: IndividualTemplateParameter.SERIAL_NUMBER,
				value: '')
		IndividualTemplate individualTemplate = new IndividualTemplate()

		when:
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), individualTemplate))

		then:
		0 * _
		individualTemplate.serialNumber == null
		ReflectionTestUtils.getNumberOfNotNullFields(individualTemplate) == 3
	}

	void "sets serial number when it is not empty"() {
		given:
		Template.Part templatePart = new Template.Part(
				key: IndividualTemplateParameter.SERIAL_NUMBER,
				value: VALUE)
		IndividualTemplate individualTemplate = new IndividualTemplate()

		when:
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), individualTemplate))

		then:
		0 * _
		individualTemplate.serialNumber == VALUE
		ReflectionTestUtils.getNumberOfNotNullFields(individualTemplate) == 4
	}

	void "sets birth values from IndividualLifeBoundaryProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(
				key: IndividualTemplateParameter.BORN,
				value: VALUE)
		IndividualLifeBoundaryDTO individualLifeBoundaryDTO = new IndividualLifeBoundaryDTO(
				year: YEAR,
				month: MONTH,
				day: DAY)
		IndividualTemplate individualTemplate = new IndividualTemplate()

		when:
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), individualTemplate))

		then:
		1 * individualLifeBoundaryProcessorMock.process(VALUE) >> individualLifeBoundaryDTO
		0 * _
		individualTemplate.yearOfBirth == YEAR
		individualTemplate.monthOfBirth == MONTH
		individualTemplate.dayOfBirth == DAY
		ReflectionTestUtils.getNumberOfNotNullFields(individualTemplate) == 6
	}

	void "sets death values from IndividualLifeBoundaryProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(
				key: IndividualTemplateParameter.DIED,
				value: VALUE)
		IndividualLifeBoundaryDTO individualLifeBoundaryDTO = new IndividualLifeBoundaryDTO(
				year: YEAR,
				month: MONTH,
				day: DAY)
		IndividualTemplate individualTemplate = new IndividualTemplate()

		when:
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), individualTemplate))

		then:
		1 * individualLifeBoundaryProcessorMock.process(VALUE) >> individualLifeBoundaryDTO
		0 * _
		individualTemplate.yearOfDeath == YEAR
		individualTemplate.monthOfDeath == MONTH
		individualTemplate.dayOfDeath == DAY
		ReflectionTestUtils.getNumberOfNotNullFields(individualTemplate) == 6
	}

	void "sets marital status from MaritalStatusProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(
				key: IndividualTemplateParameter.MARITAL_STATUS,
				value: VALUE)
		IndividualTemplate individualTemplate = new IndividualTemplate()

		when:
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), individualTemplate))

		then:
		1 * maritalStatusProcessorMock.process(VALUE) >> MARITAL_STATUS
		0 * _
		individualTemplate.maritalStatus == MARITAL_STATUS
		ReflectionTestUtils.getNumberOfNotNullFields(individualTemplate) == 4
	}

	void "sets blood type from BloodTypeProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(
				key: IndividualTemplateParameter.BLOOD_TYPE,
				value: VALUE)
		IndividualTemplate individualTemplate = new IndividualTemplate()

		when:
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), individualTemplate))

		then:
		1 * individualBloodTypeProcessorMock.process(VALUE) >> BLOOD_TYPE
		0 * _
		individualTemplate.bloodType == BLOOD_TYPE
		ReflectionTestUtils.getNumberOfNotNullFields(individualTemplate) == 4
	}

	void "adds all CharacterSpecies from CharacterSpeciesWikitextProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(key: IndividualTemplateParameter.SPECIES, value: VALUE)
		IndividualTemplate individualTemplate = new IndividualTemplate()
		CharacterSpecies characterSpecies1 = Mock()
		CharacterSpecies characterSpecies2 = Mock()

		when:
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), individualTemplate))

		then:
		1 * characterSpeciesWikitextProcessorMock.process(_ as Pair) >> Sets.newHashSet(characterSpecies1, characterSpecies2)
		0 * _
		individualTemplate.characterSpecies.contains characterSpecies1
		individualTemplate.characterSpecies.contains characterSpecies2
	}

}
