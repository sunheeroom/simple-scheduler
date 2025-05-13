package scheduler;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import scheduler.entity.Schedule;

public class SimpleScheduler {
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private static final Scanner sc = new Scanner(System.in);
    private static Map<Integer, Schedule> scheduleMap = new HashMap<>();
    
    public static void main(String[] args) {
        while (true) {
            
            System.out.println("\n 일정관리 프로그램");
            System.out.println("1. 일정 생성");
            System.out.println("2. 일정 조회");
            System.out.println("3. 일정 수정");
            System.out.println("4. 일정 삭제");
            System.out.println("0. 종료");
            System.out.print("선택: ");
            String input = sc.nextLine();
            
            switch (input) {
                case "1" -> create();
                case "2" -> list();
                case "3" -> find();
                case "4" -> update();
                case "5" -> delete();
                case "0" -> {
                    System.out.println("일정관리 프로그램 종료");
                    return;
                }
                default -> System.out.println("잘못된 입력값입니다.");
            }
        }
    }
    
    private static int findSchedule() {
        //        System.out.print("조회할 일정 ID: ");
        int id = Integer.parseInt(sc.nextLine());
        
        if (!scheduleMap.containsKey(id)) {
            System.out.println("해당 일정이 존재하지 않습니다");
            return -1;
        }
        return id;
    }
    
    private static void delete() {
        System.out.print("삭제할 일정 ID: ");
        int id = findSchedule();
        if (id == -1) {
            return;
        }
        if (!checkPassword(id)) {
            System.out.println("비밀번호가 일치하지 않습니다.");
            return;
        }
        
        scheduleMap.remove(id);
        System.out.println("일정이 삭제되었습니다");
    }
    
    private static boolean checkPassword(int id) {
        System.out.print("비밀번호: ");
        String password = sc.nextLine();
        
        Schedule schedule = scheduleMap.get(id);
        return schedule.password.equals(password);
    }
    
    /**
     * 입력받은 ID의 일정을 수정한다.
     */
    private static void update() {
        System.out.print("수정할 일정 ID: ");
        int id = findSchedule();
        if (id == -1) {
            return;
        }
        
        if (!checkPassword(id)) {
            System.out.println("비밀번호가 일치하지 않습니다.");
            return;
        }
        
        Schedule schedule = scheduleMap.get(id);
        System.out.print("새 제목: ");
        schedule.title = sc.nextLine();
        
        System.out.print("새 작성자: ");
        schedule.user = sc.nextLine();
        
        schedule.updated = LocalDate.now();
        System.out.println("일정이 수정되었습니다.");
    }
    
    /*
     * 입력받은 ID가 있으면 해당 일정을 조회한다.
     */
    
    private static void find() {
        if (scheduleMap.isEmpty()) {
            System.out.println("등록된 일정이 없습니다.");
            return;
        }
        int id = findSchedule();
        if (id == -1) {
            return;
        }
        
        Schedule schedule = scheduleMap.get(id);
        System.out.println("일정 ID: " + schedule.id);
        System.out.println("제목: " + schedule.title);
        System.out.println("작성자: " + schedule.user);
        System.out.println("작성일: " + schedule.created);
        System.out.println("수정일: " + schedule.updated);
    }
    
    /**
     * 엔터를 누르면 전체 일정을 조회한다.
     */
    private static void list() {
        if (scheduleMap.isEmpty()) {
            System.out.println("등록된 일정이 없습니다");
            return;
        }
        System.out.println("전체 일정 목록(최신 업데이트순 정렬)");
        
        scheduleMap.values().stream()
            .sorted((a, b) -> b.updated.compareTo(a.updated))
            .forEach(sch -> System.out.printf("[%d] %s - %s (%s)\n",
                sch.id, sch.title, sch.user, sch.updated));
    }
    
    /**
     * 일정을 생성하고, 일정 목록에 저장한다.
     */
    private static void create() {
        System.out.print("할 일: ");
        String title = sc.nextLine();
        System.out.print("작성자: ");
        String user = sc.nextLine();
        System.out.print("비밀번호: ");
        String password = sc.nextLine();
        
        int id = idCounter.getAndIncrement();
        Schedule schedule = new Schedule(id, title, user, password);
        scheduleMap.put(id, schedule);
    }
}