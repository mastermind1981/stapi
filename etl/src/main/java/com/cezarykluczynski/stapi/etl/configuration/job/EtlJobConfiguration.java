package com.cezarykluczynski.stapi.etl.configuration.job;

import com.cezarykluczynski.stapi.etl.astronomicalObject.creation.processor.AstronomicalObjectProcessor;
import com.cezarykluczynski.stapi.etl.astronomicalObject.creation.processor.AstronomicalObjectReader;
import com.cezarykluczynski.stapi.etl.astronomicalObject.creation.processor.AstronomicalObjectWriter;
import com.cezarykluczynski.stapi.etl.astronomicalObject.link.processor.AstronomicalObjectLinkProcessor;
import com.cezarykluczynski.stapi.etl.astronomicalObject.link.processor.AstronomicalObjectLinkReader;
import com.cezarykluczynski.stapi.etl.book.creation.processor.BookProcessor;
import com.cezarykluczynski.stapi.etl.book.creation.processor.BookReader;
import com.cezarykluczynski.stapi.etl.book.creation.processor.BookWriter;
import com.cezarykluczynski.stapi.etl.bookCollection.creation.processor.BookCollectionProcessor;
import com.cezarykluczynski.stapi.etl.bookCollection.creation.processor.BookCollectionReader;
import com.cezarykluczynski.stapi.etl.bookCollection.creation.processor.BookCollectionWriter;
import com.cezarykluczynski.stapi.etl.bookSeries.creation.processor.BookSeriesProcessor;
import com.cezarykluczynski.stapi.etl.bookSeries.creation.processor.BookSeriesReader;
import com.cezarykluczynski.stapi.etl.bookSeries.creation.processor.BookSeriesWriter;
import com.cezarykluczynski.stapi.etl.bookSeries.link.processor.BookSeriesLinkProcessor;
import com.cezarykluczynski.stapi.etl.bookSeries.link.processor.BookSeriesLinkReader;
import com.cezarykluczynski.stapi.etl.character.creation.processor.CharacterProcessor;
import com.cezarykluczynski.stapi.etl.character.creation.processor.CharacterReader;
import com.cezarykluczynski.stapi.etl.character.creation.processor.CharacterWriter;
import com.cezarykluczynski.stapi.etl.comicCollection.creation.processor.ComicCollectionProcessor;
import com.cezarykluczynski.stapi.etl.comicCollection.creation.processor.ComicCollectionReader;
import com.cezarykluczynski.stapi.etl.comicCollection.creation.processor.ComicCollectionWriter;
import com.cezarykluczynski.stapi.etl.comicSeries.creation.processor.ComicSeriesProcessor;
import com.cezarykluczynski.stapi.etl.comicSeries.creation.processor.ComicSeriesReader;
import com.cezarykluczynski.stapi.etl.comicSeries.creation.processor.ComicSeriesWriter;
import com.cezarykluczynski.stapi.etl.comicSeries.link.processor.ComicSeriesLinkProcessor;
import com.cezarykluczynski.stapi.etl.comicSeries.link.processor.ComicSeriesLinkReader;
import com.cezarykluczynski.stapi.etl.comicStrip.creation.processor.ComicStripProcessor;
import com.cezarykluczynski.stapi.etl.comicStrip.creation.processor.ComicStripReader;
import com.cezarykluczynski.stapi.etl.comicStrip.creation.processor.ComicStripWriter;
import com.cezarykluczynski.stapi.etl.comics.creation.processor.ComicsProcessor;
import com.cezarykluczynski.stapi.etl.comics.creation.processor.ComicsReader;
import com.cezarykluczynski.stapi.etl.comics.creation.processor.ComicsWriter;
import com.cezarykluczynski.stapi.etl.common.listener.CommonStepExecutionListener;
import com.cezarykluczynski.stapi.etl.company.creation.processor.CompanyProcessor;
import com.cezarykluczynski.stapi.etl.company.creation.processor.CompanyReader;
import com.cezarykluczynski.stapi.etl.company.creation.processor.CompanyWriter;
import com.cezarykluczynski.stapi.etl.configuration.job.properties.StepsProperties;
import com.cezarykluczynski.stapi.etl.episode.creation.processor.EpisodeProcessor;
import com.cezarykluczynski.stapi.etl.episode.creation.processor.EpisodeReader;
import com.cezarykluczynski.stapi.etl.episode.creation.processor.EpisodeWriter;
import com.cezarykluczynski.stapi.etl.food.creation.processor.FoodProcessor;
import com.cezarykluczynski.stapi.etl.food.creation.processor.FoodReader;
import com.cezarykluczynski.stapi.etl.food.creation.processor.FoodWriter;
import com.cezarykluczynski.stapi.etl.location.creation.processor.LocationProcessor;
import com.cezarykluczynski.stapi.etl.location.creation.processor.LocationReader;
import com.cezarykluczynski.stapi.etl.location.creation.processor.LocationWriter;
import com.cezarykluczynski.stapi.etl.movie.creation.processor.MovieProcessor;
import com.cezarykluczynski.stapi.etl.movie.creation.processor.MovieReader;
import com.cezarykluczynski.stapi.etl.movie.creation.processor.MovieWriter;
import com.cezarykluczynski.stapi.etl.organization.creation.processor.OrganizationProcessor;
import com.cezarykluczynski.stapi.etl.organization.creation.processor.OrganizationReader;
import com.cezarykluczynski.stapi.etl.organization.creation.processor.OrganizationWriter;
import com.cezarykluczynski.stapi.etl.performer.creation.processor.PerformerProcessor;
import com.cezarykluczynski.stapi.etl.performer.creation.processor.PerformerReader;
import com.cezarykluczynski.stapi.etl.performer.creation.processor.PerformerWriter;
import com.cezarykluczynski.stapi.etl.series.creation.processor.SeriesProcessor;
import com.cezarykluczynski.stapi.etl.series.creation.processor.SeriesReader;
import com.cezarykluczynski.stapi.etl.series.creation.processor.SeriesWriter;
import com.cezarykluczynski.stapi.etl.species.creation.processor.SpeciesProcessor;
import com.cezarykluczynski.stapi.etl.species.creation.processor.SpeciesReader;
import com.cezarykluczynski.stapi.etl.species.creation.processor.SpeciesWriter;
import com.cezarykluczynski.stapi.etl.staff.creation.processor.StaffProcessor;
import com.cezarykluczynski.stapi.etl.staff.creation.processor.StaffReader;
import com.cezarykluczynski.stapi.etl.staff.creation.processor.StaffWriter;
import com.cezarykluczynski.stapi.etl.util.constant.StepName;
import com.cezarykluczynski.stapi.model.astronomicalObject.entity.AstronomicalObject;
import com.cezarykluczynski.stapi.model.book.entity.Book;
import com.cezarykluczynski.stapi.model.bookCollection.entity.BookCollection;
import com.cezarykluczynski.stapi.model.bookSeries.entity.BookSeries;
import com.cezarykluczynski.stapi.model.character.entity.Character;
import com.cezarykluczynski.stapi.model.comicCollection.entity.ComicCollection;
import com.cezarykluczynski.stapi.model.comicSeries.entity.ComicSeries;
import com.cezarykluczynski.stapi.model.comicStrip.entity.ComicStrip;
import com.cezarykluczynski.stapi.model.comics.entity.Comics;
import com.cezarykluczynski.stapi.model.company.entity.Company;
import com.cezarykluczynski.stapi.model.episode.entity.Episode;
import com.cezarykluczynski.stapi.model.food.entity.Food;
import com.cezarykluczynski.stapi.model.location.entity.Location;
import com.cezarykluczynski.stapi.model.movie.entity.Movie;
import com.cezarykluczynski.stapi.model.organization.entity.Organization;
import com.cezarykluczynski.stapi.model.performer.entity.Performer;
import com.cezarykluczynski.stapi.model.series.entity.Series;
import com.cezarykluczynski.stapi.model.species.entity.Species;
import com.cezarykluczynski.stapi.model.staff.entity.Staff;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.PageHeader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
@EnableConfigurationProperties(StepsProperties.class)
public class EtlJobConfiguration {

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private JobBuilder jobBuilder;

