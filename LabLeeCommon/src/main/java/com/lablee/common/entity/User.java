package com.lablee.common.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import com.lablee.common.constant.ConstantUtil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String email;
	
	@Column(length = 128, nullable = false)
	private String password;
	
	@Column(length = 128, nullable = true)
	private String fullName;
	
	private String photo;
	private boolean enabled;
	
	
	@ManyToMany
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> setRoles = new HashSet<>();
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private MemberLabProfile memberProfile;
	
	public User(String email, String password, String fullName) {
		this.email = email;
		this.password = password;
		this.fullName = fullName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}
	
	public void addRole(Role role) {
		this.setRoles.add(role);
	}
	
//	@Transient
//	public String getFullName() {
//		return this.firstName + " " + this.lastName;
//	}
	
	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = setRoles.iterator();
		Role role = null;
		while (iterator.hasNext()) {
			role = iterator.next();
			
			if (role.getName().equals(roleName)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Transient
	public String getPhotoImagePath() {
		if (this.id == null || this.photo == null) {
			return "/images/default-user.png";
		}
		
		return new StringBuffer(ConstantUtil.PATH_USER_PHOTO_STORED_DEFAULT).append(this.id).append("/").append(this.photo).toString();
	}
}
