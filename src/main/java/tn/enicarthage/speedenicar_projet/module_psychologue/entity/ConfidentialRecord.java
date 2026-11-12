package tn.enicarthage.speedenicar_projet.module_psychologue.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.common.enums.AlertSeverity;
import tn.enicarthage.speedenicar_projet.student.entity.StudentProfile;
import tn.enicarthage.speedenicar_projet.user.entity.User;

import java.time.LocalDate;

@Document(collection = "confidential_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ConfidentialRecord  extends BaseEntity {

    @DBRef
    private StudentProfile student ;

    @DBRef
    private User psychologist;

    /*@NotNull
    private LocalDate sessionDate;*/
    @NotNull
    private LocalDate sessionDate;

    private String observations;

    @NotNull
    @Builder.Default
    private AlertSeverity riskLevel =AlertSeverity.LOW;

    private String recommendations;

    @Builder.Default
    private Boolean followUpRequired =false;

    /*@Column(name = "next_session_date")
    private LocalDate nextSessionDate;*/
    private LocalDate nextSessionDate;

    @Builder.Default
    private Integer sessionDurationMinutes=30;

    private String interventions;

    private String studentProgress;

public Boolean isCritical(){
    return AlertSeverity.CRITICAL.equals(this.riskLevel)
            || AlertSeverity.HIGH.equals(this.riskLevel);
}

public void escalteRisk(AlertSeverity newLevel){
    if(newLevel.ordinal() > this.riskLevel.ordinal()){
        this.riskLevel=newLevel;
        this.followUpRequired=true;
    }
}
}
