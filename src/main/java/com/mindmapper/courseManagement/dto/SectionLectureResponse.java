package com.mindmapper.courseManagement.dto;

import com.mindmapper.entity.Lecture;
import com.mindmapper.utility.Response;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class SectionLectureResponse extends Response {

    List<Lecture> lectures;

    public SectionLectureResponse() {
    }

    public SectionLectureResponse(String message, List<Lecture> lectures) {
        super(message, "200");
        this.lectures = lectures;
    }
}
