package eventticketsystem.preference.service;

import eventticketsystem.preference.dto.PreferenceCategory;
import eventticketsystem.preference.dto.PreferenceRequest;
import eventticketsystem.preference.dto.UserDetailsRequest;
import eventticketsystem.preference.dto.UserPreference;

import java.util.UUID;

public interface PreferenceService {

    void searchByPreference(PreferenceCategory preference);
    UserPreference getUserPreferences(UUID userId);
    void addUserPreferences (UUID userId, PreferenceRequest request);
    void addUserDetails (UUID userId, UserDetailsRequest request);
}
   