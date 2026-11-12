package tn.enicarthage.speedenicar_projet.student.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.user.entity.User;

import java.time.LocalDate;

/**
 * Profil étudiant. Les anciennes collections liées (humeurs, journal, notes, absences,
 * rendez-vous, documents) ne sont plus embarquées : chaque document enfant référence
 * l'étudiant via son propre champ {@code student} et se lit via son repository.
 */
@Document(collection = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile extends BaseEntity {

    @DBRef
    @Indexed(unique = true)
    private User user;

    @NotBlank
    @Indexed(unique = true)
    private String studentId;

    private LocalDate dateOfBirth;

    @Indexed
    private String department;

    @Indexed
    private String level;

    private Integer enrollmentYear;
}
