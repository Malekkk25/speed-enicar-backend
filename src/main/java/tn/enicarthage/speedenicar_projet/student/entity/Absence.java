package tn.enicarthage.speedenicar_projet.student.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.module_psychologue.document.MedicalDocument;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Document(collection = "absences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Absence extends BaseEntity {

    @DBRef
    private StudentProfile student;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String reason;

    @Builder.Default
    private Boolean justified = false;

    @DBRef
    private MedicalDocument document;

    private String subject;

    private String reportedBy;

    // ── Business methods ────────────────────────────────────

    public long getDurationInDays() {
        if (startDate == null || endDate == null) return 0;
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public boolean isProlonged() {
        return getDurationInDays() > 3;
    }

    public boolean isOngoing() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public void justify(MedicalDocument doc) {
        this.document = doc;
        this.justified = true;
    }

    /** Appelé par les services avant sauvegarde (remplace @PrePersist/@PreUpdate JPA). */
    public void validateDates() {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "La date de fin ne peut pas être antérieure à la date de début"
            );
        }
    }
}

