package br.com.fiap;

import br.com.fiap.notificationservice.TaskCreatedNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    @NonNull
    private final EmailService emailService;

    @RabbitListener(queues = "${broker.queue.task.created.name}")
    public void consumeTaskCreatedNotification(@Payload TaskCreatedNotification notification) {
        log.info("Received task created notification for user: {}, task title: {}", notification.userName(), notification.taskTitle());
        try {
            emailService.sendTaskCreatedEmail(notification);
        } catch (final Exception e) {
            log.error("Failed to send task created email: {}", e.getMessage());
        }
    }

}