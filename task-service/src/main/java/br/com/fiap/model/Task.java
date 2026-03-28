package br.com.fiap.model;

import br.com.fiap.exception.TaskAlreadyCancelledException;
import br.com.fiap.exception.TaskAlreadyCompletedException;
import br.com.fiap.exception.TaskAlreadyInProgressException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Table(name = "tasks")
public class Task {

    enum Status {
        OPEN,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "user_id", nullable = false, updatable = true)
    private Long userId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = true)
    private Instant updatedAt;

    @Column(name = "deleted_at", nullable = true, updatable = true)
    private Instant deletedAt;

    private Task(final String title, final String description, final Status status, final Long userId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.userId = userId;
    }

    public static Task newTask(final String title, final String description, final Long userId) {
        return new Task(title, description, Status.OPEN, userId);
    }

    public void cancel(){
        if(this.getStatus().equals(Status.CANCELLED)){
            throw new TaskAlreadyCancelledException(this.getId());
        }

        if(this.getStatus().equals(Status.COMPLETED)){
            throw new TaskAlreadyCompletedException(this.getId());

        }

        this.setDeletedAt(Instant.now());
        this.setStatus(Status.CANCELLED);
    }

    public void execute(){
        if(!this.getStatus().equals(Status.OPEN)){
            throw new TaskAlreadyInProgressException(this.getId());
        }

        this.setStatus(Status.IN_PROGRESS);
    }

    public void complete(){
        if(!this.getStatus().equals(Status.IN_PROGRESS)){
            throw new TaskAlreadyCompletedException(this.getId());
        }

        this.setStatus(Status.COMPLETED);
    }

    public void update(final String title, final String description){
        this.setTitle(title);
        this.setDescription(description);
    }
}
