package com.cezarykluczynski.stapi.etl.template.movie.processor

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair
import com.cezarykluczynski.stapi.etl.common.service.PageSectionExtractor
import com.cezarykluczynski.stapi.etl.template.movie.dto.MovieTemplate
import com.cezarykluczynski.stapi.sources.mediawiki.api.WikitextApi
import com.cezarykluczynski.stapi.sources.mediawiki.api.dto.PageSection
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page
import com.google.common.collect.Lists
import spock.lang.Specification

class MovieTemplateTitleLanguagesEnrichingProcessorTest extends Specification {

	private static final String WIKITEXT = 'WIKITEXT'
	private static final String TITLE_BULGARIAN = 'TITLE_BULGARIAN'
	private static final String TITLE_CATALAN = 'TITLE_CATALAN'
	private static final String TITLE_CHINESE_TRADITIONAL = 'TITLE_CHINESE_TRADITIONAL'
	private static final String TITLE_GERMAN = 'TITLE_GERMAN'
	private static final String TITLE_ITALIAN = 'TITLE_ITALIAN'
	private static final String TITLE_JAPANESE = 'TITLE_JAPANESE'
	private static final String TITLE_POLISH = 'TITLE_POLISH'
	private static final String TITLE_RUSSIAN = 'TITLE_RUSSIAN'
	private static final String TITLE_SERBIAN = 'TITLE_SERBIAN'
	private static final String TITLE_SPANISH = 'TITLE_SPANISH'

	private WikitextApi wikitextApiMock

	private PageSectionExtractor pageSectionExtractorMock

	private MovieTemplateTitleLanguagesEnrichingProcessor movieTemplateTitleLanguagesEnrichingProcessor

	def setup() {
		wikitextApiMock = Mock(WikitextApi)
		pageSectionExtractorMock = Mock(PageSectionExtractor)
		movieTemplateTitleLanguagesEnrichingProcessor = new MovieTemplateTitleLanguagesEnrichingProcessor(
				wikitextApiMock, pageSectionExtractorMock)
	}

	def "gets links from last page section and puts them into episode template"() {
		given:
		PageSection pageSection = new PageSection(wikitext: WIKITEXT)
		List<PageSection> pageSectionList = Lists.newArrayList(pageSection)
		Page page = new Page(sections: pageSectionList)
		MovieTemplate movieTemplate = new MovieTemplate()

		when:
		movieTemplateTitleLanguagesEnrichingProcessor.enrich(EnrichablePair.of(page, movieTemplate))

		then:
		1 * pageSectionExtractorMock.extractLastPageSection(page) >> pageSection
		1 * wikitextApiMock.getPageTitlesFromWikitext(WIKITEXT) >> Lists.newArrayList(
				"bg:${TITLE_BULGARIAN}".toString(),
				"ca:${TITLE_CATALAN}".toString(),
				"zh-cn:${TITLE_CHINESE_TRADITIONAL}".toString(),
				"de:${TITLE_GERMAN}".toString(),
				"it:${TITLE_ITALIAN}".toString(),
				"ja:${TITLE_JAPANESE}".toString(),
				"pl:${TITLE_POLISH}".toString(),
				"ru:${TITLE_RUSSIAN}".toString(),
				"sr:${TITLE_SERBIAN}".toString(),
				"es:${TITLE_SPANISH}".toString(),
		)
		movieTemplate.titleBulgarian == TITLE_BULGARIAN
		movieTemplate.titleCatalan == TITLE_CATALAN
		movieTemplate.titleChineseTraditional == TITLE_CHINESE_TRADITIONAL
		movieTemplate.titleGerman == TITLE_GERMAN
		movieTemplate.titleItalian == TITLE_ITALIAN
		movieTemplate.titleJapanese == TITLE_JAPANESE
		movieTemplate.titlePolish == TITLE_POLISH
		movieTemplate.titleRussian == TITLE_RUSSIAN
		movieTemplate.titleSerbian == TITLE_SERBIAN
		movieTemplate.titleSpanish == TITLE_SPANISH
	}

	def "tolerates empty section list"() {
		given:
		Page page = new Page(sections: Lists.newArrayList())
		MovieTemplate movieTemplate = new MovieTemplate()

		when:
		movieTemplateTitleLanguagesEnrichingProcessor.enrich(EnrichablePair.of(page, movieTemplate))

		then:
		1 * pageSectionExtractorMock.extractLastPageSection(page) >> null
		0 * _
		notThrown(Throwable)
		movieTemplate.titleBulgarian == null
		movieTemplate.titleCatalan == null
		movieTemplate.titleChineseTraditional == null
		movieTemplate.titleGerman == null
		movieTemplate.titleItalian == null
		movieTemplate.titleJapanese == null
		movieTemplate.titlePolish == null
		movieTemplate.titleRussian == null
		movieTemplate.titleSerbian == null
		movieTemplate.titleSpanish == null
	}

}
