package br.com.fiap.service;

import br.com.fiap.RabbitMQ.TaskCreatedMessageProducer;
import br.com.fiap.TaskCreatedNotification;
import br.com.fiap.feign.UserClient;
import br.com.fiap.util.TaskMapper;
import br.com.fiap.util.TaskSpecification;
import br.com.fiap.exception.TaskNotFoundException;
import br.com.fiap.model.Task;
import br.com.fiap.repository.TaskRepository;
import com.fiap.task_service.tasks.dto.CreateTaskRequestDTO;
import com.fiap.task_service.tasks.dto.PaginatedResultDTO;
import com.fiap.task_service.tasks.dto.TaskDTO;
import com.fiap.task_service.tasks.dto.UpdateTaskRequestDTO;
import com.fiap.user_service.users.dto.UserDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class TaskService {

    @NonNull
    private final TaskRepository taskRepository;

    @NonNull
    private final UserClient userClient;

    @NonNull
    private final TaskCreatedMessageProducer taskCreatedMessageProducer;

    public TaskDTO createTask(final CreateTaskRequestDTO taskDTO) {
        final var user = getUser(taskDTO.getUserId());
        final var toCreate = Task.newTask(
                taskDTO.getTitle(),
                taskDTO.getDescription(),
                user.getId());
        final var created = taskRepository.save(toCreate);

        taskCreatedMessageProducer.sendTaskCreatedMessage(
                new TaskCreatedNotification(user.getEmail(), user.getName(), created.getTitle(), created.getDescription())
        );

        return TaskMapper.toDTO(created);
    }

    public TaskDTO updateTask(final Long taskId, final UpdateTaskRequestDTO taskDTO) {
        final var toUpdate = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        toUpdate.setTitle(taskDTO.getTitle());
        toUpdate.setDescription(taskDTO.getDescription());
        final var updated = taskRepository.save(toUpdate);
        return TaskMapper.toDTO(updated);
    }

    public void cancelTask(final Long taskId) {
        final var toCancel = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        toCancel.cancel();
        taskRepository.save(toCancel);
    }

    @Transactional(readOnly = true)
    public PaginatedResultDTO getAllTasks(final Integer page,
                                          final Integer size,
                                          final String sort,
                                          final String status,
                                          final Long userId) {
        final var sortParams = sort.split(",");
        final var sortField = sortParams[0];
        final var sortDirection = sortParams[1];
        final var pageable = PageRequest.of(page,
                size,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        final var specification = TaskSpecification.create(status, userId);
        final var tasks = taskRepository.findAll(specification, pageable);
        final var totalElements = tasks.getTotalElements();
        final var totalPages = tasks.getTotalPages();
        final var taskDTOs = tasks.getContent().stream()
                .map(TaskMapper::toDTO)
                .toList();
        return new PaginatedResultDTO()
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .content(taskDTOs);
    }

    @Transactional(readOnly = true)
    public TaskDTO getTask(final Long taskId) {
        return taskRepository.findById(taskId)
                .map(TaskMapper::toDTO)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    public void executeTask(final Long taskId) {
        final var toExecute = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        toExecute.execute();
        taskRepository.save(toExecute);
    }

    public void completeTask(final Long taskId) {
        final var toComplete = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        toComplete.complete();
        taskRepository.save(toComplete);
    }



    private UserDTO getUser(final Long userId) {
       try{
          return userClient.getUser(userId);
       }catch (final Exception e){
           log.error(e.getMessage());
           throw new TaskNotFoundException(userId);
       }
    }
}