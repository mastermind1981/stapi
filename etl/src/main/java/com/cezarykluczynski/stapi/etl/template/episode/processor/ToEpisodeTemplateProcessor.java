package com.cezarykluczynski.stapi.etl.template.episode.processor;

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.dto.FixedValueHolder;
import com.cezarykluczynski.stapi.etl.common.service.PageBindingService;
import com.cezarykluczynski.stapi.etl.episode.creation.service.SeriesToEpisodeBindingService;
import com.cezarykluczynski.stapi.etl.template.common.linker.EpisodeLinkingWorkerComposite;
import com.cezarykluczynski.stapi.etl.template.episode.dto.EpisodeTemplate;
import com.cezarykluczynski.stapi.etl.template.service.TemplateFinder;
import com.cezarykluczynski.stapi.etl.util.TitleUtil;
import com.cezarykluczynski.stapi.etl.util.constant.CategoryName;
import com.cezarykluczynski.stapi.etl.util.constant.CategoryNames;
import com.cezarykluczynski.stapi.model.episode.entity.Episode;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.CategoryHeader;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template;
import com.cezarykluczynski.stapi.util.constant.TemplateName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ToEpisodeTemplateProcessor implements ItemProcessor<Page, EpisodeTemplate> {

	private EpisodeTemplateProcessor episodeTemplateProcessor;

	private EpisodeLinkingWorkerComposite episodeLinkingWorkerComposite;

	private PageBindingService pageBindingService;

	private SeriesToEpisodeBindingService seriesToEpisodeBindingService;

	private EpisodeTemplateEnrichingProcessorComposite episodeTemplateEnrichingProcessorComposite;

	private TemplateFinder templateFinder;

	private EpisodeTitleFixedValueProvider episodeTitleFixedValueProvider;

	@Inject
	public ToEpisodeTemplateProcessor(EpisodeTemplateProcessor episodeTemplateProcessor, EpisodeLinkingWorkerComposite episodeLinkingWorkerComposite,
			PageBindingService pageBindingService, SeriesToEpisodeBindingService seriesToEpisodeBindingService,
			EpisodeTemplateEnrichingProcessorComposite episodeTemplateEnrichingProcessorComposite, TemplateFinder templateFinder,
			EpisodeTitleFixedValueProvider episodeTitleFixedValueProvider) {
		this.episodeTemplateProcessor = episodeTemplateProcessor;
		this.episodeLinkingWorkerComposite = episodeLinkingWorkerComposite;
		this.pageBindingService = pageBindingService;
		this.seriesToEpisodeBindingService = seriesToEpisodeBindingService;
		this.episodeTemplateEnrichingProcessorComposite = episodeTemplateEnrichingProcessorComposite;
		this.templateFinder = templateFinder;
		this.episodeTitleFixedValueProvider = episodeTitleFixedValueProvider;
	}

	@Override
	public EpisodeTemplate process(Page item) throws Exception {
		Optional<Template> templateOptional = templateFinder.findTemplate(item, TemplateName.SIDEBAR_EPISODE);

		if (!isEpisodePage(item) || !templateOptional.isPresent()) {
			return null;
		}

		EpisodeTemplate episodeTemplate = episodeTemplateProcessor.process(templateOptional.get());

		if (episodeTemplate == null) {
			return null;
		}

		Episode episodeStub = episodeTemplate.getEpisodeStub();

		setTemplateValuesFromPage(episodeTemplate, item);
		linkPerformancesAndStaff(episodeStub, item);
		enrichTemplateWithPage(episodeTemplate, item);

		return episodeTemplate;
	}

	private void setTemplateValuesFromPage(EpisodeTemplate episodeTemplate, Page item) {
		episodeTemplate.setTitle(TitleUtil.getNameFromTitle(item.getTitle()));

		FixedValueHolder<String> titleFixedValueHolder = episodeTitleFixedValueProvider.getSearchedValue(episodeTemplate.getTitle());
		if (titleFixedValueHolder.isFound()) {
			episodeTemplate.setTitle(titleFixedValueHolder.getValue());
		}

		episodeTemplate.setPage(pageBindingService.fromPageToPageEntity(item));
		episodeTemplate.setSeries(seriesToEpisodeBindingService.mapCategoriesToSeries(item.getCategories()));
	}

	private void linkPerformancesAndStaff(Episode episodeStub, Page item) {
		episodeLinkingWorkerComposite.link(item, episodeStub);
	}

	private void enrichTemplateWithPage(EpisodeTemplate episodeTemplate, Page item) throws Exception {
		episodeTemplateEnrichingProcessorComposite.enrich(EnrichablePair.of(item, episodeTemplate));
	}

	private boolean isEpisodePage(Page source) {
		List<CategoryHeader> categoryHeaderList = source.getCategories();
		boolean hasEpisodeCategory = categoryHeaderList
				.stream()
				.map(CategoryHeader::getTitle)
				.anyMatch(this::isEpisodeCategory);

		boolean hasProductionListCategory = categoryHeaderList
				.stream()
				.map(CategoryHeader::getTitle)
				.anyMatch(this::isProductionList);

		return hasEpisodeCategory && !hasProductionListCategory;
	}

	private boolean isProductionList(String categoryName) {
		return CategoryName.PRODUCTION_LISTS.equals(categoryName);
	}

	private boolean isEpisodeCategory(String categoryName) {
		return CategoryNames.EPISODES.contains(categoryName);
	}

}
