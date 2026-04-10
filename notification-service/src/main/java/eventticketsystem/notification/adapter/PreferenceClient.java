package eventticketsystem.notification.adapter;

import eventticketsystem.notification.dto.EventCategory;
import eventticketsystem.notification.dto.User;

import java.util.List;

public interface PreferenceClient {

    List<User> getUsersByPreference(EventCategory category);
}
