package com.example.taskservice.notify;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * High-level notifier used by your domain/service layer.
 * Marked @Async so the request returns fast and email sends in background.
 */
@Service
@RequiredArgsConstructor
public class TaskEmailNotifier {

    private final EmailPort emailPort;

    @Async
    public void notifyTaskCreated(String recipientEmail, String taskId, String taskTitle) {
        String subject = "Your task was created";
        String html = """
                <h3>Task created</h3>
                <p><b>ID:</b> %s</p>
                <p><b>Title:</b> %s</p>
                <p>You can now track this task in the portal.</p>
                """.formatted(taskId, taskTitle);
        emailPort.send(recipientEmail, subject, html);
    }

}
