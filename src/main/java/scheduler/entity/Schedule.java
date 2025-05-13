package scheduler.entity;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Schedule {
    private int id;
    private String title;
    private String user;
    private String password;
    private LocalDate created;
    private LocalDate updated;
    
    public Schedule(int id, String title, String user, String password) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.password = password;
        this.created = LocalDate.now();
        this.updated = LocalDate.now();
    }
}
