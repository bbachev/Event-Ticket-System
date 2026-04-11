package eventticketsystem.gateway.adapter.impl;

import eventticketsystem.gateway.dto.event.EventResponse;
import eventticketsystem.gateway.dto.preference.PreferenceRequest;
import eventticketsystem.gateway.dto.preference.UserDetailsRequest;
import eventticketsystem.gateway.dto.preference.UserPreference;
import eventticketsystem.gateway.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PreferenceAdapterImpl implements eventticketsystem.gateway.adapter.PreferenceAdapter {

    private final RestTemplate restTemplate;

    @Value("${rest.preference-service.url}")
    private String url;

    public PreferenceAdapterImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void addUserDetails(UserEntity userEntity) {
        HttpEntity<UserDetailsRequest> requestEntity = new HttpEntity<>(
                new UserDetailsRequest(userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail())
        );

        restTemplate.exchange(
                url + "user-details/" + userEntity.getId(),
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );


    }

    @Override
    public void addUserPreferences(UUID userId, PreferenceRequest request) {
        restTemplate.exchange(
                url + userId,
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<>() {
                }
        );
    }

    @Override
    public Optional<UserPreference> getUserPreference(UUID userId) {
        HttpHeaders headers = new HttpHeaders();

        return Optional.ofNullable(
                restTemplate.exchange(
                        url + userId,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<UserPreference>() {
                        }
                ).getBody()
        );
    }
}