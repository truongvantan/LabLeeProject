package com.lablee.client.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lablee.common.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

	@Query("""
			SELECT p
			FROM Project p
			WHERE p.enabled = TRUE
			""")
	Page<Project> findAllEnabled(Pageable pageable);

	@Query("""
			SELECT p
			 FROM Project p
			 WHERE p.enabled = true
			   AND (
			         (:keyword IS NULL)
			      OR CONCAT(p.budget, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR CONCAT(p.startDate, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR CONCAT(p.endDate, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(p.sponsor) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(p.projectAbstract) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   )
			""")
	Page<Project> findAllEnabled(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("""
			SELECT p
			FROM Project p
			WHERE p.enabled = TRUE
				AND p.id = :id
			""")
	Optional<Project> findByIdEnabled(@Param("id") int id);

	Project findFirstByOrderByStartDateDesc();
	
	@Query("""
			SELECT COUNT(p)
			FROM Project p
			WHERE p.enabled = TRUE
			""")
	long getTotalProjectsEnabled();

}
