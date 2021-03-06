package com.cezarykluczynski.stapi.server.staff.mapper

import com.cezarykluczynski.stapi.client.v1.soap.StaffFull
import com.cezarykluczynski.stapi.client.v1.soap.StaffFullRequest
import com.cezarykluczynski.stapi.model.staff.dto.StaffRequestDTO
import com.cezarykluczynski.stapi.model.staff.entity.Staff
import org.mapstruct.factory.Mappers

class StaffFullSoapMapperTest extends AbstractStaffMapperTest {

	private StaffFullSoapMapper staffFullSoapMapper

	void setup() {
		staffFullSoapMapper = Mappers.getMapper(StaffFullSoapMapper)
	}

	void "maps SOAP StaffFullRequest to StaffBaseRequestDTO"() {
		given:
		StaffFullRequest staffFullRequest = new StaffFullRequest(uid: UID)

		when:
		StaffRequestDTO staffRequestDTO = staffFullSoapMapper.mapFull staffFullRequest

		then:
		staffRequestDTO.uid == UID
	}

	void "maps DB entity to full SOAP entity"() {
		given:
		Staff staff = createStaff()

		when:
		StaffFull staffFull = staffFullSoapMapper.mapFull(staff)

		then:
		staffFull.name == NAME
		staffFull.uid == UID
		staffFull.birthName == BIRTH_NAME
		staffFull.gender == GENDER_ENUM_SOAP
		staffFull.dateOfBirth == DATE_OF_BIRTH_FROM_SOAP
		staffFull.dateOfDeath == DATE_OF_DEATH_FROM_SOAP
		staffFull.placeOfBirth == PLACE_OF_BIRTH
		staffFull.placeOfDeath == PLACE_OF_DEATH
		staffFull.artDepartment == ART_DEPARTMENT
		staffFull.artDirector == ART_DIRECTOR
		staffFull.productionDesigner == PRODUCTION_DESIGNER
		staffFull.cameraAndElectricalDepartment == CAMERA_AND_ELECTRICAL_DEPARTMENT
		staffFull.cinematographer == CINEMATOGRAPHER
		staffFull.castingDepartment == CASTING_DEPARTMENT
		staffFull.costumeDepartment == COSTUME_DEPARTMENT
		staffFull.costumeDesigner == COSTUME_DESIGNER
		staffFull.director == DIRECTOR
		staffFull.assistantAndSecondUnitDirector == ASSISTANT_AND_SECOND_UNIT_DIRECTOR
		staffFull.exhibitAndAttractionStaff == EXHIBIT_AND_ATTRACTION_STAFF
		staffFull.filmEditor == FILM_EDITOR
		staffFull.linguist == LINGUIST
		staffFull.locationStaff == LOCATION_STAFF
		staffFull.makeupStaff == MAKEUP_STAFF
		staffFull.musicDepartment == MUSIC_DEPARTMENT
		staffFull.composer == COMPOSER
		staffFull.personalAssistant == PERSONAL_ASSISTANT
		staffFull.producer == PRODUCER
		staffFull.productionAssociate == PRODUCTION_ASSOCIATE
		staffFull.productionStaff == PRODUCTION_STAFF
		staffFull.publicationStaff == PUBLICATION_STAFF
		staffFull.scienceConsultant == SCIENCE_CONSULTANT
		staffFull.soundDepartment == SOUND_DEPARTMENT
		staffFull.specialAndVisualEffectsStaff == SPECIAL_AND_VISUAL_EFFECTS_STAFF
		staffFull.author == AUTHOR
		staffFull.audioAuthor == AUDIO_AUTHOR
		staffFull.calendarArtist == CALENDAR_ARTIST
		staffFull.comicArtist == COMIC_ARTIST
		staffFull.comicAuthor == COMIC_AUTHOR
		staffFull.comicColorArtist == COMIC_COLOR_ARTIST
		staffFull.comicInteriorArtist == COMIC_INTERIOR_ARTIST
		staffFull.comicInkArtist == COMIC_INK_ARTIST
		staffFull.comicPencilArtist == COMIC_PENCIL_ARTIST
		staffFull.comicLetterArtist == COMIC_LETTER_ARTIST
		staffFull.comicStripArtist == COMIC_STRIP_ARTIST
		staffFull.gameArtist == GAME_ARTIST
		staffFull.gameAuthor == GAME_AUTHOR
		staffFull.novelArtist == NOVEL_ARTIST
		staffFull.novelAuthor == NOVEL_AUTHOR
		staffFull.referenceArtist == REFERENCE_ARTIST
		staffFull.referenceAuthor == REFERENCE_AUTHOR
		staffFull.publicationArtist == PUBLICATION_ARTIST
		staffFull.publicationDesigner == PUBLICATION_DESIGNER
		staffFull.publicationEditor == PUBLICATION_EDITOR
		staffFull.publicityArtist == PUBLICITY_ARTIST
		staffFull.cbsDigitalStaff == CBS_DIGITAL_STAFF
		staffFull.ilmProductionStaff == ILM_PRODUCTION_STAFF
		staffFull.specialFeaturesStaff == SPECIAL_FEATURES_STAFF
		staffFull.storyEditor == STORY_EDITOR
		staffFull.studioExecutive == STUDIO_EXECUTIVE
		staffFull.stuntDepartment == STUNT_DEPARTMENT
		staffFull.transportationDepartment == TRANSPORTATION_DEPARTMENT
		staffFull.videoGameProductionStaff == VIDEO_GAME_PRODUCTION_STAFF
		staffFull.writer == WRITER
		staffFull.writtenEpisodes.size() == staff.writtenEpisodes.size()
		staffFull.teleplayAuthoredEpisodes.size() == staff.teleplayAuthoredEpisodes.size()
		staffFull.storyAuthoredEpisodes.size() == staff.storyAuthoredEpisodes.size()
		staffFull.directedEpisodes.size() == staff.directedEpisodes.size()
		staffFull.episodes.size() == staff.episodes.size()
		staffFull.writtenMovies.size() == staff.writtenMovies.size()
		staffFull.screenplayAuthoredMovies.size() == staff.screenplayAuthoredMovies.size()
		staffFull.storyAuthoredMovies.size() == staff.storyAuthoredMovies.size()
		staffFull.directedMovies.size() == staff.directedMovies.size()
		staffFull.producedMovies.size() == staff.producedMovies.size()
		staffFull.movies.size() == staff.movies.size()
	}

}
