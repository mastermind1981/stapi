package com.cezarykluczynski.stapi.server.species.reader;

import com.cezarykluczynski.stapi.client.v1.soap.SpeciesBaseRequest;
import com.cezarykluczynski.stapi.client.v1.soap.SpeciesBaseResponse;
import com.cezarykluczynski.stapi.client.v1.soap.SpeciesFullRequest;
import com.cezarykluczynski.stapi.client.v1.soap.SpeciesFullResponse;
import com.cezarykluczynski.stapi.model.species.entity.Species;
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper;
import com.cezarykluczynski.stapi.server.common.reader.BaseReader;
import com.cezarykluczynski.stapi.server.common.reader.FullReader;
import com.cezarykluczynski.stapi.server.common.validator.StaticValidator;
import com.cezarykluczynski.stapi.server.species.mapper.SpeciesBaseSoapMapper;
import com.cezarykluczynski.stapi.server.species.mapper.SpeciesFullSoapMapper;
import com.cezarykluczynski.stapi.server.species.query.SpeciesSoapQuery;
import com.google.common.collect.Iterables;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class SpeciesSoapReader implements BaseReader<SpeciesBaseRequest, SpeciesBaseResponse>,
		FullReader<SpeciesFullRequest, SpeciesFullResponse> {

	private SpeciesSoapQuery speciesSoapQuery;

	private SpeciesBaseSoapMapper speciesBaseSoapMapper;

	private SpeciesFullSoapMapper speciesFullSoapMapper;

	private PageMapper pageMapper;

	public SpeciesSoapReader(SpeciesSoapQuery speciesSoapQuery, SpeciesBaseSoapMapper speciesBaseSoapMapper,
			SpeciesFullSoapMapper speciesFullSoapMapper, PageMapper pageMapper) {
		this.speciesSoapQuery = speciesSoapQuery;
		this.speciesBaseSoapMapper = speciesBaseSoapMapper;
		this.speciesFullSoapMapper = speciesFullSoapMapper;
		this.pageMapper = pageMapper;
	}

	@Override
	public SpeciesBaseResponse readBase(SpeciesBaseRequest input) {
		Page<Species> speciesPage = speciesSoapQuery.query(input);
		SpeciesBaseResponse speciesResponse = new SpeciesBaseResponse();
		speciesResponse.setPage(pageMapper.fromPageToSoapResponsePage(speciesPage));
		speciesResponse.getSpecies().addAll(speciesBaseSoapMapper.mapBase(speciesPage.getContent()));
		return speciesResponse;
	}

	@Override
	public SpeciesFullResponse readFull(SpeciesFullRequest input) {
		StaticValidator.requireUid(input.getUid());
		Page<Species> speciesPage = speciesSoapQuery.query(input);
		SpeciesFullResponse speciesFullResponse = new SpeciesFullResponse();
		speciesFullResponse.setSpecies(speciesFullSoapMapper.mapFull(Iterables.getOnlyElement(speciesPage.getContent(), null)));
		return speciesFullResponse;
	}

}
