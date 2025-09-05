package com.learnsphere.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="reviews",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","course_id"}))
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(optional=false) @JoinColumn(name="course_id")
    private Course course;

    @Column(nullable=false)
    private int rating; // 0..10

    @Column(length=2000)
    private String text;

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Review() {}
    public Review(User user, Course course, int rating, String text) {
        this.user = user; this.course = course; this.rating = rating; this.text = text;
    }

    // getters/setters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Course getCourse() { return course; }
    public int getRating() { return rating; }
    public String getText() { return text; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setUser(User user) { this.user = user; }
    public void setCourse(Course course) { this.course = course; }
    public void setRating(int rating) { this.rating = rating; }
    public void setText(String text) { this.text = text; }

    @Override public String toString() {
        return "Review{id=" + id + ", user=" + user.getEmail() +
               ", course=" + course.getName() + ", rating=" + rating + "}";
    }
}
