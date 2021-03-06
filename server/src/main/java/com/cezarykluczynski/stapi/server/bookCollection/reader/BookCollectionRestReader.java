package com.cezarykluczynski.stapi.server.bookCollection.reader;

import com.cezarykluczynski.stapi.client.v1.rest.model.BookCollectionBaseResponse;
import com.cezarykluczynski.stapi.client.v1.rest.model.BookCollectionFullResponse;
import com.cezarykluczynski.stapi.model.bookCollection.entity.BookCollection;
import com.cezarykluczynski.stapi.server.bookCollection.dto.BookCollectionRestBeanParams;
import com.cezarykluczynski.stapi.server.bookCollection.mapper.BookCollectionBaseRestMapper;
import com.cezarykluczynski.stapi.server.bookCollection.mapper.BookCollectionFullRestMapper;
import com.cezarykluczynski.stapi.server.bookCollection.query.BookCollectionRestQuery;
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper;
import com.cezarykluczynski.stapi.server.common.reader.BaseReader;
import com.cezarykluczynski.stapi.server.common.reader.FullReader;
import com.cezarykluczynski.stapi.server.common.validator.StaticValidator;
import com.google.common.collect.Iterables;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class BookCollectionRestReader implements BaseReader<BookCollectionRestBeanParams, BookCollectionBaseResponse>,
		FullReader<String, BookCollectionFullResponse> {

	private BookCollectionRestQuery bookCollectionRestQuery;

	private BookCollectionBaseRestMapper bookCollectionBaseRestMapper;

	private BookCollectionFullRestMapper bookCollectionFullRestMapper;

	private PageMapper pageMapper;

	@Inject
	public BookCollectionRestReader(BookCollectionRestQuery bookCollectionRestQuery, BookCollectionBaseRestMapper bookCollectionBaseRestMapper,
			BookCollectionFullRestMapper bookCollectionFullRestMapper, PageMapper pageMapper) {
		this.bookCollectionRestQuery = bookCollectionRestQuery;
		this.bookCollectionBaseRestMapper = bookCollectionBaseRestMapper;
		this.bookCollectionFullRestMapper = bookCollectionFullRestMapper;
		this.pageMapper = pageMapper;
	}

	@Override
	public BookCollectionBaseResponse readBase(BookCollectionRestBeanParams bookCollectionRestBeanParams) {
		Page<BookCollection> bookCollectionPage = bookCollectionRestQuery.query(bookCollectionRestBeanParams);
		BookCollectionBaseResponse bookCollectionResponse = new BookCollectionBaseResponse();
		bookCollectionResponse.setPage(pageMapper.fromPageToRestResponsePage(bookCollectionPage));
		bookCollectionResponse.getBookCollections().addAll(bookCollectionBaseRestMapper.mapBase(bookCollectionPage.getContent()));
		return bookCollectionResponse;
	}

	@Override
	public BookCollectionFullResponse readFull(String uid) {
		StaticValidator.requireUid(uid);
		BookCollectionRestBeanParams bookCollectionRestBeanParams = new BookCollectionRestBeanParams();
		bookCollectionRestBeanParams.setUid(uid);
		Page<BookCollection> bookCollectionPage = bookCollectionRestQuery.query(bookCollectionRestBeanParams);
		BookCollectionFullResponse bookCollectionResponse = new BookCollectionFullResponse();
		bookCollectionResponse.setBookCollection(bookCollectionFullRestMapper
				.mapFull(Iterables.getOnlyElement(bookCollectionPage.getContent(), null)));
		return bookCollectionResponse;
	}

}
