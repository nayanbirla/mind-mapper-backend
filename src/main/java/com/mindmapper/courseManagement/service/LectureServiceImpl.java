package com.mindmapper.courseManagement.service;

import com.mindmapper.courseManagement.dto.LectureRequest;
import com.mindmapper.courseManagement.dto.LectureResponse;
import com.mindmapper.courseManagement.dto.SectionLectureResponse;
import com.mindmapper.entity.Lecture;
import com.mindmapper.entity.Section;
import com.mindmapper.repository.LectureRepository;
import com.mindmapper.repository.SectionRepository;
import com.mindmapper.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class LectureServiceImpl implements LectureService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Override
    public LectureResponse addLecture(LectureRequest lectureRequest, MultipartFile videoFile) {

        validateLectureRequest(lectureRequest);

        if (videoFile == null || videoFile.isEmpty()) {
            throw new IllegalArgumentException("Video file cannot be null or empty");
        }

        Optional<Section> sectionOptional = sectionRepository.findById(lectureRequest.getSectionId());

        if(!sectionOptional.isPresent()) {
            throw new IllegalArgumentException("Section not found with ID: " + lectureRequest.getSectionId());
        }
        // Lecture is an entity class that maps to a database table
        Lecture lecture = new Lecture();
        lecture.setTitle(lectureRequest.getLectureName());
        lecture.setSection(sectionOptional.get());

        // Here you would typically handle the video file, e.g., save it to a storage service
        lecture.setVideoUrl(uploadVideoLectureOrPdf(videoFile));

        lecture = lectureRepository.save(lecture);

        return new LectureResponse("Lecture added successfully",lecture.getLectureId(),lecture.getTitle(),lecture.getSection().getSectionId());
    }

    @Override
    public Response deleteLecture(Long lectureId) {
        if (lectureId == null) {
            throw new IllegalArgumentException("Lecture ID cannot be null");
        }

        Optional<Lecture> lectureOptional = lectureRepository.findById(lectureId);
        if (!lectureOptional.isPresent()) {
            throw new IllegalArgumentException("Lecture not found with ID: " + lectureId);
        }

        lectureRepository.delete(lectureOptional.get());
        return new Response("Lecture deleted successfully", "200");
    }

    @Override
    public LectureResponse updateLecture(LectureRequest lectureRequest,Long lectureId) {

        validateLectureRequest(lectureRequest);

        if(lectureId== null) {
            throw new IllegalArgumentException("Lecture ID cannot be null");
        }

        Optional<Lecture> lectureOptional = lectureRepository.findById(lectureId);
        if (!lectureOptional.isPresent()) {
            throw new IllegalArgumentException("Lecture not found with ID: " + lectureId);
        }

        Lecture lecture = lectureOptional.get();
        lecture.setTitle(lectureRequest.getLectureName());
        Optional<Section> sectionOptional = sectionRepository.findById(lectureRequest.getSectionId());
        if (!sectionOptional.isPresent()) {
            throw new IllegalArgumentException("Section not found with ID: " + lectureRequest.getSectionId());
        }
        lecture.setSection(sectionOptional.get());

        lectureRepository.save(lecture);

        return new LectureResponse("Lecture updated successfully", lecture.getLectureId(), lecture.getTitle(), lecture.getSection().getSectionId());

    }

    @Override
    public SectionLectureResponse getLectureBySectionId(Long sectionId) {
        if (sectionId == null) {
            throw new IllegalArgumentException("Section ID cannot be null");
        }

        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);
        if (!sectionOptional.isPresent()) {
            throw new IllegalArgumentException("Section not found with ID: " + sectionId);
        }

        Section section = sectionOptional.get();
        List<Lecture> lecture = lectureRepository.findAllLectureBySectionId(section.getSectionId());
        if (lecture.isEmpty()) {
            throw new IllegalArgumentException("No lectures found for section ID: " + sectionId);
        }
        return new SectionLectureResponse("Lecture retrieved successfully", lecture);
    }

    private void validateLectureRequest(LectureRequest lectureRequest) {
        if (lectureRequest == null) {
            throw new IllegalArgumentException("Lecture request cannot be null");
        }
        if (lectureRequest.getSectionId() == null) {
            throw new IllegalArgumentException("Section ID cannot be null");
        }
        if (lectureRequest.getLectureName() == null || lectureRequest.getLectureName().isEmpty()) {
            throw new IllegalArgumentException("Lecture name cannot be empty");
        }
        // Additional validations can be added as needed
    }

    private String uploadVideoLectureOrPdf(MultipartFile lectureFile) {

        try {
            // Path to static/images (during development)
            String uploadDir = new File("/uploads/lecture-content").getAbsolutePath();

            // Ensure the directory exists
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Create full path to save
            String originalFilename = lectureFile.getOriginalFilename();
            String filePath = uploadDir + File.separator + originalFilename;

            // Save the file
            File dest = new File(filePath);
            lectureFile.transferTo(dest);

            // Return path for use (you can construct public URL)
            return "/uploads/lecture-content" + originalFilename;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload video", e);
        }
    }

}
