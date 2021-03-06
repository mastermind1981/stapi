package com.cezarykluczynski.stapi.model.astronomicalObject.entity;

import com.cezarykluczynski.stapi.model.astronomicalObject.entity.enums.AstronomicalObjectType;
import com.cezarykluczynski.stapi.model.common.entity.PageAwareEntity;
import com.cezarykluczynski.stapi.model.page.entity.PageAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"location", "astronomicalObjects"})
@EqualsAndHashCode(callSuper = true, exclude = {"location", "astronomicalObjects"})
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class AstronomicalObject extends PageAwareEntity implements PageAware {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "astronomical_object_sequence_generator")
	@SequenceGenerator(name = "astronomical_object_sequence_generator", sequenceName = "astronomical_object_sequence", allocationSize = 1)
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private AstronomicalObjectType astronomicalObjectType;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private AstronomicalObject location;

	@ManyToMany(mappedBy = "location")
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	private Set<AstronomicalObject> astronomicalObjects;

}
