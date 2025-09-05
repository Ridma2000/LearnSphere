package com.learnsphere.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses", indexes = {
        @Index(name="idx_course_name", columnList="name")
})
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String name;

    @Column(nullable=false, length=200)
    private String instructor;

    @Column(nullable=false)
    private int durationHours; // total duration

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_category",
            joinColumns = @JoinColumn(name="course_id"),
            inverseJoinColumns = @JoinColumn(name="category_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"course_id","category_id"})
    )
    private Set<Category> categories = new HashSet<>();

    public Course() {}
    public Course(String name, String instructor, int durationHours) {
        this.name = name; this.instructor = instructor; this.durationHours = durationHours;
    }

    // getters/setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getInstructor() { return instructor; }
    public int getDurationHours() { return durationHours; }
    public Set<Category> getCategories() { return categories; }

    public void setName(String name) { this.name = name; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public void setDurationHours(int durationHours) { this.durationHours = durationHours; }

    @Override public String toString() {
        return "Course{id=" + id + ", name='" + name + "', instructor='" + instructor +
                "', durationHours=" + durationHours + "}";
    }
}
