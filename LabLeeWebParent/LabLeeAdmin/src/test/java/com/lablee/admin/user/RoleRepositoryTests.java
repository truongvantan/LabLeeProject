package com.lablee.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.lablee.admin.role.RoleRepository;
import com.lablee.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	
	@Autowired
	private RoleRepository repo;
	
	@Test
	@Disabled("Temporarily disabled")
	public void testCreateFirstRole() {
		Role roleRootAdmin = new Role("Root Admin", "Quản lí toàn bộ hệ thống");
		Role saved = repo.save(roleRootAdmin);
		
		assertThat("Root Admin".equals(saved.getName()));
	}
	
	@Test
	@Disabled("Temporarily disabled")
	public void testCreateMultipleRoles() {
		Role roleMember = new Role("Member", "Thành viên lab");
		Role roleCollaborator = new Role("Collaborator", "Cộng tác viên đăng bài");
		Role roleContentModerator = new Role("Content Moderator", "Kiểm duyệt nội dung");
		
		repo.saveAll(List.of(roleMember, roleCollaborator, roleContentModerator));
		
		
	}
	

	
}
