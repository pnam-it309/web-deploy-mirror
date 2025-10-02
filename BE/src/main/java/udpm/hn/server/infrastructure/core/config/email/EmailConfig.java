package udpm.hn.server.infrastructure.core.config.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    @Profile("!prod") // Only create this bean when not in production
    public JavaMailSender javaMailSender() {
        // Create a mock JavaMailSender that doesn't actually send emails
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Configure it to do nothing
        mailSender.setHost("localhost");
        mailSender.setPort(25);
        mailSender.setUsername("noreply@example.com");
        mailSender.setPassword("");
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.debug", "true");
        
        return mailSender;
    }
}
