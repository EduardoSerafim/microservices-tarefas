package br.com.fiap.RabbitMQ;

import br.com.fiap.TaskCreatedNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskCreatedMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.task.exchange.name}")
    private String exchangeName;

    @Value("${broker.queue.task.created.routing-key}")
    private String routingKey;

    public void sendTaskCreatedMessage(final TaskCreatedNotification notification) {
        log.info("Sending task created notification for user: {}, task title: {}", notification.userName(), notification.taskTitle());
        try {
            System.out.println("enviando");
            rabbitTemplate.convertAndSend(exchangeName, routingKey, notification);
            System.out.println("enviado");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to send task created message: {}", e.getMessage());
        }
    }
}