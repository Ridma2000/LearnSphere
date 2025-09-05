package com.learnsphere.app;

import com.learnsphere.entity.Course;
import com.learnsphere.entity.User;
import com.learnsphere.service.LearnSphereService;

import java.util.*;

public class App {
    private static final Scanner in = new Scanner(System.in);
    private static final LearnSphereService service = new LearnSphereService();

    public static void main(String[] args) {
        System.out.println("=== LearnSphere Console (Hibernate) ===");
        seedIfEmpty(); // optional demo data

        while (true) {
            menu();
            String choice = in.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addCategory(); break;
                    case "2": addCourse(); break;
                    case "3": registerUser(); break;
                    case "4": enrollUser(); break;
                    case "5": listUserCourses(); break;
                    case "6": searchCourses(); break;
                    case "7": addReview(); break;
                    case "8": avgRating(); break;
                    case "9": adminList(); break;
                    case "0": System.out.println("Bye!"); return;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void menu() {
        System.out.println("\nChoose an option:");
        System.out.println("1) Add Category");
        System.out.println("2) Add Course");
        System.out.println("3) Register User");
        System.out.println("4) Enroll User in Course");
        System.out.println("5) List Courses by User");
        System.out.println("6) Search Courses (name/category) + show avg rating");
        System.out.println("7) Add/Update Review (0..10)");
        System.out.println("8) Show Average Rating for Course");
        System.out.println("9) Admin: Users with Enrollments");
        System.out.println("0) Exit");
        System.out.print("> ");
    }

    private static void addCategory() {
        System.out.print("Category name: ");
        String name = in.nextLine();
        System.out.println("Created: " + service.createCategory(name));
    }

    private static void addCourse() {
        System.out.print("Course name: ");
        String name = in.nextLine();
        System.out.print("Instructor: ");
        String instructor = in.nextLine();
        System.out.print("Duration (hours): ");
        int hours = Integer.parseInt(in.nextLine());
        System.out.print("Categories (comma-separated): ");
        String cats = in.nextLine();
        List<String> catList = new ArrayList<>();
        for (String c : cats.split(",")) if (!c.trim().isEmpty()) catList.add(c.trim());
        System.out.println("Created: " + service.addCourse(name, instructor, hours, catList));
    }

    private static void registerUser() {
        System.out.print("User name: ");
        String name = in.nextLine();
        System.out.print("Email: ");
        String email = in.nextLine();
        System.out.println("Registered: " + service.registerUser(name, email));
    }

    private static void enrollUser() {
        System.out.print("User email: ");
        String email = in.nextLine();
        User u = service.findUserByEmail(email);
        if (u == null) { System.out.println("User not found."); return; }
        System.out.print("Course id: ");
        Long cid = Long.parseLong(in.nextLine());
        System.out.println("Enrollment: " + service.enrollUserInCourse(u.getId(), cid));
    }

    private static void listUserCourses() {
        System.out.print("User email: ");
        String email = in.nextLine();
        User u = service.findUserByEmail(email);
        if (u == null) { System.out.println("User not found."); return; }
        List<Course> cs = service.listCoursesByUser(u.getId());
        if (cs.isEmpty()) System.out.println("No enrollments.");
        else {
            System.out.println("Enrolled courses:");
            cs.forEach(c -> System.out.println(" - " + c.getId() + ": " + c.getName()
                    + " (" + c.getInstructor() + ", " + c.getDurationHours() + "h)"));
        }
    }

    private static void searchCourses() {
        System.out.print("Keyword / Category: ");
        String key = in.nextLine();
        List<Course> cs = service.searchCourses(key);
        if (cs.isEmpty()) { System.out.println("No courses found."); return; }
        for (Course c : cs) {
            Double avg = service.getAverageRatingForCourse(c.getId());
            String avgTxt = (avg == null) ? "No ratings yet" : String.format(Locale.US, "%.2f / 10", avg);
            System.out.println(c.getId() + " | " + c.getName() + " | " + c.getInstructor()
                    + " | " + c.getDurationHours() + "h | Avg: " + avgTxt);
        }
    }

    private static void addReview() {
        System.out.print("User email: ");
        String email = in.nextLine();
        User u = service.findUserByEmail(email);
        if (u == null) { System.out.println("User not found."); return; }
        System.out.print("Course id: ");
        Long cid = Long.parseLong(in.nextLine());
        System.out.print("Rating (0..10): ");
        int rating = Integer.parseInt(in.nextLine());
        System.out.print("Review text: ");
        String text = in.nextLine();
        System.out.println("Saved: " + service.addReview(u.getId(), cid, rating, text));
    }

    private static void avgRating() {
        System.out.print("Course id: ");
        Long cid = Long.parseLong(in.nextLine());
        Double avg = service.getAverageRatingForCourse(cid);
        System.out.println("Average rating: " + (avg == null ? "No ratings yet" : String.format(Locale.US, "%.2f / 10", avg)));
    }

    private static void adminList() {
        Map<User, List<Course>> map = service.adminUsersWithEnrollments();
        if (map.isEmpty()) { System.out.println("No users."); return; }
        map.forEach((u, cs) -> {
            System.out.println(u.getName() + " <" + u.getEmail() + ">");
            if (cs.isEmpty()) System.out.println("  (no enrollments)");
            else cs.forEach(c -> System.out.println("  - " + c.getId() + ": " + c.getName()));
        });
    }

    private static void seedIfEmpty() {
        // Add a couple of categories/courses/users for quick testing if DB is empty
        if (service.listCourses().isEmpty()) {
            service.createCategory("Programming");
            service.createCategory("Design");
            service.createCategory("Business");

            service.addCourse("Java Fundamentals", "Alice Johnson", 24, Arrays.asList("Programming"));
            service.addCourse("Creative Marketing", "Bob Lee", 18, Arrays.asList("Business","Design"));
        }
        if (service.listUsers().isEmpty()) {
            service.registerUser("Test User", "test@learnsphere.io");
        }
    }
}
