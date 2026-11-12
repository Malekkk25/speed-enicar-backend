package tn.enicarthage.speedenicar_projet.student.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.student.entity.MoodEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Repository
public interface MoodEntryRepository extends MongoRepository<MoodEntry, Long> {

    @Query(value = "{ 'student.$id': ?0, 'deleted': false }", sort = "{ 'date': -1 }")
    Page<MoodEntry> findByStudentIdAndDeletedFalseOrderByDateDesc(Long studentId, Pageable pageable);

    @Query("{ 'student.$id': ?0, 'date': ?1 }")
    Optional<MoodEntry> findByStudentIdAndDate(Long studentId, LocalDate date);

    @Query(value = "{ 'student.$id': ?0, 'date': ?1, 'deleted': false }", exists = true)
    boolean existsByStudentIdAndDateAndDeletedFalse(Long studentId, LocalDate date);

    @Query(value = "{ 'student.$id': ?0, 'date': { $gte: ?1, $lte: ?2 }, 'deleted': false }",
            sort = "{ 'date': 1 }")
    List<MoodEntry> findByStudentIdAndDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);

    default Double getAverageMood(Long studentId, LocalDate startDate, LocalDate endDate) {
        return findByStudentIdAndDateBetween(studentId, startDate, endDate).stream()
                .mapToInt(MoodEntry::getMoodLevel)
                .average()
                .stream().boxed().findFirst().orElse(null);
    }

    default Long countNegativeMoods(Long studentId, LocalDate startDate, LocalDate endDate) {
        return findByStudentIdAndDateBetween(studentId, startDate, endDate).stream()
                .filter(m -> m.getMoodLevel() <= 2)
                .count();
    }

    /** Liste de paires {moodLevel (Integer), nombre (Long)} triee par moodLevel. */
    default List<Object[]> getMoodDistribution(Long studentId, LocalDate startDate, LocalDate endDate) {
        Map<Integer, Long> counts = new TreeMap<>();
        findByStudentIdAndDateBetween(studentId, startDate, endDate)
                .forEach(m -> counts.merge(m.getMoodLevel(), 1L, Long::sum));
        return counts.entrySet().stream()
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .toList();
    }
}
