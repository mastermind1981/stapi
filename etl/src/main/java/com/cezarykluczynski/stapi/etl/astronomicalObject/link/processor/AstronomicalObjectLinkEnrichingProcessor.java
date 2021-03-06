package com.cezarykluczynski.stapi.etl.astronomicalObject.link.processor;

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.processor.ItemEnrichingProcessor;
import com.cezarykluczynski.stapi.model.astronomicalObject.entity.AstronomicalObject;
import com.cezarykluczynski.stapi.model.astronomicalObject.entity.enums.AstronomicalObjectType;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AstronomicalObjectLinkEnrichingProcessor implements ItemEnrichingProcessor<EnrichablePair<AstronomicalObject, AstronomicalObject>> {

	private static final Map<AstronomicalObjectType, Integer> ASTRONOMICAL_OBJECTS_SIZE_MAP = Maps.newHashMap();

	static {
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.COMET, 10);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.ASTEROID, 20);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.ASTEROIDAL_MOON, 30);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.MOON, 30);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.M_CLASS_MOON, 30);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.ASTEROID_BELT, 40);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.PLANETOID, 50);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.D_CLASS_PLANETOID, 50);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.PLANET, 60);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.D_CLASS_PLANET, 60);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.H_CLASS_PLANET, 60);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.GAS_GIANT_PLANET, 60);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.K_CLASS_PLANET, 60);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.L_CLASS_PLANET, 60);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.M_CLASS_PLANET, 60);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.Y_CLASS_PLANET, 60);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.ROGUE_PLANET, 60);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.ARTIFICIAL_PLANET, 60);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.QUASAR, 70);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.STAR, 70);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.STAR_SYSTEM, 80);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.NEBULA, 90);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.CLUSTER, 100);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.CONSTELLATION, 110);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.REGION, 120);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.SECTOR, 130);
		ASTRONOMICAL_OBJECTS_SIZE_MAP.put(AstronomicalObjectType.GALAXY, 140);
	}

	@Override
	public void enrich(EnrichablePair<AstronomicalObject, AstronomicalObject> enrichablePair) throws Exception {
		AstronomicalObject subject = enrichablePair.getOutput();
		AstronomicalObject locationCandidate = enrichablePair.getInput();

		if (locationCandidate == null) {
			return;
		}

		Integer locationCandidateSize = ASTRONOMICAL_OBJECTS_SIZE_MAP.get(locationCandidate.getAstronomicalObjectType());
		Integer subjectSize = ASTRONOMICAL_OBJECTS_SIZE_MAP.get(subject.getAstronomicalObjectType());

		if (locationCandidateSize == subjectSize) {
			log.error("Determined that astronomical object location candidate {} has the same type {} as the target {}", locationCandidate,
					locationCandidate.getAstronomicalObjectType(), subject);
		} else if (locationCandidateSize < subjectSize) {
			log.error("Determined that astronomical object location candidate {} has lower size than {} as the target {}", locationCandidate,
					locationCandidate.getAstronomicalObjectType(), subject);
		} else {
			subject.setLocation(locationCandidate);
		}

	}
}
