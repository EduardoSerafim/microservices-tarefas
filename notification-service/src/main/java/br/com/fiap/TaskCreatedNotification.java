package br.com.fiap.notificationservice;

public record TaskCreatedNotification(
        String to,
        String userName,
        String taskTitle,
        String taskDescription
) {
}
