package com.cezarykluczynski.stapi.etl.template.comics.processor

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair
import com.cezarykluczynski.stapi.etl.common.processor.comicSeries.WikitextToComicSeriesProcessor
import com.cezarykluczynski.stapi.etl.common.processor.company.WikitextToCompaniesProcessor
import com.cezarykluczynski.stapi.etl.reference.processor.ReferencesFromTemplatePartProcessor
import com.cezarykluczynski.stapi.etl.template.comicSeries.dto.ComicSeriesTemplate
import com.cezarykluczynski.stapi.etl.template.comics.dto.ComicsTemplate
import com.cezarykluczynski.stapi.etl.template.comics.dto.ComicsTemplateParameter
import com.cezarykluczynski.stapi.etl.template.common.dto.datetime.StardateRange
import com.cezarykluczynski.stapi.etl.template.common.dto.datetime.YearRange
import com.cezarykluczynski.stapi.etl.template.common.processor.datetime.WikitextToStardateRangeProcessor
import com.cezarykluczynski.stapi.etl.template.common.processor.datetime.WikitextToYearRangeProcessor
import com.cezarykluczynski.stapi.model.comicSeries.entity.ComicSeries
import com.cezarykluczynski.stapi.model.company.entity.Company
import com.cezarykluczynski.stapi.model.reference.entity.Reference
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import spock.lang.Specification

class ComicsTemplatePartsEnrichingProcessorTest extends Specification {

	private static final String PUBLISHER = 'PUBLISHER'
	private static final String PUBLISHED = 'PUBLISHED'
	private static final String YEARS = '1995-1997'
	private static final Integer YEAR_FROM = 1995
	private static final Integer YEAR_TO = 1997
	private static final String PAGES_STRING = '32'
	private static final Integer PAGES_INTEGER = 32
	private static final String STARDATES = '1995-1997'
	private static final Float STARDATE_FROM = 123.4F
	private static final Float STARDATE_TO = 456.7F
	private static final String SERIES = 'SERIES'
	private static final String WRITER = 'WRITER'
	private static final String ARTIST = 'ARTIST'
	private static final String EDITOR = 'EDITOR'
	private static final String REFERENCE = 'REFERENCE'

	private ComicsTemplatePartStaffEnrichingProcessor comicsTemplatePartStaffEnrichingProcessorMock

	private WikitextToCompaniesProcessor wikitextToCompaniesProcessorMock

	private WikitextToComicSeriesProcessor wikitextToComicSeriesProcessorMock

	private WikitextToYearRangeProcessor wikitextToYearRangeProcessorMock

	private WikitextToStardateRangeProcessor wikitextToStardateRangeProcessorMock

	private ComicsTemplatePublishedDatesEnrichingProcessor comicsTemplatePublishedDatesEnrichingProcessorMock

	private ReferencesFromTemplatePartProcessor referencesFromTemplatePartProcessorMock

	private ComicsTemplatePartsEnrichingProcessor comicsTemplatePartsEnrichingProcessor

	void setup() {
		comicsTemplatePartStaffEnrichingProcessorMock = Mock()
		wikitextToCompaniesProcessorMock = Mock()
		wikitextToComicSeriesProcessorMock = Mock()
		wikitextToYearRangeProcessorMock = Mock()
		wikitextToStardateRangeProcessorMock = Mock()
		comicsTemplatePublishedDatesEnrichingProcessorMock = Mock()
		referencesFromTemplatePartProcessorMock = Mock()
		comicsTemplatePartsEnrichingProcessor = new ComicsTemplatePartsEnrichingProcessor(comicsTemplatePartStaffEnrichingProcessorMock,
				wikitextToCompaniesProcessorMock, wikitextToComicSeriesProcessorMock, wikitextToYearRangeProcessorMock,
				wikitextToStardateRangeProcessorMock, comicsTemplatePublishedDatesEnrichingProcessorMock,
				referencesFromTemplatePartProcessorMock)
	}

