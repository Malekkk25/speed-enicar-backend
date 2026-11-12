package tn.enicarthage.speedenicar_projet.student.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;

@Document(collection = "academic_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicRecord extends BaseEntity {

    @DBRef
    private StudentProfile student;

    @NotBlank
    private String subject;

    @NotNull
    @PositiveOrZero
    private Double grade;

    @NotNull
    private Double maxGrade;

    private String semester;

    private String academicYear;

    @Builder.Default
    private Double coefficient = 1.0;

    // ─── Helpers ────────────────────────────────────────────────

    /**
     * Retourne le pourcentage de réussite (grade / maxGrade * 100).
     */
    public double getPercentage() {
        if (maxGrade == null || maxGrade == 0) return 0;
        return (grade / maxGrade) * 100;
    }

    /**
     * Vérifie si la note est au-dessus de la moyenne (>= 50%).
     */
    public boolean isPassing() {
        return getPercentage() >= 50.0;
    }
}

