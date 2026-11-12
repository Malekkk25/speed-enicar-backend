package tn.enicarthage.speedenicar_projet.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tn.enicarthage.speedenicar_projet.common.enums.*;
import tn.enicarthage.speedenicar_projet.messaging.entity.Conversation;
import tn.enicarthage.speedenicar_projet.messaging.entity.Message;
import tn.enicarthage.speedenicar_projet.messaging.repository.ConversationRepository;
import tn.enicarthage.speedenicar_projet.messaging.repository.MessageRepository;
import tn.enicarthage.speedenicar_projet.module_psychologue.appointment.Appointment;
import tn.enicarthage.speedenicar_projet.module_psychologue.appointment.AppointmentRepository;
import tn.enicarthage.speedenicar_projet.module_psychologue.appointment.TimeSlotRepository;
import tn.enicarthage.speedenicar_projet.module_psychologue.consultation.ConsultationSession;
import tn.enicarthage.speedenicar_projet.module_psychologue.consultation.ConsultationSessionRepository;
import tn.enicarthage.speedenicar_projet.module_psychologue.document.MedicalDocument;
import tn.enicarthage.speedenicar_projet.module_psychologue.document.MedicalDocumentRepository;
import tn.enicarthage.speedenicar_projet.module_psychologue.entity.ConfidentialRecord;
import tn.enicarthage.speedenicar_projet.module_psychologue.entity.TimeSlot;
import tn.enicarthage.speedenicar_projet.module_psychologue.repository.ConfidentialRecordRepository;
import tn.enicarthage.speedenicar_projet.notification.entity.Notification;
import tn.enicarthage.speedenicar_projet.notification.repository.NotificationRepository;
import tn.enicarthage.speedenicar_projet.scolarity.repository.AcademicRecordRepository;
import tn.enicarthage.speedenicar_projet.student.entity.*;
import tn.enicarthage.speedenicar_projet.student.repository.*;
import tn.enicarthage.speedenicar_projet.user.entity.User;
import tn.enicarthage.speedenicar_projet.user.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Données de démonstration : comptes (admin, scolarité, psychologues, étudiants) et
 * un jeu de données complet (humeurs, journal, notes, absences, rendez-vous, dossiers,
 * messages, notifications...). Idempotent : ne fait rien si les données existent déjà.
 * Désactivable avec {@code app.seed.enabled=false}.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final StudentProfileRepository studentRepo;
    private final MoodEntryRepository moodRepo;
    private final JournalEntryRepository journalRepo;
    private final AcademicRecordRepository gradeRepo;
    private final AbsenceRepository absenceRepo;
    private final DifficultyReportRepository reportRepo;
    private final TimeSlotRepository slotRepo;
    private final AppointmentRepository appointmentRepo;
    private final ConsultationSessionRepository sessionRepo;
    private final ConfidentialRecordRepository recordRepo;
    private final MedicalDocumentRepository documentRepo;
    private final ConversationRepository conversationRepo;
    private final MessageRepository messageRepo;
    private final NotificationRepository notificationRepo;
    private final PasswordEncoder passwordEncoder;

    private final Random rnd = new Random(42);

    @Override
    public void run(String... args) {
        User admin = user("admin@enicar.tn", "Admin@123", "Admin", "ENICar", Role.ADMIN);
        User scolarity = user("scolarite@enicar.tn", "Scolarite@123", "Mariem", "Gharbi", Role.SCOLARITY);
        User psy1 = psychologist("psy@enicar.tn", "Psy@12345", "Sarra", "Ben Ali",
                "Psychologie clinique", "PSY-0001");
        User psy2 = psychologist("psy2@enicar.tn", "Psy@12345", "Karim", "Jlassi",
                "Thérapie cognitivo-comportementale", "PSY-0002");

        String[][] people = {
                {"student@enicar.tn", "Ahmed", "Trabelsi", "STU-0001", "Informatique", "2"},
                {"yasmine@enicar.tn", "Yasmine", "Khelifi", "STU-0002", "Génie Civil", "3"},
                {"omar@enicar.tn", "Omar", "Mansour", "STU-0003", "Informatique", "1"},
                {"ines@enicar.tn", "Ines", "Chaabane", "STU-0004", "Génie Mécanique", "2"},
                {"mehdi@enicar.tn", "Mehdi", "Bouzid", "STU-0005", "Génie Électrique", "3"},
                {"salma@enicar.tn", "Salma", "Jebali", "STU-0006", "Informatique", "3"},
        };
        List<StudentProfile> students = new ArrayList<>();
        for (String[] p : people) {
            User u = user(p[0], "Student@123", p[1], p[2], Role.STUDENT);
            students.add(profile(u, p[3], p[4], p[5]));
        }

        if (gradeRepo.count() > 0) {
            log.info("Données de démonstration déjà présentes : seed des données ignoré");
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            // profil de risque : 0 = tout va bien ... 2 = étudiant en difficulté
            int risk = i == 2 ? 2 : (i == 4 ? 2 : (i == 1 ? 1 : 0));
            seedStudentData(students.get(i), risk);
        }

        seedSchedule(psy1);
        seedSchedule(psy2);
        seedAppointmentsAndRecords(students, psy1, psy2, scolarity);
        seedMessagingAndNotifications(students, psy1, scolarity, admin);

        log.info("Données de démonstration créées ({} étudiants)", students.size());
    }

    // ------------------------------------------------------------------ comptes

    private User user(String email, String pwd, String first, String last, Role role) {
        return userRepo.findByEmail(email).orElseGet(() -> {
            User u = userRepo.save(User.builder()
                    .email(email).password(passwordEncoder.encode(pwd))
                    .firstName(first).lastName(last).role(role)
                    .phone("2" + (1000000 + rnd.nextInt(8999999)))
                    .enabled(true).build());
            log.info("Compte créé : {} ({})", email, role);
            return u;
        });
    }

    private User psychologist(String email, String pwd, String first, String last, String specialty, String license) {
        User u = user(email, pwd, first, last, Role.PSYCHOLOGIST);
        if (u.getLicenseNumber() == null) {
            u.setSpecialty(specialty);
            u.setLicenseNumber(license);
            u.setBio("Psychologue à l'écoute des étudiants de l'ENICarthage.");
            u = userRepo.save(u);
        }
        return u;
    }

    private StudentProfile profile(User u, String studentId, String dept, String level) {
        return studentRepo.findAll().stream()
                .filter(s -> s.getUser() != null && u.getId().equals(s.getUser().getId()))
                .findFirst()
                .orElseGet(() -> studentRepo.save(StudentProfile.builder()
                        .user(u).studentId(studentId).department(dept).level(level)
                        .dateOfBirth(LocalDate.of(2002 + rnd.nextInt(3), 1 + rnd.nextInt(12), 1 + rnd.nextInt(28)))
                        .enrollmentYear(2023 - Integer.parseInt(level) + 1)
                        .build()));
    }

    // ------------------------------------------------------------ données étudiant

    private void seedStudentData(StudentProfile s, int risk) {
        LocalDate today = LocalDate.now();

        // Humeurs : 14 derniers jours (plus basses pour les profils à risque)
        String[] emojis = {"", "😢", "😞", "😐", "🙂", "😄"};
        MoodLevel[] labels = {null, MoodLevel.VERY_SAD, MoodLevel.SAD, MoodLevel.NEUTRAL, MoodLevel.HAPPY, MoodLevel.VERY_HAPPY};
        for (int d = 13; d >= 0; d--) {
            int base = risk == 2 ? 2 : (risk == 1 ? 3 : 4);
            int level = Math.max(1, Math.min(5, base + rnd.nextInt(3) - 1));
            moodRepo.save(MoodEntry.builder().student(s).moodLevel(level).moodLabel(labels[level])
                    .emoji(emojis[level]).date(today.minusDays(d))
                    .note(level <= 2 ? "Journée difficile, beaucoup de stress." : "Journée correcte.")
                    .activities(level >= 4 ? "Sport, amis" : "Révisions").build());
        }

        // Journal
        journalRepo.save(JournalEntry.builder().student(s).title("Début de semestre")
                .content("Beaucoup de choses à gérer entre les cours et les projets.")
                .mood(risk == 2 ? MoodLevel.SAD : MoodLevel.NEUTRAL).isPrivate(true).build());
        journalRepo.save(JournalEntry.builder().student(s).title("Petite victoire")
                .content("J'ai enfin compris le chapitre qui me bloquait.")
                .mood(MoodLevel.HAPPY).isPrivate(true).build());

        // Notes
        String[] subjects = {"Algorithmique", "Bases de données", "Mathématiques", "Anglais", "Réseaux"};
        for (String subject : subjects) {
            double grade = risk == 2 ? 5 + rnd.nextInt(7) : (risk == 1 ? 9 + rnd.nextInt(6) : 12 + rnd.nextInt(7));
            gradeRepo.save(AcademicRecord.builder().student(s).subject(subject)
                    .grade(grade).maxGrade(20.0).semester("S1").academicYear("2025-2026")
                    .coefficient(1.0 + rnd.nextInt(3)).build());
        }

        // Absences
        int absences = risk == 2 ? 4 : (risk == 1 ? 2 : 1);
        for (int a = 0; a < absences; a++) {
            LocalDate start = today.minusDays(5L + a * 12L);
            boolean prolonged = risk == 2 && a == 0;
            absenceRepo.save(Absence.builder().student(s).startDate(start)
                    .endDate(prolonged ? start.plusDays(5) : start)
                    .reason(a % 2 == 0 ? "Maladie" : "Raisons personnelles")
                    .justified(risk == 0).subject(subjects[a % subjects.length])
                    .reportedBy("Scolarité").build());
        }

        // Signalement de difficulté
        if (risk > 0) {
            reportRepo.save(DifficultyReport.builder().student(s)
                    .type(risk == 2 ? DifficultyType.ACADEMIC : DifficultyType.SOCIAL)
                    .description(risk == 2 ? "Je n'arrive plus à suivre les cours et je perds confiance."
                            : "Difficulté à m'intégrer dans mon groupe de projet.")
                    .urgency(risk == 2 ? UrgencyLevel.HIGH : UrgencyLevel.MEDIUM)
                    .status(ReportStatus.OPEN).build());
        }
    }

    // ------------------------------------------------------------------ planning

    private void seedSchedule(User psy) {
        for (DayOfWeek day : new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY}) {
            for (String[] range : new String[][]{{"09:00", "09:30"}, {"09:30", "10:00"}, {"10:00", "10:30"},
                    {"14:00", "14:30"}, {"14:30", "15:00"}, {"15:00", "15:30"}}) {
                slotRepo.save(TimeSlot.builder().psychologist(psy).dayOfWeek(day)
                        .startTime(range[0]).endTime(range[1]).available(true).build());
            }
        }
    }

    private LocalDateTime at(int plusDays, int hour, int minute) {
        return LocalDate.now().plusDays(plusDays).atTime(LocalTime.of(hour, minute));
    }

    private void seedAppointmentsAndRecords(List<StudentProfile> st, User psy1, User psy2, User scolarity) {
        // Séance terminée + dossier confidentiel + session de visio terminée
        Appointment done = appointmentRepo.save(Appointment.builder().student(st.get(2)).psychologist(psy1)
                .dateTime(at(-7, 10, 0)).locationType(LocationType.ONLINE)
                .status(AppointmentStatus.COMPLETED).type(AppointmentType.INITIAL)
                .reason("Stress des examens").notes("Première séance, bonne ouverture.").build());
        sessionRepo.save(ConsultationSession.builder().appointment(done).roomId(UUID.randomUUID().toString())
                .status(ConsultationSession.SessionStatus.ENDED)
                .startedAt(done.getDateTime()).endedAt(done.getDateTime().plusMinutes(30))
                .studentJoinedAt(done.getDateTime()).psychologistJoinedAt(done.getDateTime())
                .durationMinutes(30).build());
        recordRepo.save(ConfidentialRecord.builder().student(st.get(2)).psychologist(psy1)
                .sessionDate(done.getDateTime().toLocalDate())
                .observations("Anxiété liée aux examens, sommeil perturbé.")
                .riskLevel(AlertSeverity.HIGH).recommendations("Techniques de respiration, planning de révision.")
                .followUpRequired(true).nextSessionDate(LocalDate.now().plusDays(3))
                .sessionDurationMinutes(30).interventions("Relaxation guidée")
                .studentProgress("Motivé à suivre les conseils").build());

        recordRepo.save(ConfidentialRecord.builder().student(st.get(4)).psychologist(psy2)
                .sessionDate(LocalDate.now().minusDays(10))
                .observations("Découragement, baisse de résultats.")
                .riskLevel(AlertSeverity.CRITICAL).recommendations("Suivi hebdomadaire, tutorat.")
                .followUpRequired(true).nextSessionDate(LocalDate.now().plusDays(2))
                .sessionDurationMinutes(45).interventions("Entretien motivationnel")
                .studentProgress("Peu d'amélioration").build());

        // Rendez-vous à venir
        appointmentRepo.save(Appointment.builder().student(st.get(2)).psychologist(psy1)
                .dateTime(at(3, 10, 0)).locationType(LocationType.ONLINE)
                .status(AppointmentStatus.CONFIRMED).type(AppointmentType.FOLLOW_UP)
                .reason("Suivi stress").build());
        Appointment live = appointmentRepo.save(Appointment.builder().student(st.get(0)).psychologist(psy1)
                .dateTime(at(1, 14, 0)).locationType(LocationType.ONLINE)
                .status(AppointmentStatus.CONFIRMED).type(AppointmentType.INITIAL)
                .reason("Gestion du temps").build());
        sessionRepo.save(ConsultationSession.builder().appointment(live).roomId(UUID.randomUUID().toString())
                .status(ConsultationSession.SessionStatus.WAITING).build());
        appointmentRepo.save(Appointment.builder().student(st.get(4)).psychologist(psy2)
                .dateTime(at(2, 9, 30)).locationType(LocationType.PRESENTIAL)
                .status(AppointmentStatus.CONFIRMED).type(AppointmentType.URGENT)
                .reason("Situation urgente").build());
        appointmentRepo.save(Appointment.builder().student(st.get(1)).psychologist(psy2)
                .dateTime(at(5, 15, 0)).locationType(LocationType.PRESENTIAL)
                .status(AppointmentStatus.PENDING).type(AppointmentType.INITIAL)
                .reason("Difficultés d'intégration").build());
        appointmentRepo.save(Appointment.builder().student(st.get(3)).psychologist(psy1)
                .dateTime(at(-3, 9, 0)).locationType(LocationType.PRESENTIAL)
                .status(AppointmentStatus.CANCELLED).type(AppointmentType.INITIAL)
                .reason("Orientation").cancellationReason("Empêchement").build());

        // Documents médicaux
        documentRepo.save(doc(st.get(0), "certificat_medical.pdf", DocStatus.VALIDATED, scolarity, null));
        documentRepo.save(doc(st.get(2), "justificatif_absence.pdf", DocStatus.PENDING, null, null));
        documentRepo.save(doc(st.get(4), "ordonnance.png", DocStatus.REJECTED, scolarity, "Document illisible"));
    }

    private MedicalDocument doc(StudentProfile s, String name, DocStatus status, User agent, String rejection) {
        MedicalDocument d = new MedicalDocument();
        d.setStudent(s);
        d.setUploadedBy(s.getUser());
        d.setFileName(name);
        d.setMimeType(name.endsWith(".png") ? "image/png" : "application/pdf");
        d.setFilePath("demo/" + name); // fichier fictif : le téléchargement n'est pas disponible
        d.setFileSize(120_000L);
        d.setStatus(status);
        if (status == DocStatus.VALIDATED) d.validate(agent);
        if (status == DocStatus.REJECTED) d.reject(agent, rejection);
        return d;
    }

    // --------------------------------------------------- messagerie & notifications

    private void seedMessagingAndNotifications(List<StudentProfile> st, User psy, User scolarity, User admin) {
        User student = st.get(0).getUser();
        Conversation c = conversationRepo.save(Conversation.builder()
                .participantOne(student).participantTwo(psy).build());
        String[][] chat = {
                {"s", "Bonjour, je voudrais confirmer mon rendez-vous de demain."},
                {"p", "Bonjour Ahmed, il est bien confirmé pour 14h en visio."},
                {"s", "Parfait, merci beaucoup !"},
        };
        String last = "";
        for (String[] m : chat) {
            boolean fromStudent = m[0].equals("s");
            messageRepo.save(Message.builder().conversation(c)
                    .sender(fromStudent ? student : psy).receiver(fromStudent ? psy : student)
                    .content(m[1]).read(true).readAt(LocalDateTime.now()).build());
            last = m[1];
        }
        c.updateLastMessage(last);
        conversationRepo.save(c);

        notificationRepo.save(Notification.builder().recipient(student)
                .title("Rendez-vous confirmé").message("Votre rendez-vous de demain à 14h est confirmé.")
                .type(NotificationType.APPOINTMENT).senderName(psy.getFullName()).build());
        notificationRepo.save(Notification.builder().recipient(student)
                .title("Document validé").message("Votre certificat médical a été validé.")
                .type(NotificationType.DOCUMENT).senderName(scolarity.getFullName()).build());
        notificationRepo.save(Notification.builder().recipient(psy)
                .title("Étudiant à risque").message("Mehdi Bouzid présente un niveau de risque critique.")
                .type(NotificationType.ALERT).senderName("Système").build());
        notificationRepo.save(Notification.builder().recipient(admin)
                .title("Bienvenue").message("Les données de démonstration ont été chargées.")
                .type(NotificationType.SYSTEM).senderName("Système").build());
    }
}
