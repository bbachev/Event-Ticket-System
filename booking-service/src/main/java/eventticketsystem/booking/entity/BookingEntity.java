package eventticketsystem.booking.entity;

import eventticketsystem.booking.dto.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;



@Getter
@Setter
@Entity
@Table(name="booking", indexes = {@Index(name = "idx__booking__user_id", columnList = "user_id")})
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "booked_tickets")
    private Integer bookedTickets;

    @Column(name = "total_price")
    private Long totalPrice;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
