package com.learnsphere.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100, unique=true)
    private String name;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    public Category() {}
    public Category(String name) { this.name = name; }

    // getters/setters, toString
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Set<Course> getCourses() { return courses; }

    @Override public String toString() { return "Category{id=" + id + ", name='" + name + "'}"; }
}
