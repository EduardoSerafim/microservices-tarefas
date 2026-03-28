package br.com.fiap.exception;

public class TaskAlreadyCancelledException extends RuntimeException {
    public TaskAlreadyCancelledException(Long taskId) {
        super("Task with ID " + taskId + " is already cancelled.");
    }
}
