package tn.enicarthage.speedenicar_projet.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.enicarthage.speedenicar_projet.common.enums.Role;
import tn.enicarthage.speedenicar_projet.user.entity.User;

public interface UserRepositoryCustom {
    Page<User> findWithFilters(Role role, Boolean enabled, String search, Pageable pageable);
}
