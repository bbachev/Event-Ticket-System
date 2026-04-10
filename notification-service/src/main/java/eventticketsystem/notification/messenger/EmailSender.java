package eventticketsystem.notification.messenger;

import eventticketsystem.notification.dto.MessageTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailSender {

    private final JavaMailSender javaMailSender;

    @Getter
    @Value("${spring.mail.default-sender}")
    private String sender;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void send(MessageTemplate template, String receiver) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(sender);
        helper.setTo(receiver);
        helper.setSubject(template.subject());

        String body = template.bodyHtml();
        for (Map.Entry<String, String> entry : template.placeholders().entrySet()) {
            body = body.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        helper.setText(body, true);

        javaMailSender.send(message);
    }

}
