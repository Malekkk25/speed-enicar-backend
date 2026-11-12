package tn.enicarthage.speedenicar_projet.messaging.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;



import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "conversations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation extends BaseEntity {

    @DBRef
    private User participantOne;

    @DBRef
    private User participantTwo;

   /* @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;*/
   private LocalDateTime lastMessageAt;

    private String lastMessagePreview;
    // ── Helpers ──────────────────────────────────────────────

    public boolean involvesUser(Long userId) {
        return (participantOne != null && participantOne.getId().equals(userId))
                || (participantTwo != null && participantTwo.getId().equals(userId));
    }

    public User getOtherParticipant(Long currentUserId) {
        if (participantOne != null && participantOne.getId().equals(currentUserId)) {
            return participantTwo;
        }
        return participantOne;
    }

    public void updateLastMessage(String preview) {
        this.lastMessageAt = LocalDateTime.now();
        this.lastMessagePreview = preview != null && preview.length() > 255
                ? preview.substring(0, 252) + "..."
                : preview;
    }
}
