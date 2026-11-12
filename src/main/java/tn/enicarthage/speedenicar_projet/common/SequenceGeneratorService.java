package tn.enicarthage.speedenicar_projet.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/** Génère des ids numériques auto-incrémentés (un compteur par collection). */
@Service
public class SequenceGeneratorService {

    @Getter
    @Setter
    @Document(collection = "database_sequences")
    public static class DatabaseSequence {
        @Id
        private String id;
        private long seq;
    }

    private final MongoOperations mongoOperations;

    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public long nextId(String sequenceName) {
        DatabaseSequence counter = mongoOperations.findAndModify(
                query(where("_id").is(sequenceName)),
                new org.springframework.data.mongodb.core.query.Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return counter != null ? counter.getSeq() : 1;
    }
}
