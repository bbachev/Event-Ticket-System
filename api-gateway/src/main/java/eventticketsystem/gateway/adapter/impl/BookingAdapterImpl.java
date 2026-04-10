package eventticketsystem.gateway.adapter.impl;

import eventticketsystem.gateway.adapter.BookingAdapter;
import eventticketsystem.gateway.dto.booking.Booking;
import eventticketsystem.gateway.dto.booking.BookingRequest;
import eventticketsystem.gateway.dto.event.PageDto;
import eventticketsystem.gateway.service.impl.JwtServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Component
public class BookingAdapterImpl implements BookingAdapter {
    private final RestTemplate restTemplate;
    private final JwtServiceImpl jwtService;
    @Value("${rest.booking-service.url}")
    private String url;

    public BookingAdapterImpl(RestTemplate restTemplate, JwtServiceImpl jwtService) {
        this.restTemplate = restTemplate;
        this.jwtService = jwtService;
    }

    @Override
    public Booking createBooking(BookingRequest request, String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        headers.set("X-User-Email", email);
        headers.set("X-User-Id", jwtService.extractUserId(token));

        ResponseEntity<Booking> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<>() {
                }
        );
        return response.getBody();

    }

    @Override
    public PageDto<Booking> getAllBookingsForUser(UUID userId, int page, int size, String authHeader) {
        ResponseEntity<PageDto<Booking>> response = restTemplate.exchange(
                url + userId + "?page=" + page + "&size=" + size,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return response.getBody();

    }

    @Override
    public void deleteBooking(UUID id, String authHeader) {
        ResponseEntity<Void> response = restTemplate.exchange(
                url + id,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }
}
