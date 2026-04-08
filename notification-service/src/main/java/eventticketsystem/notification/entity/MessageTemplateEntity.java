package eventticketsystem.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@Entity
@Table(name = "message_template")
public class MessageTemplateEntity {

    @Id
    private String id;

    @Column(name = "channel")
    private String channel;

    @Column(name = "description")
    private String description;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body_html")
    private String bodyHtml;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

}
