package com.search.service.impl;

import com.search.document.EventDocument;
import com.search.dto.request.EventSearchRequest;
import com.search.service.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventSearchServiceImpl implements EventService {
    private final ElasticsearchOperations elasticsearchOperations;

    public EventSearchServiceImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public List<EventDocument> searchDocuments(EventSearchRequest request) {
        CriteriaQuery query = new CriteriaQuery(request.toCriteria());

        Sort sort = request.sortBy() != null
                ? Sort.by(request.sortDir() != null && request.sortDir().equalsIgnoreCase("DESC")
                          ? Sort.Direction.DESC : Sort.Direction.ASC, request.sortBy())
                : Sort.unsorted();

        int page = request.page() != null ? request.page() : 0;
        int size = request.size() != null ? request.size() : 10;

        query.setPageable(PageRequest.of(page, size, sort));

        SearchHits<EventDocument> hits = elasticsearchOperations.search(query, EventDocument.class);

        return hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();
    }
}