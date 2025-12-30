package com.lablee.admin.repository;

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
			SELECT DISTINCT p
			FROM Project p
			LEFT JOIN p.members m
			""")
	Page<Project> findAll(Pageable pageable);

	@Query("""
			SELECT DISTINCT p
			FROM Project p
			LEFT JOIN p.members m
			WHERE
				CONCAT(p.id, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR CONCAT(p.startDate, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR CONCAT(p.endDate, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR CONCAT(p.budget, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(p.sponsor) LIKE LOWER(CONCAT('%', :keyword, '%'))
			""")
	Page<Project> findAll(@Param("keyword") String keyword, Pageable pageable);

	@Query("""
			SELECT DISTINCT p
			FROM Project p
			LEFT JOIN p.members m
			WHERE p.id = :id
			""")
	Optional<Project> findById(@Param("id") int id);

}
