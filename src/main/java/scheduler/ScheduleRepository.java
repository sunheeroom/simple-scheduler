package scheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import scheduler.entity.Schedule;

public class ScheduleRepository {
    private final Map<Integer, Schedule> store = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);
    
    public List<Schedule> findAll() {
        return new ArrayList<>(store.values());
    }
    
    public Schedule findById(int id) {
        return store.get(id);
    }
    
    public Schedule save(Schedule schedule) {
        LocalDate now = LocalDate.now();
        schedule.setId(idCounter.getAndIncrement());
        schedule.setCreated(now);
        schedule.setUpdated(now);
        store.put(schedule.getId(), schedule);
        return schedule;
    }
    
    public Schedule update(int id, Schedule schedule) {
        store.put(id, schedule);
        return schedule;
    }
    
    public Schedule deleteById(int id) {
        return store.remove(id);
    }
    
    public boolean exists(int id) {
        return store.containsKey(id);
    }
}