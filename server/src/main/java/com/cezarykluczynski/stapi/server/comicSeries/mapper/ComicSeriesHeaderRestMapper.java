package com.cezarykluczynski.stapi.server.comicSeries.mapper;

import com.cezarykluczynski.stapi.client.v1.rest.model.ComicSeriesHeader;
import com.cezarykluczynski.stapi.model.comicSeries.entity.ComicSeries;
import com.cezarykluczynski.stapi.server.configuration.MapstructConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(config = MapstructConfiguration.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComicSeriesHeaderRestMapper {

	ComicSeriesHeader map(ComicSeries comicSeries);

	List<ComicSeriesHeader> map(List<ComicSeries> comicSeries);

}
