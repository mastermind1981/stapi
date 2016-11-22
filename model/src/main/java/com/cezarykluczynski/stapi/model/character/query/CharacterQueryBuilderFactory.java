package com.cezarykluczynski.stapi.model.character.query;

import com.cezarykluczynski.stapi.model.common.query.AbstractQueryBuilderFactory;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;
import com.cezarykluczynski.stapi.model.character.entity.Character;

import javax.inject.Inject;

@Service
public class CharacterQueryBuilderFactory extends AbstractQueryBuilderFactory<Character> {

	@Inject
	public CharacterQueryBuilderFactory(JpaContext jpaContext) {
		super(jpaContext, Character.class);
	}

}