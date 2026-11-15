package tn.enicarthage.speedenicar_projet.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Seeder désactivé : aucune donnée n'est insérée au démarrage.
 * Désactivable avec {@code app.seed.enabled=false}.
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("DataSeeder : aucune insertion de données de démonstration");
    }
}
