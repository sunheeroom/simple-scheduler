package scheduler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import scheduler.entity.Schedule;

@WebServlet("/schedule")
public class ScheduleServlet extends HttpServlet {
    private static Map<Integer, Schedule> scheduleMap = new HashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                return LocalDate.parse(json.getAsString());
            }
        })
        .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
            public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.toString());  // "2025-05-13"
            }
        })
        .create();
    
    // 일정 조회
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json");
        
        if (path == null || path.equals("/")) {
            // TODO 전체 조회
            resp.getWriter().write(gson.toJson(scheduleMap.values()));
        } else {
            try {
                // TODO path 뒤의 ID로 단일 일정 조회
                int id = Integer.parseInt(path.substring(1));
                Schedule schedule = scheduleMap.get(id);
                if (schedule == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\": =\"Schedule Not Found\"}");
                } else {
                    resp.getWriter().write(gson.toJson(schedule));
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Invalid ID format\"}");
            }
        }
    }
    
    // 일정 생성
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Schedule schedule = parseRequest(req);
        LocalDate now = LocalDate.now();
        
        schedule.setId(idCounter.getAndIncrement());
        schedule.setCreated(now);
        schedule.setUpdated(now);
        scheduleMap.put(schedule.getId(), schedule);
        
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(gson.toJson(schedule));
    }
    
    private static Schedule parseRequest(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        return gson.fromJson(reader, Schedule.class);
    }
    
    // 일정 수정
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().substring(1);
        int id = Integer.parseInt(path);
        Schedule reqSchedule = parseRequest(req);
        Schedule schedule = scheduleMap.get(id);
        
        if (schedule == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Schedule Not Found\"}");
            return;
        }
        
        if (!reqSchedule.getPassword().equals(schedule.getPassword())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"Invalid Password\"}");
            return;
        }
        
        schedule.setTitle(reqSchedule.getTitle());
        schedule.setUser(reqSchedule.getUser());
        schedule.setUpdated(LocalDate.now());
        
        scheduleMap.put(id, schedule);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(gson.toJson(schedule));
    }
    
    // 일정 삭제
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getPathInfo());
        if (!scheduleMap.containsKey(id)) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Schedule Not Found\"}");
            return;
        }
        Schedule schedule = scheduleMap.remove(id);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("{\"message\": \"Schedule deleted successfully\"}");
    }
}
