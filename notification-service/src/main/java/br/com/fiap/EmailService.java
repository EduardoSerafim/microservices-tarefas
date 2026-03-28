package br.com.fiap;

import br.com.fiap.notificationservice.TaskCreatedNotification;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${notification.email.sender}")
    private String from;

    public void sendTaskCreatedEmail(final TaskCreatedNotification notification) {
        try {
            final var variables = new HashMap<String, Object>();
            variables.put("userName", notification.userName());
            variables.put("taskTitle", notification.taskTitle());
            variables.put("taskDescription", notification.taskDescription());
            variables.put("subject", EmailTemplates.TASK_CREATED.getSubject());

            final var context = new Context();
            context.setVariables(variables);

            final var templateName = EmailTemplates.TASK_CREATED.getTemplateName();
            final var htmlContent = templateEngine.process(templateName, context);
            final var message = mailSender.createMimeMessage();

            final var mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(EmailTemplates.TASK_CREATED.getSubject());
            mimeMessageHelper.setText(htmlContent, true);
            mimeMessageHelper.setTo(notification.to());

            mailSender.send(message);
            log.info("Email sent to {} with subject {}", notification.to(), mimeMessageHelper.getMimeMessage().getSubject());
        } catch (final Exception e) {
            log.warn("Failed to send email to {}: {}", notification.to(), e.getMessage());
        }
    }
}



