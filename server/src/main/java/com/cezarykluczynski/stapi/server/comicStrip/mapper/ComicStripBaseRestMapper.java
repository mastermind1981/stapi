package com.cezarykluczynski.stapi.server.comicStrip.mapper;

import com.cezarykluczynski.stapi.client.v1.rest.model.ComicStripBase;
import com.cezarykluczynski.stapi.model.comicStrip.dto.ComicStripRequestDTO;
import com.cezarykluczynski.stapi.model.comicStrip.entity.ComicStrip;
import com.cezarykluczynski.stapi.server.comicStrip.dto.ComicStripRestBeanParams;
import com.cezarykluczynski.stapi.server.common.mapper.RequestSortRestMapper;
import com.cezarykluczynski.stapi.server.configuration.MapstructConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapstructConfiguration.class, uses = {RequestSortRestMapper.class})
public interface ComicStripBaseRestMapper {

	ComicStripRequestDTO mapBase(ComicStripRestBeanParams comicStripRestBeanParams);

	ComicStripBase mapBase(ComicStrip comicStrip);

	List<ComicStripBase> mapBase(List<ComicStrip> comicStripList);

}
