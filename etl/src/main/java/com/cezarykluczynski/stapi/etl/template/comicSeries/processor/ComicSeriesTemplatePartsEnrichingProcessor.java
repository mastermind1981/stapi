package com.cezarykluczynski.stapi.etl.template.comicSeries.processor;

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.processor.ItemEnrichingProcessor;
import com.cezarykluczynski.stapi.etl.template.comicSeries.dto.ComicSeriesTemplate;
import com.cezarykluczynski.stapi.etl.template.comicSeries.dto.ComicSeriesTemplateParameter;
import com.cezarykluczynski.stapi.etl.template.common.processor.NumberOfPartsProcessor;
import com.cezarykluczynski.stapi.etl.template.publishableSeries.processor.PublishableSeriesTemplatePartsEnrichingProcessor;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class ComicSeriesTemplatePartsEnrichingProcessor implements ItemEnrichingProcessor<EnrichablePair<List<Template.Part>, ComicSeriesTemplate>> {

	private final PublishableSeriesTemplatePartsEnrichingProcessor publishableSeriesTemplatePartsEnrichingProcessor;

	private final NumberOfPartsProcessor numberOfPartsProcessor;

	@Inject
	public ComicSeriesTemplatePartsEnrichingProcessor(PublishableSeriesTemplatePartsEnrichingProcessor
			publishableSeriesTemplatePartsEnrichingProcessor, NumberOfPartsProcessor numberOfPartsProcessor) {
		this.publishableSeriesTemplatePartsEnrichingProcessor = publishableSeriesTemplatePartsEnrichingProcessor;
		this.numberOfPartsProcessor = numberOfPartsProcessor;
	}

	@Override
	public void enrich(EnrichablePair<List<Template.Part>, ComicSeriesTemplate> enrichablePair) throws Exception {
		publishableSeriesTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(enrichablePair.getInput(), enrichablePair.getOutput()));

		ComicSeriesTemplate comicSeriesTemplate = enrichablePair.getOutput();

		for (Template.Part part : enrichablePair.getInput()) {
			String key = part.getKey();
			String value = part.getValue();

			switch (key) {
				case ComicSeriesTemplateParameter.ISSUES:
					if (comicSeriesTemplate.getNumberOfIssues() == null) {
						comicSeriesTemplate.setNumberOfIssues(numberOfPartsProcessor.process(value));
					}
					break;
				default:
					break;
			}
		}
	}

}
