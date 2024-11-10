package com.task.management.config;
import com.task.management.entity.Task;
import com.task.management.model.TaskResponse;
import com.task.management.service.EmailService;
import com.task.management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PedingTasksNotification {

    @Autowired
    TaskService taskService;

    @Autowired
    EmailService emailService;
    @Scheduled(cron = "0 0/5 * * * ?") // Every 5 minutes
    public void scheduleTaskWithCronExpression() {
        LocalDateTime today = LocalDateTime.now().with(LocalTime.MAX);
        LocalDateTime twoDaysAfter = today.plusDays(2);
        System.out.println("Date is " + twoDaysAfter);
        List<Task> list = taskService.getTasksNotficationsTask(twoDaysAfter);
        list.forEach(task -> {
            String subject = "Friendly Reminder: Your Task is Due Soon!";
            String message = "<p>Hi " + task.getUser().getUsername() + ",</p>"
                    + "<p>This is a friendly reminder that the due date for <strong>" + task.getTitle() + "</strong> is approaching. "
                    + "Please ensure that all necessary actions are completed by the deadline.</p>"
                    + "<p><strong>Due Date:</strong> " + task.getDueDate() +"</p>"
                    + "<p>If you have any questions or need assistance, feel free to reach out.</p>"
                    + "<p>Best regards</p>";
            try {
                emailService.sendEmail(new String[]{task.getUser().getEmail()},subject,message);
            } catch (MessagingException e) {
                System.err.println(e.toString());
            }
        });

    }
}
