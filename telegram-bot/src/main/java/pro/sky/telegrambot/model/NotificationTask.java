package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private long chatId;
    private LocalDateTime dateTime;

    public NotificationTask(String message, long chatId, LocalDateTime dateTime) {
        this.message = message;
        this.chatId = chatId;
        this.dateTime = dateTime;
    }
    public NotificationTask() {
    }
    public void setId(Long id) {this.id = id;}
    public String getMessage() {return message;}
    public Long getChatId() {return chatId;}
    public void setChatId(Long chatId) {this.chatId = chatId;}
    public LocalDateTime getDateTime() {return dateTime;}

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", chatId=" + chatId +
                ", dateTime=" + dateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return chatId == that.chatId && Objects.equals(id, that.id) && Objects.equals(message, that.message) && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, chatId, dateTime);
    }

    public void setDateTime(LocalDateTime dateTime) {this.dateTime = dateTime;}
}
