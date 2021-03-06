package com.cezarykluczynski.stapi.etl.template.individual.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(staticName = "of")
@Data
public class IndividualLifeBoundaryPlacesDTO {

	private String placeOfBirth;

	private String placeOfDeath;

}
