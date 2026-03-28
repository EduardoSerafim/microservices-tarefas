package br.com.fiap;

public record TaskCreatedNotification (
        String to,
        String userName,
        String taskTitle,
        String taskDescription
){
}
