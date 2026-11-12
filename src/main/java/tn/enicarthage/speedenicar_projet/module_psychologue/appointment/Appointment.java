package tn.enicarthage.speedenicar_projet.module_psychologue.appointment;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.common.enums.AppointmentStatus;
import tn.enicarthage.speedenicar_projet.common.enums.AppointmentType;
import tn.enicarthage.speedenicar_projet.common.enums.LocationType;
import tn.enicarthage.speedenicar_projet.student.entity.StudentProfile;
import tn.enicarthage.speedenicar_projet.user.entity.User;

import java.time.LocalDateTime;

@Document(collection = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment extends BaseEntity {

    @DBRef
    private StudentProfile student;

    @DBRef
    private User psychologist;

    @NotNull
    /*@Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;*/
    private LocalDateTime dateTime;

    @Builder.Default
    private Integer duration = 30;
    // Dans tn.enicarthage.speedenicar_projet.module_psychologue.appointment.Appointment
    private LocationType locationType;
    // (Ou private String locationType; si tu n'utilises pas d'Enum)
    @Builder.Default
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Builder.Default
    private AppointmentType type = AppointmentType.INITIAL;

    private String reason;

    private String notes;

    private String cancellationReason;

    private Long cancelledBy;

    // ── Business methods ────────────────────────────────────

    public void confirm() {
        if (this.status != AppointmentStatus.PENDING) {
            throw new IllegalStateException(
                    "Impossible de confirmer un RDV avec le statut : " + this.status
            );
        }
        this.status = AppointmentStatus.CONFIRMED;
    }

    /**
     * FIXED: On bloque SEULEMENT si le RDV est déjà COMPLETED.
     * Un RDV PENDING ou CONFIRMED peut être annulé.
     */
    public void cancel(Long userId, String reason) {
        if (this.status == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Un RDV COMPLETED ne peut pas être annulé"
            );
        }
        if (this.status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Ce RDV est déjà annulé"
            );
        }
        this.status = AppointmentStatus.CANCELLED;
        this.cancelledBy = userId;
        this.cancellationReason = reason;
    }



    public void complete(String sessionNotes) {
        if (this.status != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Seul un RDV CONFIRMED peut être marqué comme terminé"
            );
        }
        this.status = AppointmentStatus.COMPLETED;
        this.notes = sessionNotes;
    }

    public boolean isPast() {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }

    public boolean isUpcoming() {
        return dateTime != null
                && dateTime.isAfter(LocalDateTime.now())
                && status == AppointmentStatus.CONFIRMED;
    }
}
