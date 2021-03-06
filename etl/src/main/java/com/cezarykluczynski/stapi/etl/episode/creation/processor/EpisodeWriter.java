package com.cezarykluczynski.stapi.etl.episode.creation.processor;

import com.cezarykluczynski.stapi.model.episode.entity.Episode;
import com.cezarykluczynski.stapi.model.episode.repository.EpisodeRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class EpisodeWriter implements ItemWriter<Episode> {

	private EpisodeRepository episodeRepository;

	@Inject
	public EpisodeWriter(EpisodeRepository episodeRepository) {
		this.episodeRepository = episodeRepository;
	}

	@Override
	public void write(List<? extends Episode> items) throws Exception {
		episodeRepository.save(items);
	}

}
