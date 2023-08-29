package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.TelegramSenderService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramSenderService telegramSenderService;
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final static Pattern MESSAGE_PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private final static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final TelegramBot telegramBot;
    private final NotificationTaskRepository notificationTaskRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, TelegramSenderService telegramSenderService, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = telegramBot;
        this.telegramSenderService = telegramSenderService;
        this.notificationTaskRepository = notificationTaskRepository;
    }


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
           Long chatId = update.message().chat().id();
           String message = update.message().text();

           if ("/start".equals(message)) {
                telegramSenderService.send(chatId, "Привет, друг!");
           } else {
               Matcher matcher = MESSAGE_PATTERN.matcher(message);
               if (matcher.matches()) {
                   String task = matcher.group(3);
                   LocalDateTime localDateTime = LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMAT);

                   notificationTaskRepository.save(new NotificationTask(task, chatId, localDateTime));
                   telegramSenderService.send(chatId, "Напоминание сохранено!");

               } else {
                   telegramSenderService.send(chatId, "Неверный формат сообщения, попробуй еще раз!");
               }
           }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
