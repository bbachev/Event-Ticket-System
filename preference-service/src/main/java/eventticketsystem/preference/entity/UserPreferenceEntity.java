package eventticketsystem.preference.entity;

import eventticketsystem.preference.dto.PreferenceCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_preference", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "category"}))
public class UserPreferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private PreferenceCategory category;

    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createdAt;
}
