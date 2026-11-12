package tn.enicarthage.speedenicar_projet.module_psychologue.consultation;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultationSessionRepository extends MongoRepository<ConsultationSession, Long> {

    Optional<ConsultationSession> findByRoomId(String roomId);

    @Query("{ 'appointment.$id': ?0 }")
    Optional<ConsultationSession> findByAppointmentId(Long appointmentId);

    /** Session de la salle, uniquement si l'utilisateur est l'etudiant du rendez-vous. */
    default Optional<ConsultationSession> findByRoomIdAndStudentUserId(String roomId, Long userId) {
        return findByRoomId(roomId).filter(cs ->
                cs.getAppointment() != null
                        && cs.getAppointment().getStudent() != null
                        && cs.getAppointment().getStudent().getUser() != null
                        && userId.equals(cs.getAppointment().getStudent().getUser().getId()));
    }

    /** Session de la salle, uniquement si l'utilisateur est le psychologue du rendez-vous. */
    default Optional<ConsultationSession> findByRoomIdAndPsychologistUserId(String roomId, Long userId) {
        return findByRoomId(roomId).filter(cs ->
                cs.getAppointment() != null
                        && cs.getAppointment().getPsychologist() != null
                        && userId.equals(cs.getAppointment().getPsychologist().getId()));
    }
}
