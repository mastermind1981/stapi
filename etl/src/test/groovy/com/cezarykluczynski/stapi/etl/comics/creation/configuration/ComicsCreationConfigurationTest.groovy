package com.cezarykluczynski.stapi.etl.comics.creation.configuration

import com.cezarykluczynski.stapi.etl.comics.creation.processor.ComicsReader
import com.cezarykluczynski.stapi.etl.common.configuration.AbstractCreationConfigurationTest
import com.cezarykluczynski.stapi.etl.configuration.job.service.StepCompletenessDecider
import com.cezarykluczynski.stapi.etl.util.constant.CategoryTitle
import com.cezarykluczynski.stapi.etl.util.constant.JobName
import com.cezarykluczynski.stapi.etl.util.constant.StepName
import com.cezarykluczynski.stapi.sources.mediawiki.api.CategoryApi
import com.cezarykluczynski.stapi.sources.mediawiki.api.enums.MediaWikiSource
import org.springframework.context.ApplicationContext

class ComicsCreationConfigurationTest extends AbstractCreationConfigurationTest {

	private static final String TITLE_COMICS = 'TITLE_COMICS'
	private static final String TITLE_PHOTONOVEL = 'TITLE_PHOTONOVEL'

	private ApplicationContext applicationContextMock

	private CategoryApi categoryApiMock

	private StepCompletenessDecider jobCompletenessDeciderMock

	private ComicsCreationConfiguration comicsCreationConfiguration

	void setup() {
		applicationContextMock = Mock()
		categoryApiMock = Mock()
		jobCompletenessDeciderMock = Mock()
		comicsCreationConfiguration = new ComicsCreationConfiguration(
				applicationContext: applicationContextMock,
				categoryApi: categoryApiMock,
				stepCompletenessDecider: jobCompletenessDeciderMock)
	}

	void "ComicsReader is created with all pages when step is not completed"() {
		when:
		ComicsReader comicsReader = comicsCreationConfiguration.comicsReader()
		List<String> categoryHeaderTitleList = readerToList(comicsReader)

		then:
		1 * jobCompletenessDeciderMock.isStepComplete(JobName.JOB_CREATE, StepName.CREATE_COMICS) >> false
		1 * categoryApiMock.getPages(CategoryTitle.COMICS, MediaWikiSource.MEMORY_ALPHA_EN) >> createListWithPageHeaderTitle(TITLE_COMICS)
		1 * categoryApiMock.getPages(CategoryTitle.PHOTONOVELS, MediaWikiSource.MEMORY_ALPHA_EN) >> createListWithPageHeaderTitle(TITLE_PHOTONOVEL)
		0 * _
		categoryHeaderTitleList.contains TITLE_COMICS
		categoryHeaderTitleList.contains TITLE_PHOTONOVEL
	}

	void "ComicsReader is created with no pages when step is completed"() {
		when:
		ComicsReader comicsReader = comicsCreationConfiguration.comicsReader()
		List<String> categoryHeaderTitleList = readerToList(comicsReader)

		then:
		1 * jobCompletenessDeciderMock.isStepComplete(JobName.JOB_CREATE, StepName.CREATE_COMICS) >> true
		0 * _
		categoryHeaderTitleList.empty
	}

}
