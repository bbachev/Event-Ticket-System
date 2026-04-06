package eventticketsystem.booking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "ticket_inventory")
public class TicketInventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID eventId;

    @Column(name = "total_tickets")
    private Integer totalTickets;

    @Column(name = "available_tickets")
    private Integer availableTickets;

    @Column(name = "ticket_price")
    private Long ticketPrice;

    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
