package com.search.dto.request;

import org.springframework.data.elasticsearch.core.query.Criteria;

import java.time.OffsetDateTime;

public record EventSearchRequest(
        String query,
        String category,
        String location,
        OffsetDateTime dateFrom,
        OffsetDateTime dateTo,
        Integer priceFrom,
        Integer priceTo,
        String status,
        String sortBy,
        String sortDir,
        Integer page,
        Integer size
) {

    public Criteria toCriteria() {
        Criteria criteria = new Criteria();

        if (query != null && !query.isBlank()) {
            criteria = criteria.and(Criteria.where("name").contains(query)
                    .or(Criteria.where("description").contains(query)));
        }
        if (category != null) {
            criteria = criteria.and(Criteria.where("category").is(category));
        }
        if (location != null) {
            criteria = criteria.and(Criteria.where("location").contains(location));
        }
        if (dateFrom != null) {
            criteria = criteria.and(Criteria.where("eventDate").greaterThanEqual(dateFrom.toString()));
        }
        if (dateTo != null) {
            criteria = criteria.and(Criteria.where("eventDate").lessThanEqual(dateTo.toString()));
        }
        if (priceFrom != null) {
            criteria = criteria.and(Criteria.where("price").greaterThanEqual(priceFrom));
        }
        if (priceTo != null) {
            criteria = criteria.and(Criteria.where("price").lessThanEqual(priceTo));
        }
        if (status != null) {
            criteria = criteria.and(Criteria.where("status").is(status));
        }

        return criteria;
    }

}