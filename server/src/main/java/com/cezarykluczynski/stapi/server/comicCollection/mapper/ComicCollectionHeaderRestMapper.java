package com.cezarykluczynski.stapi.server.comicCollection.mapper;

import com.cezarykluczynski.stapi.client.v1.rest.model.ComicCollectionHeader;
import com.cezarykluczynski.stapi.model.comicCollection.entity.ComicCollection;
import com.cezarykluczynski.stapi.server.configuration.MapstructConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(config = MapstructConfiguration.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComicCollectionHeaderRestMapper {

	ComicCollectionHeader map(ComicCollection comicCollection);

	List<ComicCollectionHeader> map(List<ComicCollection> comicCollection);

}
