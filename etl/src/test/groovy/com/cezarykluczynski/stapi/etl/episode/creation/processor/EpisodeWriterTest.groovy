package com.cezarykluczynski.stapi.etl.episode.creation.processor

import com.cezarykluczynski.stapi.model.episode.entity.Episode
import com.cezarykluczynski.stapi.model.episode.repository.EpisodeRepository
import com.google.common.collect.Lists
import spock.lang.Specification

class EpisodeWriterTest extends Specification {

	private EpisodeRepository episodeRepositoryMock

	private EpisodeWriter episodeWriter

	void setup() {
		episodeRepositoryMock = Mock()
		episodeWriter = new EpisodeWriter(episodeRepositoryMock)
	}

	void "writes all entities using repository"() {
		given:
		Episode episode = new Episode()
		List<Episode> episodeList = Lists.newArrayList(episode)

		when:
		episodeWriter.write(episodeList)

		then:
		1 * episodeRepositoryMock.save(episodeList)
		0 * _
	}

}
