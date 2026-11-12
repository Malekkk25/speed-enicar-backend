package tn.enicarthage.speedenicar_projet.student.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.common.enums.MoodLevel;
import java.time.LocalDate;

/**
 * Entrée du mood tracker — suivi quotidien de l'humeur.
 *
 * Chaque étudiant peut enregistrer une entrée par jour.
 * Le moodLevel (1-5) est complété par un emoji et une note optionnelle.
 * Le champ activities stocke les activités du jour en JSON.
 *
 * Contrainte unique : (student_id, date) — une humeur par jour.
 */
@Document(collection = "mood_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodEntry extends BaseEntity {

    @DBRef
    private StudentProfile student;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer moodLevel;

    private MoodLevel moodLabel;

    private String emoji;

    private String note;

    @NotNull
    private LocalDate date;
    /**
     * Activités du jour — stockées en JSON.
     * Exemple : ["sport", "lecture", "amis"]
     */
    private String activities;
}

