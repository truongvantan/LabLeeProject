package com.lablee.client.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lablee.common.entity.Publication;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Integer> {

	@Query("""
			SELECT p
			FROM Publication p
			WHERE p.enabled = TRUE
			""")
	Page<Publication> findAllEnabled(Pageable pageable);

	@Query("""
			SELECT p
			 FROM Publication p
			 WHERE p.enabled = true
			   AND (
			         (:keyword IS NULL)
			      OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(p.cite) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(p.doiLink) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(p.publicationAbstract) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      OR LOWER(p.publishYear) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   )
			""")
	Page<Publication> findAllEnabled(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("""
			SELECT COUNT(p)
			FROM Publication p
			WHERE p.enabled = TRUE
			""")
	long getTotalPublicationsEnabled();

	List<Publication> findFirst3ByOrderByPublishYearDesc();
	
	

}
