package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.LectureRequest;
import com.mindmapper.courseManagement.dto.LectureResponse;
import com.mindmapper.courseManagement.dto.SectionLectureResponse;
import com.mindmapper.utility.Response;
import org.springframework.web.multipart.MultipartFile;

public interface LectureService {

    /**
     * Adds a new lecture to a section.
     *
     * @param lectureRequest the request containing lecture details
     * @return a response indicating the result of the operation
     */
    LectureResponse addLecture(LectureRequest lectureRequest, MultipartFile lectureVideo);

    /**
     * Deletes a lecture by its ID.
     *
     * @param lectureId the ID of the lecture to delete
     * @return a response indicating the result of the operation
     */
    Response deleteLecture(Long lectureId);

    /**
     * Updates an existing lecture.
     *
     * @param lectureRequest the request containing updated lecture details
     * @return a response indicating the result of the operation
     */
    LectureResponse updateLecture(LectureRequest lectureRequest,Long lectureId);

    SectionLectureResponse getLectureBySectionId(Long sectionId);

    // Video stream of lecture will be done further in the project
}
