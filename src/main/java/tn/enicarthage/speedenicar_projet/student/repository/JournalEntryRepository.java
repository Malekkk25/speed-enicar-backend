package tn.enicarthage.speedenicar_projet.student.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.student.entity.JournalEntry;

@Repository
public interface JournalEntryRepository extends MongoRepository<JournalEntry, Long> {

    @Query(value = "{ 'student.$id': ?0, 'deleted': false }", sort = "{ 'createdAt': -1 }")
    Page<JournalEntry> findByStudentIdAndDeletedFalseOrderByCreatedAtDesc(Long studentId, Pageable pageable);
}
