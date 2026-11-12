package tn.enicarthage.speedenicar_projet.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import tn.enicarthage.speedenicar_projet.common.enums.Role;
import tn.enicarthage.speedenicar_projet.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<User> findWithFilters(Role role, Boolean enabled, String search, Pageable pageable) {
        List<Criteria> and = new ArrayList<>();
        and.add(Criteria.where("deleted").is(false));
        if (role != null) and.add(Criteria.where("role").is(role));
        if (enabled != null) and.add(Criteria.where("enabled").is(enabled));
        if (search != null && !search.isBlank()) {
            Pattern p = Pattern.compile(Pattern.quote(search.trim()), Pattern.CASE_INSENSITIVE);
            and.add(new Criteria().orOperator(
                    Criteria.where("firstName").regex(p),
                    Criteria.where("lastName").regex(p),
                    Criteria.where("email").regex(p)));
        }
        Query query = new Query(new Criteria().andOperator(and));
        long total = mongoTemplate.count(query, User.class);
        query.with(pageable);
        if (pageable.getSort().isUnsorted()) {
            query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        return new PageImpl<>(mongoTemplate.find(query, User.class), pageable, total);
    }
}
