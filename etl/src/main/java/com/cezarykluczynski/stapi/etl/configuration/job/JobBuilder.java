package com.cezarykluczynski.stapi.etl.configuration.job;

import com.cezarykluczynski.stapi.etl.configuration.job.properties.StepProperties;
import com.cezarykluczynski.stapi.etl.configuration.job.properties.StepToStepPropertiesProvider;
import com.cezarykluczynski.stapi.etl.configuration.job.service.JobCompletenessDecider;
import com.cezarykluczynski.stapi.etl.util.constant.JobName;
import com.cezarykluczynski.stapi.etl.util.constant.StepNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JobBuilder {

	private ApplicationContext applicationContext;

	private JobBuilderFactory jobBuilderFactory;

	private StepConfigurationValidator stepConfigurationValidator;

	private JobCompletenessDecider jobCompletenessDecider;

	private StepToStepPropertiesProvider stepToStepPropertiesProvider;

	@Inject
	public JobBuilder(ApplicationContext applicationContext, JobBuilderFactory jobBuilderFactory,
			StepConfigurationValidator stepConfigurationValidator, JobCompletenessDecider jobCompletenessDecider,
			StepToStepPropertiesProvider stepToStepPropertiesProvider) {
		this.applicationContext = applicationContext;
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepConfigurationValidator = stepConfigurationValidator;
		this.jobCompletenessDecider = jobCompletenessDecider;
		this.stepToStepPropertiesProvider = stepToStepPropertiesProvider;
	}

	public synchronized Job build() {
		stepConfigurationValidator.validate();

		if (jobCompletenessDecider.isJobCompleted(JobName.JOB_CREATE)) {
			return null;
		}

		org.springframework.batch.core.job.builder.JobBuilder jobBuilder = jobBuilderFactory.get(JobName.JOB_CREATE);
		SimpleJobBuilder simpleJobBuilder = new SimpleJobBuilder(jobBuilder);

		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow1");
		Map<String, StepProperties> stepPropertiesMap = stepToStepPropertiesProvider.provide();

		List<String> stepNameList = StepNames.JOB_STEPS.get(JobName.JOB_CREATE);
		boolean fromCalled = false;
		boolean allStepsAreDisabled = true;

		for (String stepName : stepNameList) {
			if (stepPropertiesMap.get(stepName).isEnabled()) {
				allStepsAreDisabled = false;
				Step step = applicationContext.getBean(stepName, Step.class);
				if (fromCalled) {
					flowBuilder.next(step);
				} else {
					fromCalled = true;
					flowBuilder.from(step);
				}
			}
		}

		return allStepsAreDisabled ? null : simpleJobBuilder
				.split(applicationContext.getBean(TaskExecutor.class))
				.add(flowBuilder.build())
				.end()
				.build();
	}

}

