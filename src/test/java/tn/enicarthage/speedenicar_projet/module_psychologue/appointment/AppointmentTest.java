package tn.enicarthage.speedenicar_projet.module_psychologue.appointment;

import org.junit.jupiter.api.Test;
import tn.enicarthage.speedenicar_projet.common.enums.AppointmentStatus;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    @Test
    void cycleNominal_PENDING_CONFIRMED_COMPLETED() {
        Appointment a = new Appointment();
        assertEquals(AppointmentStatus.PENDING, a.getStatus());
        a.confirm();
        assertEquals(AppointmentStatus.CONFIRMED, a.getStatus());
        a.complete("Séance réalisée");
        assertEquals(AppointmentStatus.COMPLETED, a.getStatus());
    }

    @Test
    void annulationDepuisPENDING() {
        Appointment a = new Appointment();
        a.cancel(10L, "Indisponible");
        assertEquals(AppointmentStatus.CANCELLED, a.getStatus());
    }

    @Test
    void annulationDepuisCONFIRMED() {
        Appointment a = new Appointment();
        a.confirm();
        a.cancel(10L, "Indisponible");
        assertEquals(AppointmentStatus.CANCELLED, a.getStatus());
    }

    @Test
    void confirmerUnRdvTermineEstRefuse() {
        Appointment a = new Appointment();
        a.confirm();
        a.complete("RAS");
        assertThrows(IllegalStateException.class, a::confirm);
        assertEquals(AppointmentStatus.COMPLETED, a.getStatus());
    }

    @Test
    void terminerUnRdvAnnuleEstRefuse() {
        Appointment a = new Appointment();
        a.cancel(10L, "Indisponible");
        assertThrows(IllegalStateException.class, () -> a.complete("RAS"));
        assertEquals(AppointmentStatus.CANCELLED, a.getStatus());
    }

    @Test
    void statutParDefautAvecLeBuilder() {
        Appointment a = Appointment.builder().build();
        assertEquals(AppointmentStatus.PENDING, a.getStatus());
    }
}