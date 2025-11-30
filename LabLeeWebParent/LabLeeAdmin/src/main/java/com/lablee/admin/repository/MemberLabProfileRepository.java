package com.lablee.admin.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lablee.common.entity.MemberLabProfile;

@Repository
public interface MemberLabProfileRepository extends JpaRepository<MemberLabProfile, Integer> {

	@Query(value = """
			SELECT *
			FROM member_profiles
			WHERE
			  -- Match ID if keyword is numeric
			  (CAST(:keyword AS TEXT) ~ '^[0-9]+$' AND id = CAST(:keyword AS INTEGER))
			  OR
			  -- ILIKE for partial match
			  email_public ILIKE CONCAT('%', :keyword, '%')
			  OR research_interests ILIKE CONCAT('%', :keyword, '%')
			  OR current_organization ILIKE CONCAT('%', :keyword, '%')
			  OR academic_rank ILIKE CONCAT('%', :keyword, '%')
			  OR
			  -- Full-text search (if text)
			  (to_tsvector('english', coalesce(email_public,'') || ' ' || coalesce(research_interests,'') || ' ' || coalesce(current_organization,'') || ' ' || coalesce(academic_rank,'')) @@ plainto_tsquery('english', :keyword))
			""", nativeQuery = true)
	Page<MemberLabProfile> findAll(@Param("keyword") String keyword, Pageable pageable);

	@Query("""
			SELECT m FROM MemberLabProfile m JOIN FETCH m.user u
			WHERE m.id = :id
			""")
	Optional<MemberLabProfile> findByIdCustom(@Param("id") int id);

}
