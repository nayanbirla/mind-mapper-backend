package com.mindmapper.courseManagement.dto;

import com.mindmapper.utility.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddCourseResponse extends Response {

    private Long courseId;


    public AddCourseResponse(String message, String statusCode, Long courseId) {
        super(message, statusCode);
        this.courseId = courseId;
    }
}
