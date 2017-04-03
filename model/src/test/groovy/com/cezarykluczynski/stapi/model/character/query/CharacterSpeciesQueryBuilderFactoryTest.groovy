package com.cezarykluczynski.stapi.model.character.query

import com.cezarykluczynski.stapi.model.common.query.CachingStrategy
import org.springframework.data.jpa.repository.JpaContext
import spock.lang.Specification

class CharacterSpeciesQueryBuilderFactoryTest extends Specification {

	private JpaContext jpaContextMock

	private CachingStrategy cachingStrategyMock

	private CharacterSpeciesQueryBuilderFactory characterSpeciesQueryBuilderFactory

	void setup() {
		jpaContextMock = Mock(JpaContext)
		cachingStrategyMock = Mock(CachingStrategy)
	}

	void "CharacterSpeciesQueryBuilder is created"() {
		when:
		characterSpeciesQueryBuilderFactory = new CharacterSpeciesQueryBuilderFactory(jpaContextMock, cachingStrategyMock)

		then:
		characterSpeciesQueryBuilderFactory != null
	}

}
