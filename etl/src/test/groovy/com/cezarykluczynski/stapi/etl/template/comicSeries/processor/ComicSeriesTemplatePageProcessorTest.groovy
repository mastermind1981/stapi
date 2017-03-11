package com.cezarykluczynski.stapi.etl.template.comicSeries.processor

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair
import com.cezarykluczynski.stapi.etl.common.service.PageBindingService
import com.cezarykluczynski.stapi.etl.template.comicSeries.dto.ComicSeriesTemplate
import com.cezarykluczynski.stapi.etl.template.service.TemplateFinder
import com.cezarykluczynski.stapi.model.page.entity.Page as ModelPage
import com.cezarykluczynski.stapi.sources.mediawiki.api.enums.MediaWikiSource
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page
import com.cezarykluczynski.stapi.sources.mediawiki.dto.PageHeader
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template
import com.cezarykluczynski.stapi.util.ReflectionTestUtils
import com.cezarykluczynski.stapi.util.constant.PageTitle
import com.cezarykluczynski.stapi.util.constant.TemplateTitle
import com.cezarykluczynski.stapi.util.tool.LogicUtil
import com.google.common.collect.Lists
import spock.lang.Specification

class ComicSeriesTemplatePageProcessorTest extends Specification {

	private static final String TITLE = 'TITLE'
	private static final String TITLE_COMIC = 'TITLE (comic)'
	private static final String TITLE_PHOTONOVEL = 'TITLE (photonovel)'
	private static final Long PAGE_ID = 11L
	private static final MediaWikiSource SOURCES_MEDIA_WIKI_SOURCE = MediaWikiSource.MEMORY_ALPHA_EN
	private static final Boolean PHOTONOVEL_SERIES = LogicUtil.nextBoolean()

	private PageBindingService pageBindingServiceMock

	private TemplateFinder templateFinderMock

	private ComicSeriesTemplatePartsEnrichingProcessor comicSeriesTemplatePartsEnrichingProcessorMock

	private ComicSeriesTemplateFixedValuesEnrichingProcessor comicSeriesTemplateFixedValuesEnrichingProcessorMock

	private ComicSeriesTemplatePhotonovelSeriesProcessor comicSeriesTemplatePhotonovelSeriesProcessorMock

	private ComicSeriesTemplatePageProcessor comicSeriesTemplatePageProcessor

	void setup() {
		pageBindingServiceMock = Mock(PageBindingService)
		templateFinderMock = Mock(TemplateFinder)
		comicSeriesTemplatePartsEnrichingProcessorMock = Mock(ComicSeriesTemplatePartsEnrichingProcessor)
		comicSeriesTemplateFixedValuesEnrichingProcessorMock = Mock(ComicSeriesTemplateFixedValuesEnrichingProcessor)
		comicSeriesTemplatePhotonovelSeriesProcessorMock = Mock(ComicSeriesTemplatePhotonovelSeriesProcessor)
		comicSeriesTemplatePageProcessor = new ComicSeriesTemplatePageProcessor(pageBindingServiceMock, templateFinderMock,
				comicSeriesTemplatePartsEnrichingProcessorMock, comicSeriesTemplateFixedValuesEnrichingProcessorMock,
				comicSeriesTemplatePhotonovelSeriesProcessorMock)
	}

	void "returns null when page is 'DC Comics TNG timeline'"() {
		given:
		Page page = new Page(title: PageTitle.DC_COMICS_TNG_TIMELINE)

		when:
		ComicSeriesTemplate comicSeriesTemplate = comicSeriesTemplatePageProcessor.process(page)

		then:
		comicSeriesTemplate == null
	}

	void "cleans title with (comic)"() {
		given:
		Page page = new Page(title: TITLE_COMIC)

		when:
		ComicSeriesTemplate comicSeriesTemplate = comicSeriesTemplatePageProcessor.process(page)

		then:
		1 * templateFinderMock.findTemplate(page, TemplateTitle.SIDEBAR_COMIC_SERIES) >> Optional.empty()
		comicSeriesTemplate.title == TITLE
	}

	void "cleans title with (photonovel)"() {
		given:
		Page page = new Page(title: TITLE_PHOTONOVEL)

		when:
		ComicSeriesTemplate comicSeriesTemplate = comicSeriesTemplatePageProcessor.process(page)

		then:
		1 * templateFinderMock.findTemplate(page, TemplateTitle.SIDEBAR_COMIC_SERIES) >> Optional.empty()
		comicSeriesTemplate.title == TITLE
	}

