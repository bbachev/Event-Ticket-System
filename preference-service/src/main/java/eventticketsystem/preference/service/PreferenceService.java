package eventticketsystem.preference.service;

import eventticketsystem.preference.dto.*;

import java.util.List;
import java.util.UUID;

public interface PreferenceService {

    List<User> searchByPreference(PreferenceCategory preference);
    UserPreference getUserPreferences(UUID userId);
    void addUserPreferences (UUID userId, PreferenceRequest request);
    void addUserDetails (UUID userId, UserDetailsRequest request);
}
   