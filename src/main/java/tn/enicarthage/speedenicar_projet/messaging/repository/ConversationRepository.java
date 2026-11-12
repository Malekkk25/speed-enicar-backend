package tn.enicarthage.speedenicar_projet.messaging.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.messaging.entity.Conversation;

import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, Long> {

    @Query(value = "{ $or: [ { 'participantOne.$id': ?0 }, { 'participantTwo.$id': ?0 } ], 'deleted': false }",
            sort = "{ 'lastMessageAt': -1 }")
    Page<Conversation> findByUserId(Long userId, Pageable pageable);

    @Query("{ $or: [ { 'participantOne.$id': ?0, 'participantTwo.$id': ?1 }, "
            + "{ 'participantOne.$id': ?1, 'participantTwo.$id': ?0 } ], 'deleted': false }")
    Optional<Conversation> findBetweenUsers(Long user1, Long user2);
}
