package tn.enicarthage.speedenicar_projet.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwt;

    @BeforeEach
    void setUp() {
        jwt = new JwtUtils();
        String secret = Base64.getEncoder()
                .encodeToString("cle-de-test-speedenicar-0123456789-abcdefgh".getBytes());
        ReflectionTestUtils.setField(jwt, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwt, "jwtExpirationMs", 900_000L);
        ReflectionTestUtils.setField(jwt, "refreshExpirationMs", 604_800_000L);
    }

    @Test
    void tokenContientEmailEtRole() {
        String t = jwt.generateAccessToken(1L, "student@enicar.tn", "STUDENT");
        assertEquals("student@enicar.tn", jwt.extractEmail(t));
        assertEquals("STUDENT", jwt.extractRole(t));
        assertTrue(jwt.validateToken(t));
    }

    @Test
    void tokenModifieRejete() {
        String t = jwt.generateAccessToken(1L, "student@enicar.tn", "STUDENT");
        String modifie = t.substring(0, t.length() - 2) + (t.endsWith("AA") ? "BB" : "AA");
        assertFalse(jwt.validateToken(modifie));
    }

    @Test
    void tokenExpireRejete() {
        ReflectionTestUtils.setField(jwt, "jwtExpirationMs", -1_000L);
        String t = jwt.generateAccessToken(1L, "student@enicar.tn", "STUDENT");
        assertFalse(jwt.validateToken(t));
    }
}