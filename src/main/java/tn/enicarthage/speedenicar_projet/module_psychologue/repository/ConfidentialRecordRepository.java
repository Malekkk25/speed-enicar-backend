package tn.enicarthage.speedenicar_projet.module_psychologue.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.common.enums.AlertSeverity;
import tn.enicarthage.speedenicar_projet.module_psychologue.entity.ConfidentialRecord;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConfidentialRecordRepository extends MongoRepository<ConfidentialRecord, Long> {

    @Query(value = "{ 'student.$id': ?0, 'psychologist.$id': ?1, 'deleted': false }", sort = "{ 'sessionDate': -1 }")
    Page<ConfidentialRecord> findByStudentIdAndPsychologistIdAndDeletedFalseOrderBySessionDateDesc(
            Long studentId, Long psychologistId, Pageable pageable);

    @Query(value = "{ 'psychologist.$id': ?0, 'deleted': false }", sort = "{ 'sessionDate': -1 }")
    Page<ConfidentialRecord> findByPsychologistIdAndDeletedFalseOrderBySessionDateDesc(
            Long psychologistId, Pageable pageable);

    @Query(value = "{ 'student.$id': ?0, 'deleted': false }", sort = "{ 'sessionDate': -1 }")
    List<ConfidentialRecord> findByStudentIdAndDeletedFalseOrderBySessionDateDesc(Long studentId);

    @Query(value = "{ 'psychologist.$id': ?0, 'riskLevel': { $in: ?1 }, 'deleted': false }",
            sort = "{ 'sessionDate': -1 }")
    List<ConfidentialRecord> findByPsychologistAndRiskLevels(Long psychologistId, List<AlertSeverity> riskLevels);

    @Query(value = "{ 'psychologist.$id': ?0, 'followUpRequired': true, 'deleted': false }",
            sort = "{ 'sessionDate': 1 }")
    List<ConfidentialRecord> findPendingFollowUps(Long psychologistId);

    @Query(value = "{ 'student.$id': ?0, 'deleted': false }", sort = "{ 'sessionDate': -1 }")
    List<ConfidentialRecord> findLatestByStudent(Long studentId, Pageable pageable);

    @Query(value = "{ 'psychologist.$id': ?0, 'sessionDate': { $gte: ?1, $lte: ?2 }, 'deleted': false }", count = true)
    long countByPsychologistIdAndSessionDateBetweenAndDeletedFalse(
            Long psychologistId, LocalDate startDate, LocalDate endDate);

    @Query(value = "{ 'psychologist.$id': ?0, 'deleted': false }")
    List<ConfidentialRecord> findAllByPsychologistId(Long psychologistId);

    default List<Long> findDistinctStudentIds(Long psychologistId) {
        return findAllByPsychologistId(psychologistId).stream()
                .map(r -> r.getStudent().getId())
                .distinct()
                .toList();
    }
}
