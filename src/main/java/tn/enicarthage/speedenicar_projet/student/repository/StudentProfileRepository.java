package tn.enicarthage.speedenicar_projet.student.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.student.entity.StudentProfile;

import java.util.Optional;

@Repository
public interface StudentProfileRepository extends MongoRepository<StudentProfile, Long> {

    @Query("{ 'user.$id': ?0 }")
    Optional<StudentProfile> findByUserId(Long userId);

    boolean existsByStudentId(String studentId);

    @Query("{ 'user.$id': ?0, 'deleted': false }")
    Optional<StudentProfile> findByUserIdWithUser(Long userId);

    @Query("{ 'studentId': ?0, 'deleted': false }")
    Optional<StudentProfile> findByStudentIdWithUser(String studentId);
}
