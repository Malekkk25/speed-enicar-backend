package tn.enicarthage.speedenicar_projet.auth;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import tn.enicarthage.speedenicar_projet.auth.dto.RegisterRequest;
import tn.enicarthage.speedenicar_projet.common.enums.Role;
import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private RegisterRequest req(String email, String pwd) {
        return RegisterRequest.builder().email(email).password(pwd)
                .firstName("Test").lastName("User").role(Role.STUDENT).studentId("E1").build();
    }

    @ParameterizedTest(name = "{0} / {1} -> valide = {2}")
    @CsvSource({
            "new@enicar.tn, Test@1234, true",
            "new@enicar.tn, Tes@123,   false",
            "new@enicar.tn, test1234,  false",
            "newenicar.tn,  Test@1234, false"
    })
    void validation(String email, String pwd, boolean valide) {
        int violations = validator.validate(req(email, pwd)).size();
        if (valide) assertEquals(0, violations);
        else assertTrue(violations >= 1, "Une violation était attendue");
    }

    @Test
    void champsVidesRefuses() {
        assertFalse(validator.validate(req("", "")).isEmpty());
    }
}