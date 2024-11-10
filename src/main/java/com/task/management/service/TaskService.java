package com.task.management.service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.task.management.auth.CustomUserDetails;
import com.task.management.entity.History;
import com.task.management.entity.Task;
import com.task.management.entity.User;
import com.task.management.exception.TaskManagementEnum;
import com.task.management.exception.TaskManagementException;
import com.task.management.mapper.HistoryMapper;
import com.task.management.mapper.TaskMapper;
import com.task.management.model.HistoryResponse;
import com.task.management.model.TaskRequest;
import com.task.management.model.TaskResponse;
import com.task.management.repository.HistoryRepository;
import com.task.management.repository.TaskRepository;
import com.task.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    EmailService emailService;

    private final TaskMapper mapper = TaskMapper.INSTANCE;
    private final HistoryMapper mapper2 = HistoryMapper.INSTANCE;

    public TaskResponse createTask(TaskRequest  taskRequest) throws TaskManagementException {
        User createdUser = userRepository.getReferenceById(Long.parseLong(getUserId()));
        Task task = taskRepository.save( Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .user(userRepository.getReferenceById(Long.parseLong(taskRequest.getUser_id())))
                .createdUser(createdUser)
                .status(getStatus(taskRequest))
                .priority(getPriority(taskRequest))
                .dueDate(taskRequest.getDueDate())
                .createdDate(LocalDateTime.now())
                .build());
        historyRepository.save(History.builder().taskId(task.getId()).user(createdUser).description(mapper.taskToTaskResponse(task).toString()).action("Created").date(LocalDateTime.now()).build());
        try {
            emailService.sendEmail(new String[]{task.getUser().getEmail()} ,"New Task ( ".concat(task.getTitle()).concat(" ) Assign  for you "),"Task ID : ".concat(task.getId().toString()).concat(" Assign  for you"));
        } catch (MessagingException e) {
            System.err.println(e);
        }
        return mapper.taskToTaskResponse(task);
    }
    public TaskResponse updateTask(TaskRequest  taskRequest) throws TaskManagementException {
        User updatedUser = userRepository.getReferenceById(Long.parseLong(getUserId()));
        Optional.ofNullable(taskRequest.getId()).orElseThrow(TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_08::getValue);
        Task task = taskRepository.findById(taskRequest.getId()).orElseThrow(TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_07::getValue);
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUser(userRepository.getReferenceById(Long.parseLong(taskRequest.getUser_id())));
        task.setStatus(getStatus(taskRequest));
        task.setPriority(getPriority(taskRequest));
        task.setDueDate(taskRequest.getDueDate());
        taskRepository.save(task);
        historyRepository.save(History.builder().taskId(task.getId()).user(updatedUser).description(mapper.taskToTaskResponse(task).toString()).action("Updated").date(LocalDateTime.now()).build());
        return mapper.taskToTaskResponse(task);
    }
    public TaskResponse deleteTask(Long taskId) throws TaskManagementException {
        User updatedUser = userRepository.getReferenceById(Long.parseLong(getUserId()));
        Optional.ofNullable(taskId).orElseThrow(TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_08::getValue);
        Task task = taskRepository.findById(taskId).orElseThrow(TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_07::getValue);
        taskRepository.delete(task);
        historyRepository.save(History.builder().taskId(task.getId()).user(updatedUser).description(mapper.taskToTaskResponse(task).toString()).action("Deleted").date(LocalDateTime.now()).build());
        return mapper.taskToTaskResponse(task);
    }
    public List<TaskResponse> getTasks(int size , int page, String title, Task.Status status,Task.Priority priority,String description,LocalDateTime dueDateFrom,LocalDateTime dueDateTo,LocalDateTime createDateFrom,LocalDateTime createDateTo,Long userId) throws TaskManagementException {
        if((!ObjectUtils.isEmpty(dueDateTo)&&ObjectUtils.isEmpty(dueDateFrom)) || (!ObjectUtils.isEmpty(dueDateFrom)&& !ObjectUtils.isEmpty(dueDateTo))&&dueDateFrom.isAfter(dueDateTo)){
            throw TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_09.getValue();
        }
        if((!ObjectUtils.isEmpty(createDateTo)&&ObjectUtils.isEmpty(createDateTo)) || (!ObjectUtils.isEmpty(createDateFrom)&& !ObjectUtils.isEmpty(createDateTo))&&createDateFrom.isAfter(createDateTo)){
            throw TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_10.getValue();
        }
        Pageable pageable = PageRequest.of(page, size);
        Specification<Task> spec = Specification.where(titleIsLike(title).and(statusIsEqual(status))).and(priorityIsEqual(priority))
                .and(descriptionIsLike(description)).and(dueDate(dueDateFrom,dueDateTo)).and(createdDate(createDateFrom,createDateTo)).and(myTasks(userId));
        Page<Task>taskpage = taskRepository.findAll(spec,pageable);
        return mapper.taskListToTaskResponseList(taskpage.toList());
    }
    public List<Task> getTasksNotficationsTask(LocalDateTime date){
        Specification<Task> spec = Specification.where(statusIsEqual(Task.Status.TODO).or(statusIsEqual(Task.Status.IN_PROGRESS))).and(dateLessThan(date));
        return taskRepository.findAll(spec);
    }
    private Specification<Task> dateLessThan(LocalDateTime date) {
            return  (root, query, criteriaBuilder) ->criteriaBuilder.lessThan(root.get("dueDate"), date);
    }

    private Task.Priority getPriority(TaskRequest taskRequest) throws TaskManagementException {
        try {
                return Optional.ofNullable(taskRequest.getPriority())
                    .map(Task.Priority::valueOf)
                    .orElseThrow(TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_01::getValue);
        }
        catch (IllegalArgumentException e){
            throw TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_02.getValue();
        }
    }
    private Task.Status getStatus(TaskRequest taskRequest) throws TaskManagementException {
        try {
                return Optional.ofNullable(taskRequest.getStatus())
                    .map(Task.Status::valueOf)
                    .orElseThrow(TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_03::getValue);
        }
        catch (IllegalArgumentException e){
            throw  TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_04.getValue();
        }
    }
    private Specification<Task> titleIsLike(String title) {
        return (root, query, criteriaBuilder) ->
                title == null ? null : criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }
    private Specification<Task> statusIsEqual(Task.Status status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"),  status );
    }
    private Specification<Task> myTasks(Long userId) {
        User user =Optional.ofNullable(userId).flatMap(userRepository::findById).orElse(null);
        return (root, query, criteriaBuilder) ->
                user == null ? null : criteriaBuilder.equal(root.get("user"),  user );
    }
    private Specification<Task> priorityIsEqual(Task.Priority status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("priority"),  status );
    }
    private Specification<Task> descriptionIsLike(String description) {
        return (root, query, criteriaBuilder) ->
                description == null ? null : criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }
    private Specification<Task> dueDate(LocalDateTime dateFrom,LocalDateTime dateTo) {
        if(dateFrom != null && dateTo==null)
            return (root, query, criteriaBuilder) ->criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), dateFrom);
        else if (dateTo != null && dateFrom != null)
           return  (root, query, criteriaBuilder) ->criteriaBuilder.between(root.get("dueDate"), dateFrom,dateTo);
        else return (root, query, criteriaBuilder) ->null;

    }
    private Specification<Task> createdDate(LocalDateTime createdDateFrom,LocalDateTime createdDateTo) {
        if(createdDateFrom != null && createdDateTo==null)
            return (root, query, criteriaBuilder) ->criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), createdDateFrom);
        else if (createdDateTo != null && createdDateFrom != null)
            return  (root, query, criteriaBuilder) ->criteriaBuilder.between(root.get("createdDate"), createdDateFrom,createdDateTo);
        else return (root, query, criteriaBuilder) ->null;

    }
    public void clearData(){
        taskRepository.deleteAll();
        userRepository.deleteAll();

    }
    private String getUserId() {
        CustomUserDetails c = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return c.getUserId();
    }
    public List<HistoryResponse> geth(){
        return mapper2.historyListToHistoryResponseList(historyRepository.findAll());
    }


}
