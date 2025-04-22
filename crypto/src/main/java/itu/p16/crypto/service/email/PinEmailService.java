package itu.p16.crypto.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class PinEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public PinEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private String loadTemplate(String templateName) throws IOException {
        File templateFile = ResourceUtils.getFile("classpath:templates/email/" + templateName);
        return new String(Files.readAllBytes(Paths.get(templateFile.getPath())));
    }

    private void sendPinEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(senderEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); 

        mailSender.send(message);
    }

    private void sendPinEmailWithVariables(String to, String subject, Map<String, String> variables, String templateName) throws IOException, MessagingException {
        String templateContent = loadTemplate(templateName);

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            templateContent = templateContent.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        sendPinEmail(to, subject, templateContent);
    }

    public void sendTransactionDepotEmail(String to, String montantDepot, String devise, String codePin, String expiration) throws IOException, MessagingException {
        Map<String, String> variables = Map.of(
                "username", to,
                "montant", montantDepot,
                "devise", devise,
                "codepin", codePin,
                "expiration", expiration
        );
    
        sendPinEmailWithVariables(to, "Confirmation de votre dépôt", variables, "depot.html");
    }
    
    
    public void sendTransactionRetraitEmail(String to, String montantRetrait, String devise, String codePin, String expiration) throws IOException, MessagingException {
        Map<String, String> variables = Map.of(
                "username", to,
                "montant", montantRetrait,
                "devise", devise,
                "codepin", codePin,
                "expiration", expiration
        );
    
        sendPinEmailWithVariables(to, "Confirmation de votre retrait", variables, "retrait.html");
    }
    
}
