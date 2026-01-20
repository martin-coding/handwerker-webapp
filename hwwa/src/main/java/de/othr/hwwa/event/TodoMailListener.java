package de.othr.hwwa.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import de.othr.hwwa.service.EmailServiceI;

@Component
public class TodoMailListener {

    private final EmailServiceI emailService;

    public TodoMailListener(EmailServiceI emailService) {
        this.emailService = emailService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAllTodosDone(AllTodosDoneEvent event) {
        emailService.sendTodoNotification(
            event.recipientEmails(),
            event.taskTitle()
        );
    }
}

