package com.cezarykluczynski.stapi.etl.template.comics.processor;

import com.cezarykluczynski.stapi.etl.comicStrip.creation.service.ComicStripCandidatePageGatheringService;
import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.processor.CategoryTitlesExtractingProcessor;
import com.cezarykluczynski.stapi.etl.common.service.PageBindingService;
import com.cezarykluczynski.stapi.etl.template.comics.dto.ComicsTemplate;
import com.cezarykluczynski.stapi.etl.template.service.TemplateFinder;
import com.cezarykluczynski.stapi.etl.util.constant.CategoryName;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template;
import com.cezarykluczynski.stapi.util.constant.PageName;
import com.cezarykluczynski.stapi.util.constant.TemplateName;
import com.google.common.collect.Sets;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

@Service
public class ComicsTemplatePageProcessor implements ItemProcessor<Page, ComicsTemplate> {

	private static final Set<String> INVALID_TITLES = Sets.newHashSet(PageName.COMICS, PageName.PHOTONOVELS);

	private CategoryTitlesExtractingProcessor categoryTitlesExtractingProcessor;

	private ComicStripCandidatePageGatheringService comicStripCandidatePageGatheringService;

	private PageBindingService pageBindingService;

	private TemplateFinder templateFinder;

	private ComicsTemplateFixedValuesEnrichingProcessor comicsTemplateFixedValuesEnrichingProcessor;

	private ComicsTemplatePartsEnrichingProcessor comicsTemplatePartsEnrichingProcessor;

	@Inject
	public ComicsTemplatePageProcessor(CategoryTitlesExtractingProcessor categoryTitlesExtractingProcessor,
			ComicStripCandidatePageGatheringService comicStripCandidatePageGatheringService, PageBindingService pageBindingService,
			TemplateFinder templateFinder, ComicsTemplateFixedValuesEnrichingProcessor comicsTemplateFixedValuesEnrichingProcessor,
			ComicsTemplatePartsEnrichingProcessor comicsTemplatePartsEnrichingProcessor) {
		this.categoryTitlesExtractingProcessor = categoryTitlesExtractingProcessor;
		this.comicStripCandidatePageGatheringService = comicStripCandidatePageGatheringService;
		this.pageBindingService = pageBindingService;
		this.templateFinder = templateFinder;
		this.comicsTemplateFixedValuesEnrichingProcessor = comicsTemplateFixedValuesEnrichingProcessor;
		this.comicsTemplatePartsEnrichingProcessor = comicsTemplatePartsEnrichingProcessor;
	}

	@Override
	public ComicsTemplate process(Page item) throws Exception {
		if (shouldBeFilteredOut(item)) {
			return null;
		}

		Optional<Template> sidebarComicStropTemplateOptional = templateFinder.findTemplate(item, TemplateName.SIDEBAR_COMIC_STRIP);

		if (sidebarComicStropTemplateOptional.isPresent()) {
			comicStripCandidatePageGatheringService.addCandidate(item);
			return null;
		}

		ComicsTemplate comicsTemplate = new ComicsTemplate();
		comicsTemplate.setTitle(item.getTitle());
		comicsTemplate.setPage(pageBindingService.fromPageToPageEntity(item));
		comicsTemplate.setProductOfRedirect(!item.getRedirectPath().isEmpty());
		comicsTemplate.setPhotonovel(isPhotonovel(item));

		comicsTemplateFixedValuesEnrichingProcessor.enrich(EnrichablePair.of(comicsTemplate, comicsTemplate));

		Optional<Template> sidebarComicsTemplateOptional = templateFinder.findTemplate(item, TemplateName.SIDEBAR_COMIC);
		if (!sidebarComicsTemplateOptional.isPresent()) {
			return comicsTemplate;
		}

		Template sidebarComicsTemplate = sidebarComicsTemplateOptional.get();

		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(sidebarComicsTemplate.getParts(), comicsTemplate));

		return comicsTemplate;
	}

	private boolean shouldBeFilteredOut(Page item) {
		return INVALID_TITLES.contains(item.getTitle()) || categoryTitlesExtractingProcessor.process(item.getCategories())
				.contains(CategoryName.STAR_TREK_SERIES_MAGAZINES);
	}

	private boolean isPhotonovel(Page item) {
		return categoryTitlesExtractingProcessor.process(item.getCategories()).stream().anyMatch(CategoryName.PHOTONOVELS::equals);
	}

}