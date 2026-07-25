package com.search.repository;

import com.search.document.EventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EventSearchRepository extends ElasticsearchRepository<EventDocument, String> {
}
