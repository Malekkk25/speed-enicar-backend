package tn.enicarthage.speedenicar_projet.module_psychologue.consultation;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.module_psychologue.appointment.Appointment;

import java.time.LocalDateTime;
@Document(collection = "consultation_sessions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConsultationSession extends BaseEntity {

    @DBRef
    private Appointment appointment;

    private String roomId; // UUID généré à la création

    private SessionStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private LocalDateTime studentJoinedAt;

    private LocalDateTime psychologistJoinedAt;

    private Integer durationMinutes;

    public enum SessionStatus {
        WAITING,   // Créée, en attente que les deux participants rejoignent
        ACTIVE,    // Les deux participants sont connectés
        ENDED,     // Session terminée normalement
        ABANDONED  // Session abandonnée (timeout ou déconnexion)
    }
}
