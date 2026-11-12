package tn.enicarthage.speedenicar_projet.notification.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.common.enums.NotificationType;
import tn.enicarthage.speedenicar_projet.user.entity.User;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @DBRef
    private User recipient;

    @NotBlank
    private String title;

    @NotBlank
    private String message;

    @NotNull
    private NotificationType type;

    @Builder.Default
    private Boolean read = false;

    /*@Column(name = "read_at")
    private LocalDateTime readAt;*/
    private LocalDateTime readAt;

    private String link;

    private String senderName;

    // ── Business methods ────────────────────────────────────

    public void markAsRead() {
        this.read = true;
        this.readAt = LocalDateTime.now();
    }
}
