package eventticketsystem.notification.adapter.impl;
import eventticketsystem.notification.adapter.PreferenceClient;
import eventticketsystem.notification.dto.User;
import eventticketsystem.notification.dto.EventCategory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class PreferenceClientAdapter implements PreferenceClient {

    private final RestTemplate restTemplate;

    @Value("${rest.preference-service.url}")
    private String url;

    public PreferenceClientAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public List<User> getUsersByPreference(EventCategory category) {
        ResponseEntity<List<User>> response = restTemplate.exchange(
                url + category,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }
}