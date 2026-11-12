package tn.enicarthage.speedenicar_projet.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.speedenicar_projet.scolarity.repository.AcademicRecordRepository;
import tn.enicarthage.speedenicar_projet.student.entity.AcademicRecord;
import tn.enicarthage.speedenicar_projet.student.entity.StudentProfile;
import tn.enicarthage.speedenicar_projet.student.repository.StudentProfileRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicRecordService {

    private final AcademicRecordRepository academicRecordRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final tn.enicarthage.speedenicar_projet.user.repository.UserRepository userRepository;
    private final StudentMetricsService metrics;

    // ── Récupérer les notes (avec filtre semestre optionnel) ─

    public List<AcademicRecord> getGrades(String email, String semester) { // Changé en String email
        StudentProfile student = getStudentByEmail(email);

        if (semester != null && !semester.isBlank()) {
            return academicRecordRepository
                    .findByStudentIdAndSemesterAndDeletedFalse(student.getId(), semester);
        }

        return academicRecordRepository
                .findByStudentIdAndDeletedFalseOrderBySemesterAsc(student.getId());
    }

    // ── Calculer la moyenne générale ─────────────────────────

    public double getAverage(String email, String semester) { // Changé en String email
        StudentProfile student = getStudentByEmail(email);
        return metrics.averageGrade(student, semester);
    }

    // ── Helper privé ─────────────────────────────────────────

    private StudentProfile getStudentByEmail(String email) {
        return userRepository.findByEmail(email)
                .flatMap(u -> studentProfileRepository.findByUserId(u.getId()))
                .orElseThrow(() -> new RuntimeException("Profil étudiant introuvable pour l'email: " + email));
    }
}