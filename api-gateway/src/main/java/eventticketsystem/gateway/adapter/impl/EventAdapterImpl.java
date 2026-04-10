package eventticketsystem.gateway.adapter.impl;

import eventticketsystem.gateway.adapter.EventAdapter;
import eventticketsystem.gateway.dto.event.EventFilterRequest;
import eventticketsystem.gateway.dto.event.EventRequest;
import eventticketsystem.gateway.dto.event.EventResponse;
import eventticketsystem.gateway.dto.event.PageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class EventAdapterImpl implements EventAdapter {
    private final RestTemplate restTemplate;

    @Value("${rest.event-service.url}")
    private String url;

    public EventAdapterImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public EventResponse createEvent(EventRequest request, String authHeader) {
        ResponseEntity<EventResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }

    @Override
    public EventResponse updateEvent(UUID eventId, EventRequest request, String authHeader) {
        ResponseEntity<EventResponse> response = restTemplate.exchange(
                url + eventId,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<>() {
                }
        );
        return response.getBody();
    }

    @Override
    public void deleteEvent(UUID eventId, String authHeader) {
        restTemplate.exchange(
                url + eventId,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    @Override
    public Optional<EventResponse> getEvent(UUID eventId, String authHeader) {
        return Optional.ofNullable(
                restTemplate.exchange(
                        url + eventId,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<EventResponse>() {
                        }
                ).getBody()
        );
    }

    @Override
    public PageDto<EventResponse> getAllEvents(EventFilterRequest filter, String authHeader) {

        ResponseEntity<PageDto<EventResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(filter),
                new ParameterizedTypeReference<>() {
                }
        );
        return response.getBody();
    }
}
