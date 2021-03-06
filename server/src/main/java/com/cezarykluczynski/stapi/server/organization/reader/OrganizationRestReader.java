package com.cezarykluczynski.stapi.server.organization.reader;

import com.cezarykluczynski.stapi.client.v1.rest.model.OrganizationBaseResponse;
import com.cezarykluczynski.stapi.client.v1.rest.model.OrganizationFullResponse;
import com.cezarykluczynski.stapi.model.organization.entity.Organization;
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper;
import com.cezarykluczynski.stapi.server.common.reader.BaseReader;
import com.cezarykluczynski.stapi.server.common.reader.FullReader;
import com.cezarykluczynski.stapi.server.common.validator.StaticValidator;
import com.cezarykluczynski.stapi.server.organization.dto.OrganizationRestBeanParams;
import com.cezarykluczynski.stapi.server.organization.mapper.OrganizationBaseRestMapper;
import com.cezarykluczynski.stapi.server.organization.mapper.OrganizationFullRestMapper;
import com.cezarykluczynski.stapi.server.organization.query.OrganizationRestQuery;
import com.google.common.collect.Iterables;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class OrganizationRestReader implements BaseReader<OrganizationRestBeanParams, OrganizationBaseResponse>,
		FullReader<String, OrganizationFullResponse> {

	private OrganizationRestQuery organizationRestQuery;

	private OrganizationBaseRestMapper organizationBaseRestMapper;

	private OrganizationFullRestMapper organizationFullRestMapper;

	private PageMapper pageMapper;

	@Inject
	public OrganizationRestReader(OrganizationRestQuery organizationRestQuery, OrganizationBaseRestMapper organizationBaseRestMapper,
			OrganizationFullRestMapper organizationFullRestMapper, PageMapper pageMapper) {
		this.organizationRestQuery = organizationRestQuery;
		this.organizationBaseRestMapper = organizationBaseRestMapper;
		this.organizationFullRestMapper = organizationFullRestMapper;
		this.pageMapper = pageMapper;
	}

	@Override
	public OrganizationBaseResponse readBase(OrganizationRestBeanParams input) {
		Page<Organization> organizationPage = organizationRestQuery.query(input);
		OrganizationBaseResponse organizationResponse = new OrganizationBaseResponse();
		organizationResponse.setPage(pageMapper.fromPageToRestResponsePage(organizationPage));
		organizationResponse.getOrganizations().addAll(organizationBaseRestMapper.mapBase(organizationPage.getContent()));
		return organizationResponse;
	}

	@Override
	public OrganizationFullResponse readFull(String uid) {
		StaticValidator.requireUid(uid);
		OrganizationRestBeanParams organizationRestBeanParams = new OrganizationRestBeanParams();
		organizationRestBeanParams.setUid(uid);
		Page<com.cezarykluczynski.stapi.model.organization.entity.Organization> organizationPage = organizationRestQuery
				.query(organizationRestBeanParams);
		OrganizationFullResponse organizationResponse = new OrganizationFullResponse();
		organizationResponse.setOrganization(organizationFullRestMapper
				.mapFull(Iterables.getOnlyElement(organizationPage.getContent(), null)));
		return organizationResponse;
	}
}