	@Inject
	private StepBuilderFactory stepBuilderFactory;

	@Inject
	private StepsProperties stepsProperties;

	@Bean
	public FactoryBean<Job> jobCreate() {
		return new JobFactoryBean(jobBuilder.build());
	}

	@Bean(name = StepName.CREATE_COMPANIES)
	public Step stepCreateCompanies() {
		return stepBuilderFactory.get(StepName.CREATE_COMPANIES)
				.<PageHeader, Company>chunk(stepsProperties.getCreateCompanies().getCommitInterval())
				.reader(applicationContext.getBean(CompanyReader.class))
				.processor(applicationContext.getBean(CompanyProcessor.class))
				.writer(applicationContext.getBean(CompanyWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_SERIES)
	public Step stepCreateSeries() {
		return stepBuilderFactory.get(StepName.CREATE_SERIES)
				.<PageHeader, Series>chunk(stepsProperties.getCreateSeries().getCommitInterval())
				.reader(applicationContext.getBean(SeriesReader.class))
				.processor(applicationContext.getBean(SeriesProcessor.class))
				.writer(applicationContext.getBean(SeriesWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_PERFORMERS)
	public Step stepCreatePerformers() {
		return stepBuilderFactory.get(StepName.CREATE_PERFORMERS)
				.<PageHeader, Performer>chunk(stepsProperties.getCreatePerformers().getCommitInterval())
				.reader(applicationContext.getBean(PerformerReader.class))
				.processor(applicationContext.getBean(PerformerProcessor.class))
				.writer(applicationContext.getBean(PerformerWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_STAFF)
	public Step stepCreateStaff() {
		return stepBuilderFactory.get(StepName.CREATE_STAFF)
				.<PageHeader, Staff>chunk(stepsProperties.getCreateStaff().getCommitInterval())
				.reader(applicationContext.getBean(StaffReader.class))
				.processor(applicationContext.getBean(StaffProcessor.class))
				.writer(applicationContext.getBean(StaffWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_ASTRONOMICAL_OBJECTS)
	public Step stepCreateAstronomicalObject() {
		return stepBuilderFactory.get(StepName.CREATE_ASTRONOMICAL_OBJECTS)
				.<PageHeader, AstronomicalObject>chunk(stepsProperties.getCreateAstronomicalObjects().getCommitInterval())
				.reader(applicationContext.getBean(AstronomicalObjectReader.class))
				.processor(applicationContext.getBean(AstronomicalObjectProcessor.class))
				.writer(applicationContext.getBean(AstronomicalObjectWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_SPECIES)
	public Step stepCreateSpecies() {
		return stepBuilderFactory.get(StepName.CREATE_SPECIES)
				.<PageHeader, Species>chunk(stepsProperties.getCreateSpecies().getCommitInterval())
				.reader(applicationContext.getBean(SpeciesReader.class))
				.processor(applicationContext.getBean(SpeciesProcessor.class))
				.writer(applicationContext.getBean(SpeciesWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_CHARACTERS)
	public Step stepCreateCharacters() {
		return stepBuilderFactory.get(StepName.CREATE_CHARACTERS)
				.<PageHeader, Character>chunk(stepsProperties.getCreateCharacters().getCommitInterval())
				.reader(applicationContext.getBean(CharacterReader.class))
				.processor(applicationContext.getBean(CharacterProcessor.class))
				.writer(applicationContext.getBean(CharacterWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_EPISODES)
	public Step stepCreateEpisodes() {
		return stepBuilderFactory.get(StepName.CREATE_EPISODES)
				.<PageHeader, Episode>chunk(stepsProperties.getCreateEpisodes().getCommitInterval())
				.reader(applicationContext.getBean(EpisodeReader.class))
				.processor(applicationContext.getBean(EpisodeProcessor.class))
				.writer(applicationContext.getBean(EpisodeWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_MOVIES)
	public Step stepCreateMovies() {
		return stepBuilderFactory.get(StepName.CREATE_MOVIES)
				.<PageHeader, Movie>chunk(stepsProperties.getCreateMovies().getCommitInterval())
				.reader(applicationContext.getBean(MovieReader.class))
				.processor(applicationContext.getBean(MovieProcessor.class))
				.writer(applicationContext.getBean(MovieWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.LINK_ASTRONOMICAL_OBJECTS)
	public Step stepLinkAstronomicalObject() {
		return stepBuilderFactory.get(StepName.LINK_ASTRONOMICAL_OBJECTS)
				.<AstronomicalObject, AstronomicalObject>chunk(stepsProperties.getLinkAstronomicalObjects().getCommitInterval())
				.reader(applicationContext.getBean(AstronomicalObjectLinkReader.class))
				.processor(applicationContext.getBean(AstronomicalObjectLinkProcessor.class))
				.writer(applicationContext.getBean(AstronomicalObjectWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_COMIC_SERIES)
	public Step stepCreateComicSeries() {
		return stepBuilderFactory.get(StepName.CREATE_COMIC_SERIES)
				.<PageHeader, ComicSeries>chunk(stepsProperties.getCreateComicSeries().getCommitInterval())
				.reader(applicationContext.getBean(ComicSeriesReader.class))
				.processor(applicationContext.getBean(ComicSeriesProcessor.class))
				.writer(applicationContext.getBean(ComicSeriesWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.LINK_COMIC_SERIES)
	public Step stepLinkComicSeries() {
		return stepBuilderFactory.get(StepName.LINK_COMIC_SERIES)
				.<ComicSeries, ComicSeries>chunk(stepsProperties.getLinkComicSeries().getCommitInterval())
				.reader(applicationContext.getBean(ComicSeriesLinkReader.class))
				.processor(applicationContext.getBean(ComicSeriesLinkProcessor.class))
				.writer(applicationContext.getBean(ComicSeriesWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_COMICS)
	public Step stepCreateComics() {
		return stepBuilderFactory.get(StepName.CREATE_COMICS)
				.<PageHeader, Comics>chunk(stepsProperties.getCreateComics().getCommitInterval())
				.reader(applicationContext.getBean(ComicsReader.class))
				.processor(applicationContext.getBean(ComicsProcessor.class))
				.writer(applicationContext.getBean(ComicsWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_COMIC_STRIPS)
	public Step stepCreateComicStrips() {
		return stepBuilderFactory.get(StepName.CREATE_COMIC_STRIPS)
				.<PageHeader, ComicStrip>chunk(stepsProperties.getCreateComicStrips().getCommitInterval())
				.reader(applicationContext.getBean(ComicStripReader.class))
				.processor(applicationContext.getBean(ComicStripProcessor.class))
				.writer(applicationContext.getBean(ComicStripWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_COMIC_COLLECTIONS)
	public Step stepCreateComicCollections() {
		return stepBuilderFactory.get(StepName.CREATE_COMIC_COLLECTIONS)
				.<PageHeader, ComicCollection>chunk(stepsProperties.getCreateComicCollections().getCommitInterval())
				.reader(applicationContext.getBean(ComicCollectionReader.class))
				.processor(applicationContext.getBean(ComicCollectionProcessor.class))
				.writer(applicationContext.getBean(ComicCollectionWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_ORGANIZATIONS)
	public Step stepCreateOrganizations() {
		return stepBuilderFactory.get(StepName.CREATE_ORGANIZATIONS)
				.<PageHeader, Organization>chunk(stepsProperties.getCreateOrganizations().getCommitInterval())
				.reader(applicationContext.getBean(OrganizationReader.class))
				.processor(applicationContext.getBean(OrganizationProcessor.class))
				.writer(applicationContext.getBean(OrganizationWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_FOODS)
	public Step stepCreateFoods() {
		return stepBuilderFactory.get(StepName.CREATE_FOODS)
				.<PageHeader, Food>chunk(stepsProperties.getCreateFoods().getCommitInterval())
				.reader(applicationContext.getBean(FoodReader.class))
				.processor(applicationContext.getBean(FoodProcessor.class))
				.writer(applicationContext.getBean(FoodWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_LOCATIONS)
	public Step stepCreateLocations() {
		return stepBuilderFactory.get(StepName.CREATE_LOCATIONS)
				.<PageHeader, Location>chunk(stepsProperties.getCreateLocations().getCommitInterval())
				.reader(applicationContext.getBean(LocationReader.class))
				.processor(applicationContext.getBean(LocationProcessor.class))
				.writer(applicationContext.getBean(LocationWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_BOOK_SERIES)
	public Step stepCreateBookSeries() {
		return stepBuilderFactory.get(StepName.CREATE_BOOK_SERIES)
				.<PageHeader, BookSeries>chunk(stepsProperties.getCreateBookSeries().getCommitInterval())
				.reader(applicationContext.getBean(BookSeriesReader.class))
				.processor(applicationContext.getBean(BookSeriesProcessor.class))
				.writer(applicationContext.getBean(BookSeriesWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.LINK_BOOK_SERIES)
	public Step stepLinkBookSeries() {
		return stepBuilderFactory.get(StepName.LINK_BOOK_SERIES)
				.<BookSeries, BookSeries>chunk(stepsProperties.getLinkBookSeries().getCommitInterval())
				.reader(applicationContext.getBean(BookSeriesLinkReader.class))
				.processor(applicationContext.getBean(BookSeriesLinkProcessor.class))
				.writer(applicationContext.getBean(BookSeriesWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_BOOKS)
	public Step stepCreateBooks() {
		return stepBuilderFactory.get(StepName.CREATE_BOOKS)
				.<PageHeader, Book>chunk(stepsProperties.getCreateBooks().getCommitInterval())
				.reader(applicationContext.getBean(BookReader.class))
				.processor(applicationContext.getBean(BookProcessor.class))
				.writer(applicationContext.getBean(BookWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

	@Bean(name = StepName.CREATE_BOOK_COLLECTIONS)
	public Step stepCreateBookCollections() {
		return stepBuilderFactory.get(StepName.CREATE_BOOK_COLLECTIONS)
				.<PageHeader, BookCollection>chunk(stepsProperties.getCreateBookCollections().getCommitInterval())
				.reader(applicationContext.getBean(BookCollectionReader.class))
				.processor(applicationContext.getBean(BookCollectionProcessor.class))
				.writer(applicationContext.getBean(BookCollectionWriter.class))
				.listener(applicationContext.getBean(CommonStepExecutionListener.class))
				.startLimit(1)
				.allowStartIfComplete(false)
				.build();
	}

}
