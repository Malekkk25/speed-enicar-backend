package tn.enicarthage.speedenicar_projet.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base de tous les documents MongoDB.
 * L'id reste un Long (généré par {@link SequenceGeneratorService}) pour garder
 * le même contrat d'API qu'avec l'ancienne base SQL.
 */
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    @Id
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Boolean deleted = false;
}
