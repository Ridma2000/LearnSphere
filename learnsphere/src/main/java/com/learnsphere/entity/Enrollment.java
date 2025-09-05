package com.learnsphere.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="enrollments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","course_id"}))
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(optional=false) @JoinColumn(name="course_id")
    private Course course;

    @Column(nullable=false)
    private LocalDateTime enrolledAt = LocalDateTime.now();

    public Enrollment() {}
    public Enrollment(User user, Course course) { this.user = user; this.course = course; }

    // getters/setters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Course getCourse() { return course; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }

    public void setUser(User user) { this.user = user; }
    public void setCourse(Course course) { this.course = course; }

    @Override public String toString() {
        return "Enrollment{id=" + id + ", user=" + user.getEmail() +
               ", course=" + course.getName() + ", enrolledAt=" + enrolledAt + "}";
    }
}
