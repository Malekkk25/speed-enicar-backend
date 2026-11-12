package tn.enicarthage.speedenicar_projet.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.notification.entity.Notification;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, Long> {

    @Query(value = "{ 'recipient.$id': ?0, 'deleted': false }", sort = "{ 'createdAt': -1 }")
    Page<Notification> findByRecipientIdAndDeletedFalseOrderByCreatedAtDesc(Long recipientId, Pageable pageable);

    @Query(value = "{ 'recipient.$id': ?0, 'read': false, 'deleted': false }", sort = "{ 'createdAt': -1 }")
    Page<Notification> findByRecipientIdAndReadFalseAndDeletedFalseOrderByCreatedAtDesc(Long recipientId, Pageable pageable);

    @Query(value = "{ 'recipient.$id': ?0, 'read': false, 'deleted': false }", count = true)
    long countByRecipientIdAndReadFalseAndDeletedFalse(Long recipientId);

    @Query("{ 'recipient.$id': ?0, 'read': false }")
    List<Notification> findUnreadByRecipientId(Long recipientId);

    /** Marque toutes les notifications non lues comme lues, retourne le nombre modifie. */
    default int markAllAsRead(Long userId) {
        List<Notification> unread = findUnreadByRecipientId(userId);
        LocalDateTime now = LocalDateTime.now();
        unread.forEach(n -> {
            n.setRead(true);
            n.setReadAt(now);
        });
        saveAll(unread);
        return unread.size();
    }
}
