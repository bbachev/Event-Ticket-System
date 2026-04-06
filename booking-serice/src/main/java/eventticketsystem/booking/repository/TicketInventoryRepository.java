package eventticketsystem.booking.repository;

import eventticketsystem.booking.entity.TicketInventoryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface TicketInventoryRepository extends JpaRepository<TicketInventoryEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TicketInventoryEntity> findById(UUID eventId);
}
