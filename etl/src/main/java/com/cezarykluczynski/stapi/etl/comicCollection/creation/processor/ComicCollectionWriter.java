package com.cezarykluczynski.stapi.etl.comicCollection.creation.processor;

import com.cezarykluczynski.stapi.model.comicCollection.entity.ComicCollection;
import com.cezarykluczynski.stapi.model.comicCollection.repository.ComicCollectionRepository;
import com.cezarykluczynski.stapi.model.page.entity.PageAware;
import com.cezarykluczynski.stapi.model.page.service.DuplicateFilteringPreSavePageAwareFilter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComicCollectionWriter implements ItemWriter<ComicCollection> {

	private ComicCollectionRepository comicCollectionRepository;

	private DuplicateFilteringPreSavePageAwareFilter duplicateFilteringPreSavePageAwareProcessor;

	@Inject
	public ComicCollectionWriter(ComicCollectionRepository comicCollectionRepository,
			DuplicateFilteringPreSavePageAwareFilter duplicateFilteringPreSavePageAwareProcessor) {
		this.comicCollectionRepository = comicCollectionRepository;
		this.duplicateFilteringPreSavePageAwareProcessor = duplicateFilteringPreSavePageAwareProcessor;
	}

	@Override
	public void write(List<? extends ComicCollection> items) throws Exception {
		comicCollectionRepository.save(process(items));
	}

	private List<ComicCollection> process(List<? extends ComicCollection> comicCollectionList) {
		List<ComicCollection> comicCollectionListWithoutExtends = fromExtendsListToComicCollectionList(comicCollectionList);
		return filterDuplicates(comicCollectionListWithoutExtends);
	}

	private List<ComicCollection> fromExtendsListToComicCollectionList(List<? extends ComicCollection> comicCollectionList) {
		return comicCollectionList
				.stream()
				.map(pageAware -> (ComicCollection) pageAware)
				.collect(Collectors.toList());
	}

	private List<ComicCollection> filterDuplicates(List<ComicCollection> comicCollectionList) {
		return duplicateFilteringPreSavePageAwareProcessor.process(comicCollectionList.stream()
				.map(comicCollection -> (PageAware) comicCollection)
				.collect(Collectors.toList()), ComicCollection.class).stream()
				.map(pageAware -> (ComicCollection) pageAware)
				.collect(Collectors.toList());
	}

}
