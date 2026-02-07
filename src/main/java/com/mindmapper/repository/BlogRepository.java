package com.mindmapper.repository;

import com.mindmapper.entity.Blog;
import com.mindmapper.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByAuthor(UserInfo author);
}
