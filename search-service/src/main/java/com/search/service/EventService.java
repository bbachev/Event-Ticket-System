package com.search.service;

import com.search.document.EventDocument;
import com.search.dto.request.EventSearchRequest;

import java.util.List;

public interface EventService {
    List<EventDocument> searchDocuments(EventSearchRequest request);
}
