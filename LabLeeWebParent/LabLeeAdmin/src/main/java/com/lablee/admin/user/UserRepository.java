package com.lablee.admin.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lablee.common.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query("SELECT u FROM User u WHERE u.deleted = FALSE")
	List<User> findAllWithoutDelete();

	Optional<User> findByEmail(String email);
	
	@Query("UPDATE User u SET u.deleted = TRUE WHERE u.id = :id")
	@Modifying
	@Transactional
	void deleteByIdSoft(@Param("id") Integer id);

	List<User> findAllByOrderByIdAsc();
	
	@Query("""
			SELECT u
			FROM User u
			WHERE 
				CAST(u.id AS string) LIKE %:keyword% OR
				u.email LIKE %:keyword% OR
				u.firstName LIKE %:keyword% OR
				u.lastName LIKE %:keyword%
			""")
	Page<User> findAll(@Param("keyword") String keyword, Pageable pageable);
}
