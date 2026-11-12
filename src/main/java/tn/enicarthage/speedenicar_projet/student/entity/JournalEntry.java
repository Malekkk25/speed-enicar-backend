package tn.enicarthage.speedenicar_projet.student.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.common.enums.MoodLevel;

@Document(collection = "journal_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry extends BaseEntity {

    @DBRef
    private StudentProfile student;

    @NotBlank
    private String title;

    /**
     * Contenu chiffré — utiliser @Convert(converter = EncryptionConverter.class)
     * en production pour le chiffrement transparent.
     */
    private String content;

    private MoodLevel mood;

    @Builder.Default
    private Boolean isPrivate = true;
}
