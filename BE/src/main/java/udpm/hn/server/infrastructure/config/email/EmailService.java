package udpm.hn.server.infrastructure.config.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // Process the email template
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);
            
            // Set email properties
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            // Send the email
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    // Example of a specific email sending method
    @Async
    public void sendVerificationEmail(String to, String username, String verificationUrl) {
        Map<String, Object> variables = Map.of(
            "username", username,
            "verificationUrl", verificationUrl
        );
        sendEmail(to, "Verify your email address", "email/verification", variables);
    }

    @Async
    public void sendPasswordResetEmail(String to, String username, String resetUrl) {
        Map<String, Object> variables = Map.of(
            "username", username,
            "resetUrl", resetUrl
        );
        sendEmail(to, "Password Reset Request", "email/password-reset", variables);
    }
}
