package com.cezarykluczynski.stapi.etl.bookSeries.creation.processor;


import com.cezarykluczynski.stapi.model.bookSeries.entity.BookSeries;
import com.cezarykluczynski.stapi.model.bookSeries.repository.BookSeriesRepository;
import com.cezarykluczynski.stapi.model.page.entity.PageAware;
import com.cezarykluczynski.stapi.model.page.service.DuplicateFilteringPreSavePageAwareFilter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookSeriesWriter implements ItemWriter<BookSeries> {

	private BookSeriesRepository bookSeriesRepository;

	private DuplicateFilteringPreSavePageAwareFilter duplicateFilteringPreSavePageAwareProcessor;

	@Inject
	public BookSeriesWriter(BookSeriesRepository bookSeriesRepository,
			DuplicateFilteringPreSavePageAwareFilter duplicateFilteringPreSavePageAwareProcessor) {
		this.bookSeriesRepository = bookSeriesRepository;
		this.duplicateFilteringPreSavePageAwareProcessor = duplicateFilteringPreSavePageAwareProcessor;
	}

	@Override
	public void write(List<? extends BookSeries> items) throws Exception {
		bookSeriesRepository.save(process(items));
	}

	private List<BookSeries> process(List<? extends BookSeries> bookSeriesList) {
		List<BookSeries> bookSeriesListWithoutExtends = fromExtendsListToBookSeriesList(bookSeriesList);
		return filterDuplicates(bookSeriesListWithoutExtends);
	}

	private List<BookSeries> fromExtendsListToBookSeriesList(List<? extends BookSeries> bookSeriesList) {
		return bookSeriesList
				.stream()
				.map(pageAware -> (BookSeries) pageAware)
				.collect(Collectors.toList());
	}

	private List<BookSeries> filterDuplicates(List<BookSeries> bookSeriesList) {
		return duplicateFilteringPreSavePageAwareProcessor.process(bookSeriesList.stream()
				.map(bookSeries -> (PageAware) bookSeries)
				.collect(Collectors.toList()), BookSeries.class).stream()
				.map(pageAware -> (BookSeries) pageAware)
				.collect(Collectors.toList());
	}

}
