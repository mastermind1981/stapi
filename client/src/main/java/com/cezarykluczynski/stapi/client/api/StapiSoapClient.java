package com.cezarykluczynski.stapi.client.api;

import com.cezarykluczynski.stapi.client.v1.soap.AstronomicalObjectPortType;
import com.cezarykluczynski.stapi.client.v1.soap.BookPortType;
import com.cezarykluczynski.stapi.client.v1.soap.BookSeriesPortType;
import com.cezarykluczynski.stapi.client.v1.soap.CharacterPortType;
import com.cezarykluczynski.stapi.client.v1.soap.ComicCollectionPortType;
import com.cezarykluczynski.stapi.client.v1.soap.ComicSeriesPortType;
import com.cezarykluczynski.stapi.client.v1.soap.ComicStripPortType;
import com.cezarykluczynski.stapi.client.v1.soap.ComicsPortType;
import com.cezarykluczynski.stapi.client.v1.soap.CompanyPortType;
import com.cezarykluczynski.stapi.client.v1.soap.EpisodePortType;
import com.cezarykluczynski.stapi.client.v1.soap.FoodPortType;
import com.cezarykluczynski.stapi.client.v1.soap.LocationPortType;
import com.cezarykluczynski.stapi.client.v1.soap.MoviePortType;
import com.cezarykluczynski.stapi.client.v1.soap.OrganizationPortType;
import com.cezarykluczynski.stapi.client.v1.soap.PerformerPortType;
import com.cezarykluczynski.stapi.client.v1.soap.SeriesPortType;
import com.cezarykluczynski.stapi.client.v1.soap.SpeciesPortType;
import com.cezarykluczynski.stapi.client.v1.soap.StaffPortType;
import lombok.Getter;

public class StapiSoapClient {

	private StapiSoapPortTypesProvider stapiSoapPortTypesProvider;

	@Getter
	private SeriesPortType seriesPortType;

	@Getter
	private PerformerPortType performerPortType;

	@Getter
	private StaffPortType staffPortType;

	@Getter
	private CharacterPortType characterPortType;

	@Getter
	private EpisodePortType episodePortType;

	@Getter
	private MoviePortType moviePortType;

	@Getter
	private AstronomicalObjectPortType astronomicalObjectPortType;

	@Getter
	private CompanyPortType companyPortType;

	@Getter
	private ComicSeriesPortType comicSeriesPortType;

	@Getter
	private ComicsPortType comicsPortType;

	@Getter
	private ComicStripPortType comicStripPortType;

	@Getter
	private ComicCollectionPortType comicCollectionPortType;

	@Getter
	private SpeciesPortType speciesPortType;

	@Getter
	private OrganizationPortType organizationPortType;

	@Getter
	private FoodPortType foodPortType;

	@Getter
	private LocationPortType locationPortType;

	@Getter
	private BookSeriesPortType bookSeriesPortType;

	@Getter
	private BookPortType bookPortType;

	public StapiSoapClient() {
		stapiSoapPortTypesProvider = new StapiSoapPortTypesProvider();
		bindPortTypes();
	}

	public StapiSoapClient(String apiUrl) {
		stapiSoapPortTypesProvider = new StapiSoapPortTypesProvider(apiUrl);
		bindPortTypes();
	}

	private void bindPortTypes() {
		seriesPortType = stapiSoapPortTypesProvider.getSeriesPortType();
		performerPortType = stapiSoapPortTypesProvider.getPerformerPortType();
		staffPortType = stapiSoapPortTypesProvider.getStaffPortType();
		characterPortType = stapiSoapPortTypesProvider.getCharacterPortType();
		episodePortType = stapiSoapPortTypesProvider.getEpisodePortType();
		moviePortType = stapiSoapPortTypesProvider.getMoviePortType();
		astronomicalObjectPortType = stapiSoapPortTypesProvider.getAstronomicalObjectPortType();
		companyPortType = stapiSoapPortTypesProvider.getCompanyPortType();
		comicSeriesPortType = stapiSoapPortTypesProvider.getComicSeriesPortType();
		comicsPortType = stapiSoapPortTypesProvider.getComicsPortType();
		comicStripPortType = stapiSoapPortTypesProvider.getComicStripPortType();
		comicCollectionPortType = stapiSoapPortTypesProvider.getComicCollectionPortType();
		speciesPortType = stapiSoapPortTypesProvider.getSpeciesPortType();
		organizationPortType = stapiSoapPortTypesProvider.getOrganizationPortType();
		foodPortType = stapiSoapPortTypesProvider.getFoodPortType();
		locationPortType = stapiSoapPortTypesProvider.getLocationPortType();
		bookSeriesPortType = stapiSoapPortTypesProvider.getBookSeriesPortType();
		bookPortType = stapiSoapPortTypesProvider.getBookPortType();
	}

}
