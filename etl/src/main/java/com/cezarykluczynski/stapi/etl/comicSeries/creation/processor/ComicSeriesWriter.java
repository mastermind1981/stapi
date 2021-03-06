package com.cezarykluczynski.stapi.etl.comicSeries.creation.processor;


import com.cezarykluczynski.stapi.model.comicSeries.entity.ComicSeries;
import com.cezarykluczynski.stapi.model.comicSeries.repository.ComicSeriesRepository;
import com.cezarykluczynski.stapi.model.page.entity.PageAware;
import com.cezarykluczynski.stapi.model.page.service.DuplicateFilteringPreSavePageAwareFilter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComicSeriesWriter implements ItemWriter<ComicSeries> {

	private ComicSeriesRepository comicSeriesRepository;

	private DuplicateFilteringPreSavePageAwareFilter duplicateFilteringPreSavePageAwareProcessor;

	@Inject
	public ComicSeriesWriter(ComicSeriesRepository comicSeriesRepository,
			DuplicateFilteringPreSavePageAwareFilter duplicateFilteringPreSavePageAwareProcessor) {
		this.comicSeriesRepository = comicSeriesRepository;
		this.duplicateFilteringPreSavePageAwareProcessor = duplicateFilteringPreSavePageAwareProcessor;
	}

	@Override
	public void write(List<? extends ComicSeries> items) throws Exception {
		comicSeriesRepository.save(process(items));
	}

	private List<ComicSeries> process(List<? extends ComicSeries> comicSeriesList) {
		List<ComicSeries> comicSeriesListWithoutExtends = fromExtendsListToComicSeriesList(comicSeriesList);
		return filterDuplicates(comicSeriesListWithoutExtends);
	}

	private List<ComicSeries> fromExtendsListToComicSeriesList(List<? extends ComicSeries> comicSeriesList) {
		return comicSeriesList
				.stream()
				.map(pageAware -> (ComicSeries) pageAware)
				.collect(Collectors.toList());
	}

	private List<ComicSeries> filterDuplicates(List<ComicSeries> comicSeriesList) {
		return duplicateFilteringPreSavePageAwareProcessor.process(comicSeriesList.stream()
				.map(comicSeries -> (PageAware) comicSeries)
				.collect(Collectors.toList()), ComicSeries.class).stream()
				.map(pageAware -> (ComicSeries) pageAware)
				.collect(Collectors.toList());
	}

}
