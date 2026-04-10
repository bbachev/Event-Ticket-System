package eventticketsystem.gateway.dto.preference;

import eventticketsystem.gateway.dto.gateway.User;

import java.util.List;

public record UserPreference(User user, List<PreferenceCategory> preferences) {
}
