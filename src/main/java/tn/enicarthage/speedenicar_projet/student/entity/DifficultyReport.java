package tn.enicarthage.speedenicar_projet.student.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.common.enums.DifficultyType;
import tn.enicarthage.speedenicar_projet.common.enums.UrgencyLevel;
import tn.enicarthage.speedenicar_projet.common.enums.ReportStatus;
import tn.enicarthage.speedenicar_projet.user.entity.User;

@Document(collection = "difficulty_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DifficultyReport extends BaseEntity {

    @DBRef
    private StudentProfile student;

    @NotNull
    private DifficultyType type;

    @NotBlank
    private String description;

    @NotNull
    private UrgencyLevel urgency;

    @Builder.Default
    private ReportStatus status = ReportStatus.OPEN;

    @DBRef
    private User assignedTo;

    // ── Business methods ─────────────────────────────────────

    public boolean isOpen() {
        return ReportStatus.OPEN.equals(this.status);
    }

    public boolean isResolved() {
        return ReportStatus.RESOLVED.equals(this.status);
    }

    public boolean isCritical() {
        return UrgencyLevel.CRITICAL.equals(this.urgency);
    }
}