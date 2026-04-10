package eventticketsystem.gateway.adapter;


import eventticketsystem.gateway.dto.preference.PreferenceRequest;
import eventticketsystem.gateway.dto.preference.UserDetailsRequest;
import eventticketsystem.gateway.dto.preference.UserPreference;
import eventticketsystem.gateway.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface PreferenceAdapter {

    void addUserDetails(UserEntity userEntity);
    void addUserPreferences(UUID userId, PreferenceRequest request, String authHeader);
    Optional<UserPreference> getUserPreference(UUID userId, String authHeader);
}
