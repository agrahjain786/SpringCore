package com.techlabs.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

	Page<Blog> findByPublishedTrue(Pageable pageable);

	Page<Blog> findByPublishedFalse(Pageable pageable);

}