	void "passes ComicsTemplate to ComicsTemplatePartStaffEnrichingProcessor when writer part is found"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.WRITER, value: WRITER)
		ComicsTemplate comicsTemplate = new ComicsTemplate()
		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		1 * comicsTemplatePartStaffEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
			EnrichablePair<Template.Part, ComicsTemplate> enrichablePair ->
				assert enrichablePair.input == templatePart
				assert enrichablePair.output != null
		}
		0 * _
	}

	void "passes ComicsTemplate to ComicsTemplatePartStaffEnrichingProcessor when artist part is found"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.ARTIST, value: ARTIST)
		ComicsTemplate comicsTemplate = new ComicsTemplate()
		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		1 * comicsTemplatePartStaffEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
			EnrichablePair<Template.Part, ComicsTemplate> enrichablePair ->
				assert enrichablePair.input == templatePart
				assert enrichablePair.output != null
		}
		0 * _
	}

	void "passes ComicsTemplate to ComicsTemplatePartStaffEnrichingProcessor when editor part is found"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.EDITOR, value: EDITOR)
		ComicsTemplate comicsTemplate = new ComicsTemplate()
		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		1 * comicsTemplatePartStaffEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
				EnrichablePair<Template.Part, ComicsTemplate> enrichablePair ->
			assert enrichablePair.input == templatePart
			assert enrichablePair.output != null
		}
		0 * _
	}

	void "sets publishers from WikitextToCompaniesProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.PUBLISHER, value: PUBLISHER)
		ComicsTemplate comicsTemplate = new ComicsTemplate()
		Company company1 = Mock()
		Company company2 = Mock()

		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		1 * wikitextToCompaniesProcessorMock.process(PUBLISHER) >> Sets.newHashSet(company1, company2)
		0 * _
		comicsTemplate.publishers.size() == 2
		comicsTemplate.publishers.contains company1
		comicsTemplate.publishers.contains company2
	}

	void "sets comic series from WikitextToComicSeriesProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.SERIES, value: SERIES)
		ComicsTemplate comicsTemplate = new ComicsTemplate()
		ComicSeries comicSeries1 = Mock()
		ComicSeries comicSeries2 = Mock()

		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		1 * wikitextToComicSeriesProcessorMock.process(SERIES) >> Sets.newHashSet(comicSeries1, comicSeries2)
		0 * _
		comicsTemplate.comicSeries.size() == 2
		comicsTemplate.comicSeries.contains comicSeries1
		comicsTemplate.comicSeries.contains comicSeries2
	}

	void "passes ComicsTemplate to ComicsTemplatePublishedDatesEnrichingProcessor, when published part is found"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.PUBLISHED, value: PUBLISHED)
		ComicsTemplate comicsTemplate = new ComicsTemplate()

		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		1 * comicsTemplatePublishedDatesEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
			EnrichablePair<Template.Part, ComicSeriesTemplate> enrichablePair ->
				assert enrichablePair.input == templatePart
				assert enrichablePair.output != null
		}
		0 * _
	}

	void "passes ComicsTemplate to ComicsTemplatePublishedDatesEnrichingProcessor, when cover date part is found"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.COVER_DATE, value: PUBLISHED)
		ComicsTemplate comicsTemplate = new ComicsTemplate()

		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		1 * comicsTemplatePublishedDatesEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
			EnrichablePair<Template.Part, ComicSeriesTemplate> enrichablePair ->
				assert enrichablePair.input == templatePart
				assert enrichablePair.output != null
		}
		0 * _
	}

	void "sets number of pages"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.PAGES, value: PAGES_STRING)
		ComicsTemplate comicsTemplate = new ComicsTemplate()

		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		0 * _
		comicsTemplate.numberOfPages == PAGES_INTEGER
	}

	void "sets year from and year to from WikitextToYearRangeProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.YEAR, value: YEARS)
		ComicsTemplate comicsTemplate = new ComicsTemplate()
		YearRange yearRange = new YearRange(yearFrom: YEAR_FROM, yearTo: YEAR_TO)

		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		1 * wikitextToYearRangeProcessorMock.process(YEARS) >> yearRange
		0 * _
		comicsTemplate.yearFrom == YEAR_FROM
		comicsTemplate.yearTo == YEAR_TO
	}

	void "does not set year from and year to from WikitextToYearRangeProcessor, when value is already present"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.YEAR, value: STARDATES)
		ComicsTemplate comicsTemplate = new ComicsTemplate(yearFrom: YEAR_FROM)

		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		0 * _
		comicsTemplate.yearFrom == YEAR_FROM
	}

	void "sets stardate from and stardate to from WikitextToStardateRangeProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.STARDATE, value: STARDATES)
		ComicsTemplate comicsTemplate = new ComicsTemplate()
		StardateRange stardateRange = new StardateRange(stardateFrom: STARDATE_FROM, stardateTo: STARDATE_TO)

		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		1 * wikitextToStardateRangeProcessorMock.process(STARDATES) >> stardateRange
		0 * _
		comicsTemplate.stardateFrom == STARDATE_FROM
		comicsTemplate.stardateTo == STARDATE_TO
	}

	void "does not set year from and year to from WikitextToStardateRangeProcessor, when value is already present"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.STARDATE, value: STARDATES)
		ComicsTemplate comicsTemplate = new ComicsTemplate(stardateFrom: STARDATE_FROM)

		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		0 * _
		comicsTemplate.stardateFrom == STARDATE_FROM
	}

	void "sets references from ReferencesFromTemplatePartProcessor"() {
		given:
		Template.Part templatePart = new Template.Part(key: ComicsTemplateParameter.REFERENCE, value: REFERENCE)
		ComicsTemplate comicsTemplate = new ComicsTemplate()
		Reference reference1 = Mock()
		Reference reference2 = Mock()

		when:
		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(Lists.newArrayList(templatePart), comicsTemplate))

		then:
		1 * referencesFromTemplatePartProcessorMock.process(templatePart) >> Sets.newHashSet(reference1, reference2)
		0 * _
		comicsTemplate.references.size() == 2
		comicsTemplate.references.contains reference1
		comicsTemplate.references.contains reference2
	}

}
