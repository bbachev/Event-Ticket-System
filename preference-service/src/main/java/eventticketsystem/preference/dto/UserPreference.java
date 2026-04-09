package eventticketsystem.preference.dto;

import java.util.List;

public record UserPreference(User user, List<PreferenceCategory> preferences) {
}
