package com.cezarykluczynski.stapi.etl.util.constant;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class StepNames {

	public static final Map<String, List<String>> JOB_STEPS = Maps.newHashMap();

	static {
		JOB_STEPS.put(JobName.JOB_CREATE, Lists.newArrayList(
				StepName.CREATE_SERIES,
				StepName.CREATE_PERFORMERS,
				StepName.CREATE_STAFF,
				StepName.CREATE_CHARACTERS,
				StepName.CREATE_EPISODES,
				StepName.CREATE_MOVIES
		));
	}

}