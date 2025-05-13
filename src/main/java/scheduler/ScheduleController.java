package scheduler;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scheduler.dto.ScheduleDTO;
import scheduler.entity.Schedule;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService service;
    
    public ScheduleController(ScheduleService service) {
        this.service = service;
    }
    
    @GetMapping
    public List<ScheduleDTO> getAll() {
        return service.getAll().stream().map(ScheduleDTO::new).toList();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getById(@PathVariable int id) {
        Schedule s = service.getById(id);
        return s == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(new ScheduleDTO(s));
    }
    
    @PostMapping
    public ResponseEntity<ScheduleDTO> create(@RequestBody Schedule schedule) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ScheduleDTO(service.create(schedule)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> update(@PathVariable int id, @RequestBody Schedule schedule) {
        Schedule updated = service.update(id, schedule);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(new ScheduleDTO(updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id, @RequestBody Map<String, String> body) {
        String password = body.get("password");
        boolean result = service.delete(id, password);
        if (!result) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(Map.of("message", "Schedule deleted"));
    }
}
