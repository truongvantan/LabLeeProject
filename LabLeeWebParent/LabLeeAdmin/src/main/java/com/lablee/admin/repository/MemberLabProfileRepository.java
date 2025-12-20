package com.lablee.admin.repository;

import java.util.List;
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

	@Query("""
			SELECT m
			FROM MemberLabProfile m
			WHERE
				CONCAT(m.id, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR CONCAT(m.joinDate, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR CONCAT(m.leaveDate, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(m.currentOrganization) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(m.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(m.emailPublic) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(m.researchInterests) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(m.academicRank) LIKE LOWER(CONCAT('%', :keyword, '%'))
			""")
	Page<MemberLabProfile> findAll(@Param("keyword") String keyword, Pageable pageable);

	@Query("""
			SELECT m FROM MemberLabProfile m JOIN FETCH m.user u
			WHERE m.id = :id
			""")
	Optional<MemberLabProfile> findByIdCustom(@Param("id") int id);
	
	@Query("""
			SELECT m
			FROM MemberLabProfile m
			WHERE m.enabled = TRUE
			""")
	List<MemberLabProfile> findAllEnabled();

}
