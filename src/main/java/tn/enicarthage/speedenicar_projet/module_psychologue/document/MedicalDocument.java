package tn.enicarthage.speedenicar_projet.module_psychologue.document;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
// Si tu utilises @SuperBuilder dans BaseEntity, remplace @Builder par @SuperBuilder ici aussi
import lombok.experimental.SuperBuilder;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.common.enums.DocStatus;
import tn.enicarthage.speedenicar_projet.student.entity.StudentProfile;
import tn.enicarthage.speedenicar_projet.user.entity.User;

import java.time.LocalDateTime;

@Document(collection = "medical_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalDocument extends BaseEntity {

    @DBRef
    private StudentProfile student;

    @DBRef
    private User uploadedBy;

    @NotBlank
    private String fileName;

    private String mimeType;

    @NotBlank
    private String filePath;  // chemin relatif uniquement

    private Long fileSize;

    private DocStatus status = DocStatus.PENDING;

    @DBRef
    private User validatedBy;

    private LocalDateTime validationDate;

    private String rejectionReason;

    public void validate(User agent) {
        this.status = DocStatus.VALIDATED;
        this.validatedBy = agent;
        this.validationDate = LocalDateTime.now();
    }

    public void reject(User agent, String reason) {
        this.status = DocStatus.REJECTED;
        this.validatedBy = agent;
        this.validationDate = LocalDateTime.now();
        this.rejectionReason = reason;
    }

    public boolean isPending() {
        return DocStatus.PENDING.equals(this.status);
    }
}