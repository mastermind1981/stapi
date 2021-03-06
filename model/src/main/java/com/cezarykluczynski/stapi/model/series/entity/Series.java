package com.cezarykluczynski.stapi.model.series.entity;

import com.cezarykluczynski.stapi.model.common.entity.PageAwareEntity;
import com.cezarykluczynski.stapi.model.company.entity.Company;
import com.cezarykluczynski.stapi.model.episode.entity.Episode;
import com.cezarykluczynski.stapi.model.page.entity.PageAware;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"productionCompany", "originalBroadcaster", "episodes"})
@EqualsAndHashCode(callSuper = true, exclude = {"productionCompany", "originalBroadcaster", "episodes"})
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Series extends PageAwareEntity implements PageAware {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "series_sequence_generator")
	@SequenceGenerator(name = "series_sequence_generator", sequenceName = "series_sequence", allocationSize = 1)
	private Long id;

	@Column(nullable = false)
	private String title;

	private String abbreviation;

	private Integer productionStartYear;

	private Integer productionEndYear;

	private LocalDate originalRunStartDate;

	private LocalDate originalRunEndDate;

	private Integer seasonsCount;

	private Integer episodesCount;

	private Integer featureLengthEpisodesCount;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "production_company_id")
	private Company productionCompany;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "original_broadcaster_id")
	private Company originalBroadcaster;

	@OneToMany(mappedBy = "series", fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	private Set<Episode> episodes;

}
