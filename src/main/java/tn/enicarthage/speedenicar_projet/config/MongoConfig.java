package tn.enicarthage.speedenicar_projet.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.scheduling.annotation.EnableAsync;
import tn.enicarthage.speedenicar_projet.common.BaseEntity;
import tn.enicarthage.speedenicar_projet.common.SequenceGeneratorService;

@Configuration
@EnableMongoAuditing
@EnableAsync
public class MongoConfig {

    /** Assigne un id Long auto-incrémenté à chaque nouveau document. */
    @Bean
    public BeforeConvertCallback<BaseEntity> idGenerationCallback(ObjectProvider<SequenceGeneratorService> sequences) {
        return (entity, collection) -> {
            if (entity.getId() == null) {
                entity.setId(sequences.getObject().nextId(collection));
            }
            return entity;
        };
    }
}
