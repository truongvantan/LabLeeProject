package com.lablee.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lablee.common.entity.Publication;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Integer> {

	@Query(value = """
			SELECT *
			FROM publications
			WHERE
			  -- Match ID if keyword is numeric
			  (CAST(:keyword AS TEXT) ~ '^[0-9]+$' AND id = CAST(:keyword AS INTEGER))
			  OR
			  -- ILIKE for partial match
			  title ILIKE CONCAT('%', :keyword, '%')
			  OR cite ILIKE CONCAT('%', :keyword, '%')
			  OR doi_link ILIKE CONCAT('%', :keyword, '%')
			  OR publication_abstract ILIKE CONCAT('%', :keyword, '%')
			  OR
			  -- Full-text search (if text)
			  (to_tsvector('english', coalesce(title,'') || ' ' || coalesce(cite,'') || ' ' || coalesce(doi_link,'') || ' ' || coalesce(publication_abstract,'')) @@ plainto_tsquery('english', :keyword))
			""", nativeQuery = true)
	Page<Publication> findAll(@Param("keyword") String keyword, Pageable pageable);

}
