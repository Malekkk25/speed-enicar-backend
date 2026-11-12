package tn.enicarthage.speedenicar_projet.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.speedenicar_projet.common.enums.Role;
import tn.enicarthage.speedenicar_projet.user.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    Page<User> findByRoleAndDeletedFalseOrderByLastNameAsc(Role role, Pageable pageable);

    long countByRoleAndDeletedFalse(Role role);

    long countByEnabledTrueAndDeletedFalse();

    long countByDeletedFalse();
}
