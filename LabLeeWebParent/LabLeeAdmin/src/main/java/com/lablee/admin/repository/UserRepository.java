package com.lablee.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lablee.common.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u JOIN FETCH u.setRoles WHERE u.email = :email")
	Optional<User> findByEmail(@Param("email") String email);

	List<User> findAllByOrderByIdAsc();

	@Query("""
			SELECT u
			FROM User u
			WHERE
				CONCAT(u.id, '') LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			""")
	Page<User> findAll(@Param("keyword") String keyword, Pageable pageable);

	@Query(value = """
			SELECT u.*
			FROM users u
			INNER JOIN users_roles ur ON u.id = ur.user_id
			INNER JOIN roles r ON r.id = ur.role_id
			WHERE r.name = 'Thành viên lab'
			ORDER BY u.id
			""", nativeQuery = true)
	List<User> findAllByRoleMemberLab();

	@Query(value = """
			SELECT u.*
			FROM users u
			INNER JOIN users_roles ur ON u.id = ur.user_id
			INNER JOIN roles r ON r.id = ur.role_id
			WHERE r.name = 'Thành viên lab' AND u.id NOT IN (SELECT user_id FROM member_profiles)
			ORDER BY u.id
			""", nativeQuery = true)
	List<User> findAllMemberLabWithoutProfile();
}
