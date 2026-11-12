package tn.enicarthage.speedenicar_projet.scolarity.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.student.entity.AcademicRecord;

import java.util.List;

@Repository
public interface AcademicRecordRepository extends MongoRepository<AcademicRecord, Long> {

    @Query("{ 'student.$id': ?0 }")
    List<AcademicRecord> findByStudentId(Long studentId);

    @Query("{ 'student.$id': ?0, 'semester': ?1 }")
    List<AcademicRecord> findByStudentIdAndSemester(Long studentId, String semester);

    @Query(value = "{ 'student.$id': ?0, 'deleted': false }", sort = "{ 'semester': 1 }")
    List<AcademicRecord> findActiveByStudentId(Long studentId);

    @Query(value = "{ 'student.$id': ?0, 'deleted': false }", sort = "{ 'semester': 1 }")
    List<AcademicRecord> findByStudentIdAndDeletedFalseOrderBySemesterAsc(Long studentId);

    @Query("{ 'student.$id': ?0, 'semester': ?1, 'deleted': false }")
    List<AcademicRecord> findByStudentIdAndSemesterAndDeletedFalse(Long studentId, String semester);
}
