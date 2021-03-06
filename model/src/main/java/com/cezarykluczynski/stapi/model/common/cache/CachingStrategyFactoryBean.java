package com.cezarykluczynski.stapi.model.common.cache;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class CachingStrategyFactoryBean implements FactoryBean<CachingStrategy> {

	private final CacheProperties cacheProperties;

	@Inject
	public CachingStrategyFactoryBean(CacheProperties cacheProperties) {
		this.cacheProperties = cacheProperties;
	}

	@Override
	public CachingStrategy getObject() throws Exception {
		CachingStrategyType cachingStrategyType = cacheProperties.getCachingStrategyType();

		switch (cachingStrategyType) {
			case NO_CACHE:
				return new NoCacheCachingStrategy();
			case CACHE_FULL_ENTITIES:
				return new FullEntityCachingStrategy();
			case CACHE_ALL:
				return new CacheAllCachingStrategy();
			default:
				throw new RuntimeException(String.format("Cannot map %s to CachingStrategy", cachingStrategyType));
		}
	}

	@Override
	public Class<?> getObjectType() {
		return CachingStrategy.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
