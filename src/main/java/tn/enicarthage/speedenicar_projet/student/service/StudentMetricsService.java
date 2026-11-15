package tn.enicarthage.speedenicar_projet.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.speedenicar_projet.student.entity.Absence;
import tn.enicarthage.speedenicar_projet.student.entity.StudentProfile;
import tn.enicarthage.speedenicar_projet.scolarity.repository.AcademicRecordRepository;
import tn.enicarthage.speedenicar_projet.student.repository.AbsenceRepository;

import java.util.List;

/** Calculs dérivés d'un étudiant (moyenne, absences) — remplace les anciennes collections JPA. */
@Service
@RequiredArgsConstructor
public class StudentMetricsService {

    private final AcademicRecordRepository academicRepo;
    private final AbsenceRepository absenceRepo;

    /** Moyenne en % ; {@code semester == null} = tous les semestres. */
    public double averageGrade(StudentProfile student, String semester) {
        return academicRepo.findByStudentIdAndDeletedFalseOrderBySemesterAsc(student.getId()).stream()
                .filter(r -> semester == null || semester.equals(r.getSemester()))
                .filter(r -> r.getGrade() != null && r.getMaxGrade() != null && r.getMaxGrade() != 0)
                .mapToDouble(r -> (r.getGrade() / r.getMaxGrade()) * 100)
                .average()
                .orElse(0.0);
    }

    public long countUnjustifiedAbsences(StudentProfile student) {
        return absenceRepo.findByStudentIdAndJustifiedFalseAndDeletedFalse(student.getId()).size();
    }

    public List<Absence> activeAbsences(StudentProfile student) {
        return absenceRepo.findActiveByStudentId(student.getId());
    }

    public long totalAbsenceDays(StudentProfile student) {
        return activeAbsences(student).stream().mapToLong(Absence::getDurationInDays).sum();
    }
}
