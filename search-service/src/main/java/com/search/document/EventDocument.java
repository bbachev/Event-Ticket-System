package com.search.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "events")
public class EventDocument {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private String location;
    private String eventDate;
    private Integer price;
    private Integer totalTickets;
    private String status;
}