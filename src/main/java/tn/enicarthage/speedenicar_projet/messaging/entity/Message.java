package tn.enicarthage.speedenicar_projet.messaging.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;



import jakarta.validation.constraints.NotBlank;
import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.user.entity.User;

import java.time.LocalDateTime;

@Document(collection = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends BaseEntity {

    @DBRef
    private Conversation conversation;

    @DBRef
    private User sender;

    @DBRef
    private User receiver;

    @NotBlank
    private String content;

    /*@Column(name = "read_at")
    private LocalDateTime readAt;*/
    private LocalDateTime readAt;

    @Builder.Default
    private Boolean read = false;

    private String attachmentUrl;

    private String attachmentName;

    // ── Business methods ────────────────────────────────────

    public void markAsRead() {
        this.read = true;
        this.readAt = LocalDateTime.now();
    }

    public boolean isFromUser(Long userId) {
        return sender != null && sender.getId().equals(userId);
    }
}
