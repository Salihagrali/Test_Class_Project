
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;


import static org.junit.jupiter.api.Assertions.*;

public class LoginBotTest {

    LoginBot loginBot;

    @BeforeEach
    public void setUp() {
        loginBot = new LoginBot("https://www.letgo.com/");
        loginBot.connect();
        loginBot.removeCookieOption();
    }

    @Test
    public void testValidEmail() {
        loginBot.login("user@gmail.com");
        boolean isEnabled = loginBot.isContinueButtonEnabled();
        assertTrue(isEnabled);
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "@gmail.com",     // missing username
            "usergmail.com",  // missing @
            "user@",            // missing domain
            "user@.com",        // domain name missing
            "user@xcom",        // no dot in domain
            "x@x.c",            // too short TLD
            ""                 // empty string
    })
    public void testInvalidEmail(String email) {
        loginBot.login(email);
        boolean isEnabled = loginBot.isContinueButtonEnabled();
        assertFalse(isEnabled);
    }



    @AfterAll
    public static void tearDown() {
        DriverFactory.quitDriver();
    }

}
