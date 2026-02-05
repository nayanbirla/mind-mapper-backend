package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.MarkLectureRequest;
import com.mindmapper.courseManagement.dto.ProgressResponse;
import com.mindmapper.utility.Response;

public interface ProgressService {
    Response markLectureAsComplete(MarkLectureRequest request, String userEmail);

    ProgressResponse getCourseProgress(Long courseId, String userEmail);

    Response claimCertificate(Long courseId, String userEmail);
}
