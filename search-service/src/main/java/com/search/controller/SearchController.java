package com.search.controller;

import com.search.document.EventDocument;
import com.search.dto.request.EventSearchRequest;
import com.search.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final EventService eventService;

    public SearchController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDocument>> searchEvents(@ModelAttribute EventSearchRequest request) {
        return ResponseEntity.ok(this.eventService.searchDocuments(request));
    }
}