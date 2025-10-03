package com.mindmapper.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false,length = 100)
    private String title;

    @Column(nullable = true,length = 1000)
    private String description;

    @Column(nullable = false,length = 100)
    private String category;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = true)
    private String level;

    @Column(nullable = true)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private UserInfo instructorId;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserInfo getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(UserInfo instructorId) {
        this.instructorId = instructorId;
    }
}
