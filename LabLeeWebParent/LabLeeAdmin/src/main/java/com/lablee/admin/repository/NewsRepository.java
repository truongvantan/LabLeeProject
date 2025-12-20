package com.lablee.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lablee.common.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

	@Query("""
			SELECT n
			FROM News n
			WHERE
				CONCAT(n.id, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR CONCAT(n.createAt, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR CONCAT(n.updateAt, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
			""")
	Page<News> findAll(@Param("keyword") String keyword, Pageable pageable);

}
