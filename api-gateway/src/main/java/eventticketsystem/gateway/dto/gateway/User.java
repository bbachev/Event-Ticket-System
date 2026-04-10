package eventticketsystem.gateway.dto.gateway;

import java.util.UUID;

public record User(UUID id, String firstName, String lastName, String email, UserRole role) {
}
