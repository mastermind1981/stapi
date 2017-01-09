package com.cezarykluczynski.stapi.model.episode.entity;

import com.cezarykluczynski.stapi.model.character.entity.Character;
import com.cezarykluczynski.stapi.model.common.entity.PageAwareEntity;
import com.cezarykluczynski.stapi.model.page.entity.PageAware;
import com.cezarykluczynski.stapi.model.performer.entity.Performer;
import com.cezarykluczynski.stapi.model.series.entity.Series;
import com.cezarykluczynski.stapi.model.staff.entity.Staff;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"series", "writers", "teleplayAuthors", "storyAuthors", "directors", "staff",
		"performers", "stuntPerformers", "standInPerformers"})
@EqualsAndHashCode(callSuper = true, exclude = {"series", "writers", "teleplayAuthors", "storyAuthors", "directors",
		"staff", "performers", "stuntPerformers", "standInPerformers"})
public class Episode extends PageAwareEntity implements PageAware {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "episode_sequence_generator")
	@SequenceGenerator(name = "episode_sequence_generator", sequenceName = "episode_sequence", allocationSize = 1)
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "series_id")
	private Series series;

	private String title;

	private String titleGerman;

	private String titleItalian;

	private String titleJapanese;

	private Integer seasonNumber;

	private Integer episodeNumber;

	private String productionSerialNumber;

	private Boolean featureLength;

	private Float stardateFrom;

	private Float stardateTo;

	private Integer yearFrom;

	private Integer yearTo;

	private LocalDate usAirDate;

	private LocalDate finalScriptDate;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "episodes_writers",
			joinColumns = @JoinColumn(name = "episode_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "staff_id", nullable = false, updatable = false))
	private Set<Staff> writers = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "episodes_teleplay_authors",
			joinColumns = @JoinColumn(name = "episode_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "staff_id", nullable = false, updatable = false))
	private Set<Staff> teleplayAuthors = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "episodes_story_authors",
			joinColumns = @JoinColumn(name = "episode_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "staff_id", nullable = false, updatable = false))
	private Set<Staff> storyAuthors = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "episodes_directors",
			joinColumns = @JoinColumn(name = "episode_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "staff_id", nullable = false, updatable = false))
	private Set<Staff> directors = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "episodes_staff",
			joinColumns = @JoinColumn(name = "episode_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "staff_id", nullable = false, updatable = false))
	private Set<Staff> staff = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "episodes_performers",
			joinColumns = @JoinColumn(name = "episode_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "performer_id", nullable = false, updatable = false))
	private Set<Performer> performers = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "episodes_stunt_performers",
			joinColumns = @JoinColumn(name = "episode_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "performer_id", nullable = false, updatable = false))
	private Set<Performer> stuntPerformers = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "episodes_stand_in_performers",
			joinColumns = @JoinColumn(name = "episode_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "performer_id", nullable = false, updatable = false))
	private Set<Performer> standInPerformers = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "episodes_characters",
			joinColumns = @JoinColumn(name = "episode_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "character_id", nullable = false, updatable = false))
	private Set<Character> characters = Sets.newHashSet();

}
