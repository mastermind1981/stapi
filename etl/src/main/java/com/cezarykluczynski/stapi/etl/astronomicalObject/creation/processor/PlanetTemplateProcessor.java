package com.cezarykluczynski.stapi.etl.astronomicalObject.creation.processor;

import com.cezarykluczynski.stapi.etl.template.planet.dto.PlanetTemplate;
import com.cezarykluczynski.stapi.etl.template.planet.mapper.AstronomicalObjectTypeMapper;
import com.cezarykluczynski.stapi.model.astronomicalObject.entity.AstronomicalObject;
import com.cezarykluczynski.stapi.model.common.service.UidGenerator;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class PlanetTemplateProcessor implements ItemProcessor<PlanetTemplate, AstronomicalObject> {

	private UidGenerator uidGenerator;

	private AstronomicalObjectTypeMapper astronomicalObjectTypeMapper;

	@Inject
	public PlanetTemplateProcessor(UidGenerator uidGenerator, AstronomicalObjectTypeMapper astronomicalObjectTypeMapper) {
		this.uidGenerator = uidGenerator;
		this.astronomicalObjectTypeMapper = astronomicalObjectTypeMapper;
	}

	@Override
	public AstronomicalObject process(PlanetTemplate item) throws Exception {
		if (item.isProductOfRedirect()) {
			return null;
		}

		AstronomicalObject astronomicalObject = new AstronomicalObject();
		astronomicalObject.setName(item.getName());
		astronomicalObject.setUid(uidGenerator.generateFromPage(item.getPage(), AstronomicalObject.class));
		astronomicalObject.setPage(item.getPage());
		astronomicalObject.setAstronomicalObjectType(astronomicalObjectTypeMapper.fromEtlToModel(item.getAstronomicalObjectType()));
		return astronomicalObject;
	}

}
