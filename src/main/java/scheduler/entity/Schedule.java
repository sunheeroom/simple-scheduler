package scheduler.entity;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Schedule {
    public int id;
    public String title;
    public String user;
    public String password;
    public LocalDate created;
    public LocalDate updated;
    
    public Schedule(int id, String title, String user, String password) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.password = password;
        this.created = LocalDate.now();
        this.updated = LocalDate.now();
    }
}
