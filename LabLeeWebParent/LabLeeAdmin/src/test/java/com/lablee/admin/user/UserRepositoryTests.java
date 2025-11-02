package com.lablee.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.lablee.common.entity.Role;
import com.lablee.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	@Test
	@Disabled("")
	public void testCreateFirstUser() {
		User user = new User("example@gmail.com", "password", "Tân", "Trương Văn");
		Role roleRootAdmin = new Role(1);
		
		user.addRole(roleRootAdmin);
		
		User saved = repo.save(user);
		
		assertThat(saved.getId()).isGreaterThan(0);
	}
	
	@Test
	@Disabled("")
	public void testCreateListUser() {
		User user1 = new User("ronaldo@gmail.com", "password", "Ronaldo", "Cristiano");
		User user2 = new User("messi@gmail.com", "password", "Messi", "Lionel");
		User user3 = new User("halland@gmail.com", "password", "Halland", "Erling");
		
		Role roleMember = new Role(2);
		
		user1.addRole(roleMember);
		user2.addRole(roleMember);
		user3.addRole(roleMember);
		
		repo.saveAll(List.of(user1, user2, user3));
		
	}
	
	@Test
	@Disabled("")
	public void testCreateUserWithMultipleRoles() {
		User user = new User("johndoe@gmail.com", "password", "Doe", "John");
		Role roleMember = new Role(2);
		Role roleContentModerator = new Role(4);
		
		user.addRole(roleMember);
		user.addRole(roleContentModerator);
		
		User saved = repo.save(user);
		
		assertThat(saved.getId()).isGreaterThan(0);
	}
	
	@Test
	@Disabled("")
	public void testGetAllUser() {
		List<User> listUsers = repo.findAll();
		
		System.out.println("Count: " + listUsers.size());
		
		for (User u : listUsers) {
			System.out.println(u);
		}
	}
	
	@Test
	@Disabled("")
	public void testGetAllUserWithoutDelete() {
		List<User> listUsers = repo.findAllWithoutDelete();
		
		System.out.println("Count: " + listUsers.size());
		
		for (User u : listUsers) {
			System.out.println(u);
		}
	}
	
	@Test
	@Disabled("")
	public void testUpdateUserInfo() {
		User userRootAdmin = repo.findById(1).get();
		
		userRootAdmin.setEnabled(true);
		userRootAdmin.setEmail("tan@gmail.com");
		
		repo.save(userRootAdmin);
		
	}
	
	@Test
	@Disabled("")
	public void testUpdateUserRole() {
		User userJohn = repo.findById(3).get();
		Role roleContentModerator = new Role(4);
		Role roleCollaborator = new Role(3);
		userJohn.getSetRoles().remove(roleContentModerator);
		userJohn.addRole(roleCollaborator);
		
		repo.save(userJohn);
		
	}
	
	@Test
	@Disabled("")
	public void testHardDeleteUser() {
		Optional<User> optionalUser = repo.findByEmail("johndoe@gmail.com");
		
		if (optionalUser.isPresent()) {
			User userJane = optionalUser.get();
			repo.deleteById(userJane.getId());
		}
		
	}
	
	@Test
	@Disabled("")
	public void testSoftDeleteUser() {
		Optional<User> optionalUser = repo.findByEmail("johndoe@gmail.com");
		
		if (optionalUser.isPresent()) {
			User userJohn = optionalUser.get();
			repo.deleteByIdSoft(userJohn.getId());
			
		}
		
	}
}
