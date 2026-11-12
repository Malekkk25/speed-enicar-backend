package tn.enicarthage.speedenicar_projet.module_psychologue.appointment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.module_psychologue.entity.TimeSlot;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface TimeSlotRepository extends MongoRepository<TimeSlot, Long> {

    @Query(value = "{ 'psychologist.$id': ?0 }", delete = true)
    void deleteByPsychologistId(Long psyId);

    @Query(value = "{ 'psychologist.$id': ?0, 'deleted': false }", sort = "{ 'dayOfWeek': 1, 'startTime': 1 }")
    List<TimeSlot> findByPsychologistIdAndDeletedFalseOrderByDayOfWeekAscStartTimeAsc(Long psychologistId);

    @Query("{ 'psychologist.$id': ?0, 'dayOfWeek': ?1, 'available': true, 'deleted': false }")
    List<TimeSlot> findByPsychologistIdAndDayOfWeekAndAvailableTrueAndDeletedFalse(
            Long psychologistId, DayOfWeek dayOfWeek);

    List<TimeSlot> findByDayOfWeekInAndAvailableTrueAndDeletedFalse(List<String> daysOfWeek);

    List<TimeSlot> findByDayOfWeekAndAvailableTrueAndDeletedFalse(DayOfWeek dayOfWeek);

    List<TimeSlot> findByAvailableTrue();
}
