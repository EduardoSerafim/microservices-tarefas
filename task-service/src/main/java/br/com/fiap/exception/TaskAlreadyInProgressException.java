package br.com.fiap.exception;

public class TaskAlreadyInProgressException extends RuntimeException {
    public TaskAlreadyInProgressException(Long taskId) {
        super("Task with ID " + taskId + " is already in progress.");
    }
}
