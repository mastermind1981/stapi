package com.cezarykluczynski.stapi.etl.template.individual.processor.species;

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.dto.FixedValueHolder;
import com.cezarykluczynski.stapi.etl.template.individual.dto.IndividualTemplate;
import com.cezarykluczynski.stapi.model.character.entity.CharacterSpecies;
import com.cezarykluczynski.stapi.sources.mediawiki.api.WikitextApi;
import com.cezarykluczynski.stapi.sources.mediawiki.api.dto.PageLink;
import com.cezarykluczynski.stapi.util.tool.NumberUtil;
import com.cezarykluczynski.stapi.util.tool.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.Fraction;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class CharacterSpeciesWikitextProcessor implements ItemProcessor<Pair<String, IndividualTemplate>, Set<CharacterSpecies>> {

	private static final String HUMAN = "Human";
	private static final String AUGMENT = "Augment";
	private static final String HYBRID = "hybrid";
	private static final String ARDANAN = "Ardanan";
	private static final String FORMER = "former";

	private static final List<String> FRACTION_NAMES = Lists.newArrayList(CharacterSpeciesLiteralFractionWikitextEnrichingProcessor
			.FRACTIONS.keySet());

	private CharacterSpeciesFixedValueProvider characterSpeciesFixedValueProvider;

	private WikitextApi wikitextApi;

	private CharacterSpeciesLiteralFractionWikitextEnrichingProcessor characterSpeciesLiteralFractionWikitextEnrichingProcessor;

	private CharacterSpeciesWithSpeciesNameEnrichingProcessor characterSpeciesWithSpeciesNameEnrichingProcessor;

	@Inject
	public CharacterSpeciesWikitextProcessor(CharacterSpeciesFixedValueProvider characterSpeciesFixedValueProvider, WikitextApi wikitextApi,
			CharacterSpeciesLiteralFractionWikitextEnrichingProcessor characterSpeciesLiteralFractionWikitextEnrichingProcessor,
			CharacterSpeciesWithSpeciesNameEnrichingProcessor characterSpeciesWithSpeciesNameEnrichingProcessor) {
		this.characterSpeciesFixedValueProvider = characterSpeciesFixedValueProvider;
		this.wikitextApi = wikitextApi;
		this.characterSpeciesLiteralFractionWikitextEnrichingProcessor = characterSpeciesLiteralFractionWikitextEnrichingProcessor;
		this.characterSpeciesWithSpeciesNameEnrichingProcessor = characterSpeciesWithSpeciesNameEnrichingProcessor;
	}

	@Override
	public Set<CharacterSpecies> process(Pair<String, IndividualTemplate> pair) throws Exception {
		String item = pair.getLeft();
		IndividualTemplate individualTemplate = pair.getRight();
		Set<CharacterSpecies> characterSpeciesSet = Sets.newHashSet();

		FixedValueHolder<Map<String, Fraction>> characterSpeciesFixedValueHolder = characterSpeciesFixedValueProvider
				.getSearchedValue(individualTemplate.getName());

		if (characterSpeciesFixedValueHolder.isFound()) {
			for (Map.Entry<String, Fraction> speciesCandidate : characterSpeciesFixedValueHolder.getValue().entrySet()) {
				tryAddSingleSpeciesName(speciesCandidate.getKey(), characterSpeciesSet, speciesCandidate.getValue());
			}

			return characterSpeciesSet;
		}

		List<PageLink> pageLinkList = wikitextApi.getPageLinksFromWikitext(item);

		if (pageLinkList.isEmpty()) {
			return characterSpeciesSet;
		}

		if (pageLinkList.size() == 1) {
			tryAddSingleSpeciesName(pageLinkList.get(0).getTitle(), characterSpeciesSet);
		} else {
			if (StringUtil.containsAnyIgnoreCase(item, FRACTION_NAMES)) {
				characterSpeciesLiteralFractionWikitextEnrichingProcessor
						.enrich(EnrichablePair.of(Pair.of(item, pageLinkList), characterSpeciesSet));
			} else if (isHumanAugment(pageLinkList)) {
				tryAddSingleSpeciesName(HUMAN, characterSpeciesSet);
			} else if (isHybrid(item, pageLinkList)) {
				for (PageLink pageLink : pageLinkList) {
					if (!isHybrid(pageLink)) {
						tryAddSingleSpeciesName(pageLink.getTitle(), characterSpeciesSet, Fraction.getFraction(1, 2));
					}
				}
			} else if (isArdanan(pageLinkList)) {
				tryAddSingleSpeciesName(ARDANAN, characterSpeciesSet);
			} else if (isFormer(item, pageLinkList)) {
				tryAddSingleSpeciesName(pageLinkList.get(0).getTitle(), characterSpeciesSet);
			} else {
				log.info("Unknown species: \"{}\"", item);
			}
		}

		return characterSpeciesSet;
	}

	private boolean isHumanAugment(List<PageLink> pageLinkList) {
		return pageLinkList.size() == 2 && HUMAN.equals(pageLinkList.get(0).getTitle()) && AUGMENT.equals(pageLinkList.get(1).getTitle());
	}

	private boolean isHybrid(String item, List<PageLink> pageLinkList) {
		return pageLinkList.size() == 2 && StringUtils.containsIgnoreCase(item, HYBRID) && pageLinkList.stream().noneMatch(this::isHybrid)
				|| pageLinkList.size() == 3 && pageLinkList.stream().anyMatch(this::isHybrid);
	}

	private boolean isHybrid(PageLink pageLink) {
		return StringUtils.equalsIgnoreCase(pageLink.getTitle(), HYBRID) || StringUtils.equalsIgnoreCase(pageLink.getDescription(), HYBRID);
	}

	private void tryAddSingleSpeciesName(String speciesName, Set<CharacterSpecies> characterSpeciesSet) throws Exception {
		tryAddSingleSpeciesName(speciesName, characterSpeciesSet, Fraction.getFraction(1, 1));
	}

	private void tryAddSingleSpeciesName(String speciesName, Set<CharacterSpecies> characterSpeciesSet, Fraction fraction) throws Exception {
		characterSpeciesWithSpeciesNameEnrichingProcessor.enrich(EnrichablePair.of(Pair.of(speciesName, fraction), characterSpeciesSet));
	}

	private boolean isArdanan(List<PageLink> pageLinkList) {
		return pageLinkList.stream().anyMatch(pageLink -> StringUtils.equalsIgnoreCase(pageLink.getTitle(), ARDANAN));
	}

	private boolean isFormer(String item, List<PageLink> pageLinkList) {
		List<Integer> formerPositions = StringUtil.getAllSubstringPositions(item, FORMER);

		if (formerPositions.size() != 1 || !NumberUtil.inRangeInclusive(pageLinkList.size(), 2, 3)) {
			return false;
		}

		Integer lastLinkStartPosition = pageLinkList.get(pageLinkList.size() - 1).getStartPosition();
		return lastLinkStartPosition > formerPositions.get(0);
	}

}
