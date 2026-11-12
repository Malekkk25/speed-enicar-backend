package tn.enicarthage.speedenicar_projet.module_psychologue.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.user.entity.User;

@Repository
public interface PsychologistProfileRepository extends MongoRepository<User, Long> {
}
