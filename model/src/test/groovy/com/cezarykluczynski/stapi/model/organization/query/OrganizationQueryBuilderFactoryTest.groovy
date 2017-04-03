package com.cezarykluczynski.stapi.model.organization.query

import com.cezarykluczynski.stapi.model.common.query.CachingStrategy
import org.springframework.data.jpa.repository.JpaContext
import spock.lang.Specification

class OrganizationQueryBuilderFactoryTest extends Specification {

	private JpaContext jpaContextMock

	private CachingStrategy cachingStrategyMock

	private OrganizationQueryBuilderFactory organizationQueryBuilderFactory

	void setup() {
		jpaContextMock = Mock(JpaContext)
		cachingStrategyMock = Mock(CachingStrategy)
	}

	void "OrganizationQueryBuilder is created"() {
		when:
		organizationQueryBuilderFactory = new OrganizationQueryBuilderFactory(jpaContextMock, cachingStrategyMock)

		then:
		organizationQueryBuilderFactory != null
	}

}
