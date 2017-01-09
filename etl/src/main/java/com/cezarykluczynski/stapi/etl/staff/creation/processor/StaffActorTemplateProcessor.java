package com.cezarykluczynski.stapi.etl.staff.creation.processor;

import com.cezarykluczynski.stapi.etl.common.processor.AbstractActorTemplateProcessor;
import com.cezarykluczynski.stapi.etl.template.actor.dto.ActorTemplate;
import com.cezarykluczynski.stapi.model.common.service.GuidGenerator;
import com.cezarykluczynski.stapi.model.staff.entity.Staff;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class StaffActorTemplateProcessor extends AbstractActorTemplateProcessor
		implements ItemProcessor<ActorTemplate, Staff> {

	private GuidGenerator guidGenerator;

	@Inject
	public StaffActorTemplateProcessor(GuidGenerator guidGenerator) {
		this.guidGenerator = guidGenerator;
	}

	@Override
	public Staff process(ActorTemplate item) throws Exception {
		Staff staff = new Staff();

		processCommonFields(staff, item);
		staff.setGuid(guidGenerator.generateFromPage(item.getPage(), Staff.class));
		staff.setArtDepartment(item.isArtDepartment());
		staff.setArtDirector(item.isArtDirector());
		staff.setProductionDesigner(item.isProductionDesigner());
		staff.setCameraAndElectricalDepartment(item.isCameraAndElectricalDepartment());
		staff.setCinematographer(item.isCinematographer());
		staff.setCastingDepartment(item.isCastingDepartment());
		staff.setCostumeDepartment(item.isCostumeDepartment());
		staff.setCostumeDesigner(item.isCostumeDesigner());
		staff.setDirector(item.isDirector());
		staff.setAssistantAndSecondUnitDirector(item.isAssistantAndSecondUnitDirector());
		staff.setExhibitAndAttractionStaff(item.isExhibitAndAttractionStaff());
		staff.setFilmEditor(item.isFilmEditor());
		staff.setLinguist(item.isLinguist());
		staff.setLocationStaff(item.isLocationStaff());
		staff.setMakeupStaff(item.isMakeupStaff());
		staff.setMusicDepartment(item.isMusicDepartment());
		staff.setComposer(item.isComposer());
		staff.setPersonalAssistant(item.isPersonalAssistant());
		staff.setProducer(item.isProducer());
		staff.setProductionAssociate(item.isProductionAssociate());
		staff.setProductionStaff(item.isProductionStaff());
		staff.setPublicationStaff(item.isPublicationStaff());
		staff.setScienceConsultant(item.isScienceConsultant());
		staff.setSoundDepartment(item.isSoundDepartment());
		staff.setSpecialAndVisualEffectsStaff(item.isSpecialAndVisualEffectsStaff());
		staff.setAuthor(item.isAuthor());
		staff.setAudioAuthor(item.isAudioAuthor());
		staff.setCalendarArtist(item.isCalendarArtist());
		staff.setComicArtist(item.isComicArtist());
		staff.setComicAuthor(item.isComicAuthor());
		staff.setComicColorArtist(item.isComicColorArtist());
		staff.setComicInteriorArtist(item.isComicInteriorArtist());
		staff.setComicInkArtist(item.isComicInkArtist());
		staff.setComicPencilArtist(item.isComicPencilArtist());
		staff.setComicLetterArtist(item.isComicLetterArtist());
		staff.setComicStripArtist(item.isComicStripArtist());
		staff.setGameArtist(item.isGameArtist());
		staff.setGameAuthor(item.isGameAuthor());
		staff.setNovelArtist(item.isNovelArtist());
		staff.setNovelAuthor(item.isNovelAuthor());
		staff.setReferenceArtist(item.isReferenceArtist());
		staff.setReferenceAuthor(item.isReferenceAuthor());
		staff.setPublicationArtist(item.isPublicationArtist());
		staff.setPublicationDesigner(item.isPublicationDesigner());
		staff.setPublicationEditor(item.isPublicationEditor());
		staff.setPublicityArtist(item.isPublicityArtist());
		staff.setCbsDigitalStaff(item.isCbsDigitalStaff());
		staff.setIlmProductionStaff(item.isIlmProductionStaff());
		staff.setSpecialFeaturesStaff(item.isSpecialFeaturesStaff());
		staff.setStoryEditor(item.isStoryEditor());
		staff.setStudioExecutive(item.isStudioExecutive());
		staff.setStuntDepartment(item.isStuntDepartment());
		staff.setTransportationDepartment(item.isTransportationDepartment());
		staff.setVideoGameProductionStaff(item.isVideoGameProductionStaff());
		staff.setWriter(item.isWriter());

		return staff;
	}

}
