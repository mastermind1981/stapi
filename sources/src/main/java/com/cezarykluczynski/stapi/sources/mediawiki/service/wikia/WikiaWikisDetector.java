package com.cezarykluczynski.stapi.sources.mediawiki.service.wikia;

import com.cezarykluczynski.stapi.sources.mediawiki.api.enums.MediaWikiSource;
import com.cezarykluczynski.stapi.sources.mediawiki.configuration.MediaWikiSourcesProperties;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

@Service
public class WikiaWikisDetector {

	private Map<MediaWikiSource, Boolean> areWikiaWikis = Maps.newHashMap();

	private MediaWikiSourcesProperties mediaWikiSourcesProperties;

	private WikiaUrlDetector wikiaUrlDetector;

	@Inject
	public WikiaWikisDetector(MediaWikiSourcesProperties mediaWikiSourcesProperties, WikiaUrlDetector wikiaUrlDetector) {
		this.mediaWikiSourcesProperties = mediaWikiSourcesProperties;
		this.wikiaUrlDetector = wikiaUrlDetector;
		configure();
	}

	private void configure() {
		areWikiaWikis.put(MediaWikiSource.MEMORY_ALPHA_EN, wikiaUrlDetector
				.isWikiaWikiUrl(mediaWikiSourcesProperties.getMemoryAlphaEn().getApiUrl()));
		areWikiaWikis.put(MediaWikiSource.MEMORY_BETA_EN, wikiaUrlDetector
				.isWikiaWikiUrl(mediaWikiSourcesProperties.getMemoryBetaEn().getApiUrl()));
	}

	public boolean isWikiaWiki(MediaWikiSource mediaWikiSource) {
		return areWikiaWikis.containsKey(mediaWikiSource) && areWikiaWikis.get(mediaWikiSource);
	}

}
