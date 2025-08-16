package net.engineeringdigest.journalApp.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void sendMail(){
        emailService.sendEmail("familylink204@gmail.com","kenilbhandari9@gmail.com", "Hello Cooked", "Literally Cooked big bigger biggest");
    }

}
