package com.cezarykluczynski.stapi.server.episode.endpoint;


import com.cezarykluczynski.stapi.client.v1.rest.model.EpisodeBaseResponse;
import com.cezarykluczynski.stapi.client.v1.rest.model.EpisodeFullResponse;
import com.cezarykluczynski.stapi.server.common.dto.PageSortBeanParams;
import com.cezarykluczynski.stapi.server.episode.dto.EpisodeRestBeanParams;
import com.cezarykluczynski.stapi.server.episode.reader.EpisodeRestReader;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Service
@Produces(MediaType.APPLICATION_JSON)
public class EpisodeRestEndpoint {

	public static final String ADDRESS = "/v1/rest/episode";

	private EpisodeRestReader episodeRestReader;

	@Inject
	public EpisodeRestEndpoint(EpisodeRestReader episodeRestReader) {
		this.episodeRestReader = episodeRestReader;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public EpisodeFullResponse getEpisode(@QueryParam("uid") String uid) {
		return episodeRestReader.readFull(uid);
	}

	@GET
	@Path("search")
	@Consumes(MediaType.APPLICATION_JSON)
	public EpisodeBaseResponse searchEpisode(@BeanParam PageSortBeanParams pageSortBeanParams) {
		return episodeRestReader.readBase(EpisodeRestBeanParams.fromPageSortBeanParams(pageSortBeanParams));
	}

	@POST
	@Path("search")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public EpisodeBaseResponse searchEpisode(@BeanParam EpisodeRestBeanParams episodeRestBeanParams) {
		return episodeRestReader.readBase(episodeRestBeanParams);
	}

}
