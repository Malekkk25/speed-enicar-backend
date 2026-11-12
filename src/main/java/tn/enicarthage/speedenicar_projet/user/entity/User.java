package tn.enicarthage.speedenicar_projet.user.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;




import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.common.enums.Role;
import tn.enicarthage.speedenicar_projet.messaging.entity.Message;
import tn.enicarthage.speedenicar_projet.notification.entity.Notification;
import tn.enicarthage.speedenicar_projet.student.entity.StudentProfile;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "users")
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String phone;
    @Indexed
    private Role role; // STUDENT, PSYCHOLOGIST, etc.

    // --- CHAMPS AJOUTÉS (Anciennement dans PsychologistProfile) ---
    private String bio;
    private String specialty;
    private String licenseNumber;
    private Boolean enabled;

    private LocalDateTime lastLogin;
    // Ajoute ici tous les autres champs que tu avais dans PsychologistProfile
    public String getFullName() {
        if (firstName == null && lastName == null) return "";
        return this.firstName + " " + this.lastName;
    }
}
