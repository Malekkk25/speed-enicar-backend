package tn.enicarthage.speedenicar_projet.module_psychologue.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.user.entity.User;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Document(collection = "time_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlot extends BaseEntity {

    // On pointe vers User au lieu de PsychologistProfile
    @DBRef
    private User psychologist;

    private DayOfWeek dayOfWeek;
    private String startTime;
    private String endTime;
    @Builder.Default
    private boolean available = true;

}








