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

	@Query("""
			SELECT p
			FROM Publication p
			WHERE
				CONCAT(p.id, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(p.cite) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(p.doiLink) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(p.publicationAbstract) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(p.publishYear) LIKE LOWER(CONCAT('%', :keyword, '%'))
			""")
	Page<Publication> findAll(@Param("keyword") String keyword, Pageable pageable);

}
