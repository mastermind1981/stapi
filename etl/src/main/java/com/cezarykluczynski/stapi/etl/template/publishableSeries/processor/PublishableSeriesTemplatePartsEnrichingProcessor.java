package com.cezarykluczynski.stapi.etl.template.publishableSeries.processor;

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.processor.ItemEnrichingProcessor;
import com.cezarykluczynski.stapi.etl.common.processor.company.WikitextToCompaniesProcessor;
import com.cezarykluczynski.stapi.etl.template.common.dto.datetime.StardateRange;
import com.cezarykluczynski.stapi.etl.template.common.dto.datetime.YearRange;
import com.cezarykluczynski.stapi.etl.template.common.processor.datetime.WikitextToStardateRangeProcessor;
import com.cezarykluczynski.stapi.etl.template.common.processor.datetime.WikitextToYearRangeProcessor;
import com.cezarykluczynski.stapi.etl.template.publishableSeries.dto.PublishableSeriesTemplate;
import com.cezarykluczynski.stapi.etl.template.publishableSeries.dto.PublishableSeriesTemplateParameter;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class PublishableSeriesTemplatePartsEnrichingProcessor
		implements ItemEnrichingProcessor<EnrichablePair<List<Template.Part>, PublishableSeriesTemplate>> {

	private WikitextToCompaniesProcessor wikitextToCompaniesProcessor;

	private PublishableSeriesPublishedDatesEnrichingProcessor publishableSeriesPublishedDatesEnrichingProcessor;

	private WikitextToYearRangeProcessor wikitextToYearRangeProcessor;

	private WikitextToStardateRangeProcessor wikitextToStardateRangeProcessor;

	private PublishableSeriesTemplateMiniseriesProcessor publishableSeriesTemplateMiniseriesProcessor;

	@Inject
	public PublishableSeriesTemplatePartsEnrichingProcessor(WikitextToCompaniesProcessor wikitextToCompaniesProcessor,
			PublishableSeriesPublishedDatesEnrichingProcessor publishableSeriesPublishedDatesEnrichingProcessor,
			WikitextToYearRangeProcessor wikitextToYearRangeProcessor, WikitextToStardateRangeProcessor wikitextToStardateRangeProcessor,
			PublishableSeriesTemplateMiniseriesProcessor publishableSeriesTemplateMiniseriesProcessor) {
		this.wikitextToCompaniesProcessor = wikitextToCompaniesProcessor;
		this.publishableSeriesPublishedDatesEnrichingProcessor = publishableSeriesPublishedDatesEnrichingProcessor;
		this.wikitextToYearRangeProcessor = wikitextToYearRangeProcessor;
		this.wikitextToStardateRangeProcessor = wikitextToStardateRangeProcessor;
		this.publishableSeriesTemplateMiniseriesProcessor = publishableSeriesTemplateMiniseriesProcessor;
	}

	@Override
	public void enrich(EnrichablePair<List<Template.Part>, PublishableSeriesTemplate> enrichablePair) throws Exception {
		PublishableSeriesTemplate bookSeriesTemplate = enrichablePair.getOutput();

		for (Template.Part part : enrichablePair.getInput()) {
			String key = part.getKey();
			String value = part.getValue();

			switch (key) {
				case PublishableSeriesTemplateParameter.PUBLISHER:
					bookSeriesTemplate.getPublishers().addAll(wikitextToCompaniesProcessor.process(value));
					break;
				case PublishableSeriesTemplateParameter.PUBLISHED:
					if (bookSeriesTemplate.getPublishedYearFrom() == null && bookSeriesTemplate.getPublishedYearTo() == null) {
						publishableSeriesPublishedDatesEnrichingProcessor.enrich(EnrichablePair.of(part, bookSeriesTemplate));
					}
					break;
				case PublishableSeriesTemplateParameter.YEAR:
					if (bookSeriesTemplate.getYearFrom() == null && bookSeriesTemplate.getYearTo() == null) {
						YearRange yearRange = wikitextToYearRangeProcessor.process(value);
						if (yearRange != null) {
							bookSeriesTemplate.setYearFrom(yearRange.getYearFrom());
							bookSeriesTemplate.setYearTo(yearRange.getYearTo());
						}
					}
					break;
				case PublishableSeriesTemplateParameter.STARDATE:
					if (bookSeriesTemplate.getStardateFrom() == null && bookSeriesTemplate.getStardateTo() == null) {
						StardateRange stardateRange = wikitextToStardateRangeProcessor.process(value);
						if (stardateRange != null) {
							bookSeriesTemplate.setStardateFrom(stardateRange.getStardateFrom());
							bookSeriesTemplate.setStardateTo(stardateRange.getStardateTo());
						}
					}
					break;
				case PublishableSeriesTemplateParameter.SERIES:
					if (bookSeriesTemplate.getMiniseries() == null) {
						bookSeriesTemplate.setMiniseries(publishableSeriesTemplateMiniseriesProcessor.process(value));
					}
					break;
				default:
					break;
			}
		}
	}

}