	void "missing template results ComicSeriesTemplate with only the name and page"() {
		given:
		Page page = new Page(
				title: TITLE,
				pageId: PAGE_ID,
				mediaWikiSource: SOURCES_MEDIA_WIKI_SOURCE)
		ModelPage modelPage = new ModelPage()

		when:
		ComicSeriesTemplate comicSeriesTemplate = comicSeriesTemplatePageProcessor.process(page)

		then:
		1 * pageBindingServiceMock.fromPageToPageEntity(page) >> modelPage
		1 * comicSeriesTemplatePhotonovelSeriesProcessorMock.process(page) >> PHOTONOVEL_SERIES
		1 * comicSeriesTemplateFixedValuesEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
				EnrichablePair<ComicSeriesTemplate, ComicSeriesTemplate> enrichablePair ->
			assert enrichablePair.input == enrichablePair.output
		}
		1 * templateFinderMock.findTemplate(page, TemplateTitle.SIDEBAR_COMIC_SERIES) >> Optional.empty()
		0 * _
		comicSeriesTemplate.title == TITLE
		comicSeriesTemplate.page == modelPage
		comicSeriesTemplate.photonovelSeries == PHOTONOVEL_SERIES
		ReflectionTestUtils.getNumberOfNotNullFields(comicSeriesTemplate) == 5
	}

	void "sets productOfRedirect flag to true"() {
		given:
		Page page = new Page(
				title: TITLE,
				redirectPath: Lists.newArrayList(Mock(PageHeader)))
		ModelPage modelPage = new ModelPage()

		when:
		ComicSeriesTemplate comicSeriesTemplate = comicSeriesTemplatePageProcessor.process(page)

		then:
		1 * pageBindingServiceMock.fromPageToPageEntity(page) >> modelPage
		1 * comicSeriesTemplatePhotonovelSeriesProcessorMock.process(page)
		1 * comicSeriesTemplateFixedValuesEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
				EnrichablePair<ComicSeriesTemplate, ComicSeriesTemplate> enrichablePair ->
			assert enrichablePair.input == enrichablePair.output
		}
		1 * templateFinderMock.findTemplate(page, TemplateTitle.SIDEBAR_COMIC_SERIES) >> Optional.empty()
		comicSeriesTemplate.productOfRedirect
	}

	void "sets productOfRedirect flag to false"() {
		given:
		Page page = new Page(title: TITLE)
		ModelPage modelPage = new ModelPage()

		when:
		ComicSeriesTemplate comicSeriesTemplate = comicSeriesTemplatePageProcessor.process(page)

		then:
		1 * pageBindingServiceMock.fromPageToPageEntity(page) >> modelPage
		1 * comicSeriesTemplatePhotonovelSeriesProcessorMock.process(page)
		1 * comicSeriesTemplateFixedValuesEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
				EnrichablePair<ComicSeriesTemplate, ComicSeriesTemplate> enrichablePair ->
			assert enrichablePair.input == enrichablePair.output
		}
		1 * templateFinderMock.findTemplate(page, TemplateTitle.SIDEBAR_COMIC_SERIES) >> Optional.empty()
		!comicSeriesTemplate.productOfRedirect
	}

	void "when sidebar comic series is found, enriching processor is called"() {
		given:
		List<Template.Part> templatePartList = Lists.newArrayList(Mock(Template.Part))
		Page page = new Page(title: TITLE)
		Template sidebarComicSeriesTemplate = new Template(parts: templatePartList)

		when:
		comicSeriesTemplatePageProcessor.process(page)

		then:
		1 * pageBindingServiceMock.fromPageToPageEntity(page)
		1 * comicSeriesTemplatePhotonovelSeriesProcessorMock.process(page)
		1 * comicSeriesTemplateFixedValuesEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
				EnrichablePair<ComicSeriesTemplate, ComicSeriesTemplate> enrichablePair ->
			assert enrichablePair.input == enrichablePair.output
		}
		1 * templateFinderMock.findTemplate(page, TemplateTitle.SIDEBAR_COMIC_SERIES) >> Optional.of(sidebarComicSeriesTemplate)
		1 * comicSeriesTemplatePartsEnrichingProcessorMock.enrich(_) >> { EnrichablePair<List<Template.Part>, ComicSeriesTemplate> enrichablePair ->
			assert enrichablePair.input == templatePartList
			assert enrichablePair.output != null
		}
		0 * _
	}

}
