package com.cezarykluczynski.stapi.etl.template.common.dto.performance;

import com.cezarykluczynski.stapi.etl.template.common.dto.datetime.enums.StardateYearSource;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class StardateYearCandidateDTO {

	private String value;

	private StardateYearSource source; // TODO: should be last field in the class

	private String title;

}
