package tn.enicarthage.speedenicar_projet.student.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.student.entity.Absence;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbsenceRepository extends MongoRepository<Absence, Long> {

    @Query(value = "{ 'student.$id': ?0, 'deleted': false }", sort = "{ 'startDate': -1 }")
    Page<Absence> findByStudentIdAndDeletedFalseOrderByStartDateDesc(Long studentId, Pageable pageable);

    @Query("{ 'student.$id': ?0, 'justified': false, 'deleted': false }")
    List<Absence> findByStudentIdAndJustifiedFalseAndDeletedFalse(Long studentId);

    @Query(value = "{ 'student.$id': ?0, 'deleted': false }", sort = "{ 'startDate': -1 }")
    List<Absence> findActiveByStudentId(Long studentId);

    @Query(value = "{ 'student.$id': ?0, 'startDate': { $gte: ?1 }, 'endDate': { $lte: ?2 }, 'deleted': false }",
            sort = "{ 'startDate': -1 }")
    List<Absence> findByStudentIdAndPeriod(Long studentId, LocalDate startDate, LocalDate endDate);

    /** Absences injustifiees de plus de 3 jours (3 * 86 400 000 ms). */
    @Query(value = "{ 'deleted': false, 'justified': false, "
            + "$expr: { $gt: [ { $subtract: ['$endDate', '$startDate'] }, 259200000 ] } }",
            sort = "{ 'startDate': -1 }")
    Page<Absence> findProlongedUnjustified(Pageable pageable);

    @Query(value = "{ 'student.$id': ?0, 'justified': false, 'deleted': false }", count = true)
    Long countUnjustified(Long studentId);

    @Query(value = "{ 'student.$id': ?0, 'deleted': false, 'startDate': { $gte: ?1 } }", count = true)
    Long getTotalAbsenceDays(Long studentId, LocalDate startDate);
}
