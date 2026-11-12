package tn.enicarthage.speedenicar_projet.student.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.common.enums.ReportStatus;
import tn.enicarthage.speedenicar_projet.common.enums.UrgencyLevel;
import tn.enicarthage.speedenicar_projet.student.entity.DifficultyReport;

import java.util.List;

@Repository
public interface DifficultyReportRepository extends MongoRepository<DifficultyReport, Long> {

    @Query(value = "{ 'student.$id': ?0, 'deleted': false }", sort = "{ 'createdAt': -1 }")
    Page<DifficultyReport> findByStudentIdAndDeletedFalseOrderByCreatedAtDesc(Long studentId, Pageable pageable);

    @Query("{ 'student.$id': ?0, 'status': ?1, 'deleted': false }")
    List<DifficultyReport> findByStudentIdAndStatusAndDeletedFalse(Long studentId, ReportStatus status);

    @Query("{ 'student.$id': ?0, 'urgency': ?1, 'deleted': false }")
    List<DifficultyReport> findByStudentIdAndUrgencyAndDeletedFalse(Long studentId, UrgencyLevel urgency);

    @Query(value = "{ 'student.$id': ?0, 'status': ?1, 'deleted': false }", count = true)
    Long countByStudentIdAndStatusAndDeletedFalse(Long studentId, ReportStatus status);

    @Query(value = "{ 'student.$id': ?0 }", sort = "{ 'createdAt': -1 }")
    List<DifficultyReport> findByStudentIdOrderByCreatedAtDesc(Long studentId);
}
