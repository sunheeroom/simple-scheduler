package scheduler.dto;

import java.time.LocalDate;
import lombok.Getter;
import scheduler.entity.Schedule;

@Getter
public class ScheduleDTO {
    private final int id;
    private final String title;
    private final String user;
    private final LocalDate created;
    private final LocalDate updated;
    
    public ScheduleDTO(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.user = schedule.getUser();
        this.created = schedule.getCreated();
        this.updated = schedule.getUpdated();
    }
}