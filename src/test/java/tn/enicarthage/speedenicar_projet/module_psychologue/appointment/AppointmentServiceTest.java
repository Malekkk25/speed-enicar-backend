package tn.enicarthage.speedenicar_projet.module_psychologue.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.enicarthage.speedenicar_projet.common.enums.AppointmentStatus;
import tn.enicarthage.speedenicar_projet.common.exception.BusinessException;
import tn.enicarthage.speedenicar_projet.module_psychologue.PsychologistService;
import tn.enicarthage.speedenicar_projet.module_psychologue.appointment.Appointment;
import tn.enicarthage.speedenicar_projet.module_psychologue.appointment.AppointmentRepository;
import tn.enicarthage.speedenicar_projet.module_psychologue.dto.AppointmentResponse;
import tn.enicarthage.speedenicar_projet.student.entity.StudentProfile;
import tn.enicarthage.speedenicar_projet.user.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    private static final Long PSY_ID = 5L;
    private static final Long RDV_ID = 100L;

    @Mock
    private AppointmentRepository appointmentRepo;

    @InjectMocks
    private PsychologistService service;

    private Appointment rdv;

    @BeforeEach
    void setUp() {
        User psy = User.builder().firstName("Leila").lastName("Psy").build();
        psy.setId(PSY_ID);

        User etudiantUser = User.builder().firstName("Sami").lastName("Etudiant").build();
        etudiantUser.setId(1L);

        StudentProfile etudiant = new StudentProfile();
        etudiant.setId(1L);
        etudiant.setUser(etudiantUser);
        etudiant.setDepartment("Informatique");

        rdv = Appointment.builder().student(etudiant).psychologist(psy).build();
        rdv.setId(RDV_ID);
    }

    private void repositoryRenvoieLeRdv() {
        when(appointmentRepo.findById(RDV_ID)).thenReturn(Optional.of(rdv));
    }

    private void saveRenvoieSonArgument() {
        when(appointmentRepo.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void etape1_confirmerUnRdvPending() {
        repositoryRenvoieLeRdv();
        saveRenvoieSonArgument();

        AppointmentResponse r = service.confirmAppointment(PSY_ID, RDV_ID);

        assertEquals(AppointmentStatus.CONFIRMED, r.getStatus());
        verify(appointmentRepo, times(1)).save(rdv);
    }

    @Test
    void etape2_terminerUnRdvConfirme() {
        rdv.confirm();
        repositoryRenvoieLeRdv();
        saveRenvoieSonArgument();

        AppointmentResponse r = service.completeAppointment(PSY_ID, RDV_ID, "Séance réalisée");

        assertEquals(AppointmentStatus.COMPLETED, r.getStatus());
        verify(appointmentRepo, times(1)).save(rdv);
    }

    @Test
    void etape3_annulerUnRdvPending() {
        repositoryRenvoieLeRdv();
        saveRenvoieSonArgument();

        AppointmentResponse r = service.cancelAppointment(PSY_ID, RDV_ID, "Indisponible");

        assertEquals(AppointmentStatus.CANCELLED, r.getStatus());
        verify(appointmentRepo, times(1)).save(rdv);
    }

    @Test
    void etape4_confirmerUnRdvTermineEstRefuse() {
        rdv.confirm();
        rdv.complete("RAS");
        repositoryRenvoieLeRdv();

        assertThrows(IllegalStateException.class, () -> service.confirmAppointment(PSY_ID, RDV_ID));

        assertEquals(AppointmentStatus.COMPLETED, rdv.getStatus());
        verify(appointmentRepo, never()).save(any(Appointment.class));
    }

    @Test
    void etape5_terminerUnRdvAnnuleEstRefuse() {
        rdv.cancel(PSY_ID, "Indisponible");
        repositoryRenvoieLeRdv();

        assertThrows(IllegalStateException.class, () -> service.completeAppointment(PSY_ID, RDV_ID, "RAS"));

        assertEquals(AppointmentStatus.CANCELLED, rdv.getStatus());
        verify(appointmentRepo, never()).save(any(Appointment.class));
    }

    @Test
    void bonus_unAutrePsychologueNePeutPasConfirmer() {
        repositoryRenvoieLeRdv();

        assertThrows(BusinessException.class, () -> service.confirmAppointment(99L, RDV_ID));

        assertEquals(AppointmentStatus.PENDING, rdv.getStatus());
        verify(appointmentRepo, never()).save(any(Appointment.class));
    }
}