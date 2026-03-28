package br.com.fiap.exception;

public class TaskAlreadyCompletedException extends RuntimeException {
    public TaskAlreadyCompletedException(Long taskId) {
        super("Task with ID " + taskId + " is already completed.");
    }
}
