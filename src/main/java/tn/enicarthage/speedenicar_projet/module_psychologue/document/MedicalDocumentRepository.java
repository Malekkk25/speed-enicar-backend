package tn.enicarthage.speedenicar_projet.module_psychologue.document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.common.enums.DocStatus;

import java.util.List;

@Repository
public interface MedicalDocumentRepository extends MongoRepository<MedicalDocument, Long> {

    // ── Etudiant ────────────────────────────────────────────────

    @Query(value = "{ 'student.$id': ?0 }", sort = "{ 'createdAt': -1 }")
    List<MedicalDocument> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    @Query(value = "{ 'student.$id': ?0, 'status': ?1 }", sort = "{ 'createdAt': -1 }")
    List<MedicalDocument> findByStudentIdAndStatusOrderByCreatedAtDesc(Long studentId, DocStatus status);

    @Query(value = "{ 'student.$id': ?0 }", count = true)
    long countByStudentId(Long studentId);

    @Query(value = "{ 'student.$id': ?0, 'fileName': ?1 }", exists = true)
    boolean existsByStudentIdAndFileName(Long studentId, String fileName);

    // ── Psychologue / scolarite ─────────────────────────────────

    List<MedicalDocument> findByStatusOrderByCreatedAtAsc(DocStatus status);

    Page<MedicalDocument> findByStatusOrderByCreatedAtAsc(DocStatus status, Pageable pageable);

    long countByStatus(DocStatus status);

    // ── Securite & download ─────────────────────────────────────

    @Query(value = "{ '_id': ?0, 'status': ?1 }", exists = true)
    boolean existsByIdAndStatus(Long id, DocStatus status);

    // ── Recherche ───────────────────────────────────────────────

    default List<MedicalDocument> findByStudentIdWithStudent(Long studentId) {
        return findByStudentIdOrderByCreatedAtDesc(studentId);
    }

    default List<MedicalDocument> findPendingWithStudentInfo() {
        return findByStatusOrderByCreatedAtAsc(DocStatus.PENDING);
    }

    List<MedicalDocument> findByFileNameContainingIgnoreCaseOrderByCreatedAtDesc(String fileName);
}
