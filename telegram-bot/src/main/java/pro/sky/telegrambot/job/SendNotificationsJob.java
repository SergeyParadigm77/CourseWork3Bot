package pro.sky.telegrambot.job;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.TelegramSenderService;
import java.util.concurrent.TimeUnit;

@Component
public class SendNotificationsJob {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(SendNotificationsJob.class);
    private final NotificationTaskRepository notificationTaskRepository;
    private final TelegramSenderService telegramSenderService;

    public SendNotificationsJob(NotificationTaskRepository notificationTaskRepository, TelegramSenderService telegramSenderService) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramSenderService = telegramSenderService;
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 1)
    public void sendNotifications() {
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        logger.info("Job started for dateTime = " + currentDateTime);
        notificationTaskRepository.findAllByDateTime(currentDateTime).forEach(task -> {
            telegramSenderService.send(task.getChatId(), "Напоминание! " + task.getMessage());
            logger.info("Sending notification job finished for dateTime = " + currentDateTime);
        });
    }
}
