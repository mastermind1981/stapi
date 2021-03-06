package com.cezarykluczynski.stapi.etl.location.creation.service;

import com.cezarykluczynski.stapi.etl.common.service.CategoryFinder;
import com.cezarykluczynski.stapi.etl.template.common.service.PageFilter;
import com.cezarykluczynski.stapi.etl.util.constant.CategoryTitle;
import com.cezarykluczynski.stapi.etl.util.constant.CategoryTitles;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
@Slf4j
public class LocationPageFilter implements PageFilter {

	private final CategoryFinder categoryFinder;

	private final LocationNameFilter locationNameFilter;

	@Inject
	public LocationPageFilter(CategoryFinder categoryFinder, LocationNameFilter locationNameFilter) {
		this.categoryFinder = categoryFinder;
		this.locationNameFilter = locationNameFilter;
	}

	@Override
	public boolean shouldBeFilteredOut(Page page) {
		if (!page.getRedirectPath().isEmpty()) {
			return true;
		}

		if (categoryFinder.hasAnyCategory(page, CategoryTitles.ORGANIZATIONS)
				|| categoryFinder.hasAnyCategory(page, Lists.newArrayList(CategoryTitle.LISTS))) {
			return true;
		}

		String locationName = page.getTitle();
		return Boolean.FALSE.equals(locationNameFilter.isLocation(locationName));
	}

}
