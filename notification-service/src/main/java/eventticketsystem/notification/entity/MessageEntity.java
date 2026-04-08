package eventticketsystem.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "message")
public class MessageEntity {

    @Id
    private UUID id;

    @Column(name = "template_id")
    private String templateId;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "subject")
    private String subject;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
