package com.cezarykluczynski.stapi.etl.book.creation.service;

import com.cezarykluczynski.stapi.etl.common.service.CategorySortingService;
import com.cezarykluczynski.stapi.etl.template.common.service.PageFilter;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Set;

@Service
public class BookPageFilter implements PageFilter {

	private static final Set<String> INVALID_TITLES = Sets.newHashSet("Star Trek Reader", "Schuster & Schuster",
			"Star Trek Roleplaying Game (Last Unicorn)", "Starlog photo guidebook Special Effects", "The Best of Star Trek (Titan)",
			"Star Trek: Federation Gift Pak", "To Boldly Go: Rare Photos from the TOS Soundstage", "These Are the Voyages: TOS",
			"Star Trek Emissary Gift Set", "Captain Sulu Adventures", "The Best of Trek", "Stardate (magazine)");

	private final CategorySortingService categorySortingService;

	@Inject
	public BookPageFilter(CategorySortingService categorySortingService) {
		this.categorySortingService = categorySortingService;
	}

	@Override
	public boolean shouldBeFilteredOut(Page page) {
		if (!page.getRedirectPath().isEmpty() || categorySortingService.isSortedOnTopOfAnyCategory(page)
				|| INVALID_TITLES.contains(page.getTitle())) {
			return true;
		}

		return false;
	}

}
