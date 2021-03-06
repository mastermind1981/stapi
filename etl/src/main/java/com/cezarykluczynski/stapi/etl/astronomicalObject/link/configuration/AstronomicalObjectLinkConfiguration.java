package com.cezarykluczynski.stapi.etl.astronomicalObject.link.configuration;

import com.cezarykluczynski.stapi.etl.astronomicalObject.link.processor.AstronomicalObjectLinkReader;
import com.cezarykluczynski.stapi.etl.configuration.job.properties.StepsProperties;
import com.cezarykluczynski.stapi.etl.configuration.job.service.StepCompletenessDecider;
import com.cezarykluczynski.stapi.model.astronomicalObject.repository.AstronomicalObjectRepository;
import com.google.common.collect.ImmutableMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import javax.inject.Inject;

@Configuration
public class AstronomicalObjectLinkConfiguration {

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private StepCompletenessDecider stepCompletenessDecider;

	@Inject
	private AstronomicalObjectRepository astronomicalObjectRepository;

	@Inject
	private StepsProperties stepsProperties;

	@Bean
	public AstronomicalObjectLinkReader astronomicalObjectLinkReader() throws Exception {
		AstronomicalObjectLinkReader astronomicalObjectLinkReader = new AstronomicalObjectLinkReader();
		astronomicalObjectLinkReader.setPageSize(stepsProperties.getLinkAstronomicalObjects().getCommitInterval());
		astronomicalObjectLinkReader.setRepository(astronomicalObjectRepository);
		astronomicalObjectLinkReader.setSort(ImmutableMap.of("id", Sort.Direction.ASC));
		astronomicalObjectLinkReader.setMethodName("findAll");
		astronomicalObjectLinkReader.afterPropertiesSet();
		return astronomicalObjectLinkReader;
	}

}
