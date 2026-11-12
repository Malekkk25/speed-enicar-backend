package tn.enicarthage.speedenicar_projet.messaging.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.messaging.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, Long> {

    @Query(value = "{ 'conversation.$id': ?0, 'deleted': false }", sort = "{ 'createdAt': 1 }")
    Page<Message> findByConversationIdAndDeletedFalseOrderByCreatedAtAsc(Long conversationId, Pageable pageable);

    @Query(value = "{ 'conversation.$id': ?0, 'deleted': false }", sort = "{ 'createdAt': -1 }")
    Page<Message> findByConversationIdAndDeletedFalseOrderByCreatedAtDesc(Long conversationId, Pageable pageable);

    @Query(value = "{ 'receiver.$id': ?0, 'read': false, 'deleted': false }", count = true)
    long countByReceiverIdAndReadFalseAndDeletedFalse(Long receiverId);

    @Query(value = "{ 'conversation.$id': ?0, 'receiver.$id': ?1, 'read': false, 'deleted': false }", count = true)
    long countByConversationIdAndReceiverIdAndReadFalseAndDeletedFalse(Long conversationId, Long receiverId);

    @Query("{ 'conversation.$id': ?0, 'receiver.$id': ?1, 'read': false }")
    List<Message> findUnreadInConversation(Long conversationId, Long receiverId);

    /** Marque les messages non lus de la conversation comme lus, retourne le nombre modifie. */
    default int markConversationAsRead(Long conversationId, Long userId) {
        List<Message> unread = findUnreadInConversation(conversationId, userId);
        LocalDateTime now = LocalDateTime.now();
        unread.forEach(m -> {
            m.setRead(true);
            m.setReadAt(now);
        });
        saveAll(unread);
        return unread.size();
    }
}
