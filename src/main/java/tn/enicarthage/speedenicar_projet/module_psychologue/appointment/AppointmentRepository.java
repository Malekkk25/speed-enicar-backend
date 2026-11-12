package tn.enicarthage.speedenicar_projet.module_psychologue.appointment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.common.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, Long> {

    @Query(value = "{ 'student.$id': ?0, 'deleted': false }", sort = "{ 'dateTime': -1 }")
    Page<Appointment> findByStudentIdAndDeletedFalseOrderByDateTimeDesc(Long studentId, Pageable pageable);

    @Query(value = "{ 'psychologist.$id': ?0, 'deleted': false }", sort = "{ 'dateTime': -1 }")
    Page<Appointment> findByPsychologistIdAndDeletedFalseOrderByDateTimeDesc(Long psychologistId, Pageable pageable);

    @Query(value = "{ 'psychologist.$id': ?0, 'status': ?1, 'deleted': false }", sort = "{ 'dateTime': 1 }")
    List<Appointment> findByPsychologistIdAndStatusAndDeletedFalseOrderByDateTimeAsc(
            Long psychologistId, AppointmentStatus status);

    @Query(value = "{ 'psychologist.$id': ?0, 'dateTime': { $gte: ?1, $lte: ?2 }, "
            + "'status': { $in: ?3 }, 'deleted': false }", sort = "{ 'dateTime': 1 }")
    List<Appointment> findByPsychologistAndDateRange(
            Long psychologistId, LocalDateTime start, LocalDateTime end, List<AppointmentStatus> statuses);

    @Query("{ 'psychologist.$id': ?0, 'dateTime': { $gte: ?1, $lte: ?2 }, "
            + "'status': { $ne: 'CANCELLED' }, 'deleted': false }")
    List<Appointment> findOccupiedSlots(Long psychologistId, LocalDateTime start, LocalDateTime end);

    @Query(value = "{ 'psychologist.$id': ?0, 'status': ?1, 'deleted': false }", count = true)
    long countByPsychologistIdAndStatusAndDeletedFalse(Long psychologistId, AppointmentStatus status);

    @Query(value = "{ 'psychologist.$id': ?0, 'dateTime': { $gte: ?1, $lte: ?2 }, "
            + "'status': { $ne: ?3 }, 'deleted': false }", count = true)
    long countByPsychologistIdAndDateTimeBetweenAndStatusNotAndDeletedFalse(
            Long psychologistId, LocalDateTime start, LocalDateTime end, AppointmentStatus excludeStatus);

    @Query(value = "{ 'psychologist.$id': ?0, 'status': 'PENDING', 'deleted': false }", sort = "{ 'dateTime': 1 }")
    List<Appointment> findPendingRequests(Long psychologistId);

    List<Appointment> findByDateTimeAfterAndStatusNot(LocalDateTime now, AppointmentStatus status);

    @Query(value = "{ 'psychologist.$id': ?0, 'dateTime': ?1, 'status': { $ne: ?2 } }", exists = true)
    boolean existsByPsychologistIdAndDateTimeAndStatusNot(Long psyId, LocalDateTime dateTime, AppointmentStatus status);

    @Query("{ 'student.$id': ?0 }")
    List<Appointment> findByStudentId(Long studentId);

    @Query("{ 'student.$id': ?0, 'dateTime': { $gte: ?1, $lte: ?2 } }")
    List<Appointment> findByStudentIdAndDateTimeBetween(Long studentId, LocalDateTime start, LocalDateTime end);

    @Query(value = "{ 'student.$id': ?0 }", sort = "{ 'dateTime': -1 }")
    Page<Appointment> findByStudentIdOrderByDateTimeDesc(Long studentId, Pageable pageable);

    long countByStatus(String status);
}
