package eventticketsystem.booking.repository;

import eventticketsystem.booking.entity.TicketInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketInventoryRepository extends JpaRepository<TicketInventoryEntity, UUID> {
}
