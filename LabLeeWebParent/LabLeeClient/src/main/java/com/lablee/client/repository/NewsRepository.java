package com.lablee.client.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lablee.common.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

	List<News> findFirst4ByOrderByUpdateAtDesc();

	@Query("""
			SELECT n
			FROM News n
			WHERE n.enabled = TRUE
			""")
	Page<News> findAllEnabled(Pageable pageable);

	@Query("""
			SELECT n
			 FROM News n
			 WHERE n.enabled = true
			   AND (
			         (:keyword IS NULL)
			      OR CONCAT(n.updateAt, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   )
			""")
	Page<News> findAllEnabled(@Param("keyword") String keyword, Pageable pageable);

	@Query("""
			SELECT n
			FROM News n
			WHERE n.enabled = TRUE
				AND n.id = :id
			""")
	Optional<News> findByIdEnabled(@Param("id") int id);

}
