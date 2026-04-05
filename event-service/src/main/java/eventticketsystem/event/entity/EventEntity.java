package eventticketsystem.event.entity;

import eventticketsystem.event.dto.EventCategory;
import eventticketsystem.event.dto.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "event")
@Entity
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category" )
    @Enumerated(EnumType.STRING)
    private EventCategory category;

    @Column(name = "location")
    @NonNull
    private String location;

    @Column(name = "event_date")
    @NonNull
    private OffsetDateTime eventDate;

    @Column(name = "price")
    @NonNull
    private Long price;

    @Column(name = "total_tickets")
    @NonNull
    private Integer totalTickets;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
