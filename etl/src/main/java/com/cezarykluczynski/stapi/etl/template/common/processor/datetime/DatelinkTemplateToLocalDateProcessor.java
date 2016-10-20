package com.cezarykluczynski.stapi.etl.template.common.processor.datetime;

import com.cezarykluczynski.stapi.etl.template.common.dto.DayMonthYearCandidate;
import com.cezarykluczynski.stapi.util.constants.TemplateNames;
import com.cezarykluczynski.stapi.util.constants.TemplateParam;
import com.cezarykluczynski.stapi.wiki.dto.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;

@Service
@Slf4j
public class DatelinkTemplateToLocalDateProcessor implements ItemProcessor<Template, LocalDate> {

	private DayMonthYearProcessor dayMonthYearProcessor;

	@Inject
	public DatelinkTemplateToLocalDateProcessor(DayMonthYearProcessor dayMonthYearProcessor) {
		this.dayMonthYearProcessor = dayMonthYearProcessor;
	}

	@Override
	public LocalDate process(Template item) throws Exception {
		String title = item.getTitle();
		if (!TemplateNames.D.equals(title) && !TemplateNames.DATELINK.equals(title)) {
			log.warn("Template {} passed to TemplateToLocalDateProcessor::process was of different type", item);
			return null;
		}

		String day = null;
		String month = null;
		String year = null;

		for (Template.Part part : item.getParts()) {
			String key = part.getKey();
			String value = part.getValue();

			if (key.equals(TemplateParam.FIRST)) {
				day = value;
			}

			if (key.equals(TemplateParam.SECOND)) {
				month = value;
			}

			if (key.equals(TemplateParam.THIRD)) {
				year = value;
			}
		}

		return dayMonthYearProcessor.process(DayMonthYearCandidate.of(day, month, year));
	}

}