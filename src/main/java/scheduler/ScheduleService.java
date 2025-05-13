package scheduler;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import scheduler.entity.Schedule;

@Service
public class ScheduleService {
    private final ScheduleRepository repository = new ScheduleRepository();
    
    public List<Schedule> getAll() {
        return repository.findAll();
    }
    
    public Schedule getById(int id) {
        return repository.findById(id);
    }
    
    public Schedule create(Schedule schedule) {
        return repository.save(schedule);
    }
    
    public Schedule update(int id, Schedule reqSchedule) {
        Schedule existing = repository.findById(id);
        if (existing == null || !existing.getPassword().equals(reqSchedule.getPassword())) {
            return null;
        }
        existing.setTitle(reqSchedule.getTitle());
        existing.setUser(reqSchedule.getUser());
        existing.setUpdated(LocalDate.now());
        return repository.update(id, existing);
    }
    
    public boolean delete(int id, String password) {
        Schedule s = repository.findById(id);
        if (s == null || !s.getPassword().equals(password)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
