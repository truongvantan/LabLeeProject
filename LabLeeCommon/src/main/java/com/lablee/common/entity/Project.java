package com.lablee.common.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.lablee.common.constant.ConstantUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate startDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate endDate;

	private String sponsor;
	
	private String thumbnail;

	@Column(columnDefinition = "TEXT", nullable = true)
	private String projectAbstract;

	@Column(columnDefinition = "TEXT", nullable = true)
	private String content;

	private double budget;

	private boolean enabled;

	@ManyToMany
	@JoinTable(name = "members_projects", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "member_id"))
	private Set<MemberLabProfile> members = new HashSet<>();

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
		Project other = (Project) obj;
		return Objects.equals(id, other.id);
	}
	
	@Transient
	public String getThumbnailPath() {
		if (this.id == null || this.thumbnail == null) {
			return "/images/default-user.png";
		}

		return new StringBuffer(ConstantUtil.PATH_PROJECT_THUMBNAIL_STORED_DEFAULT).append(this.id).append("/")
				.append(this.thumbnail).toString();
	}
	
	@Transient
	public String getShortTitle() {
		if (title != null && title.length() > 60) {
			return title.substring(0, 60) + "...";
		}
		
		return title;
	}
	
	@Transient
	public String getShortAbstract() {
		if (projectAbstract != null && projectAbstract.length() > 60) {
			return projectAbstract.substring(0, 60) + "...";
		}
		
		return projectAbstract;
	}
	
	@Transient
	public String getListMemberName() {
		if (members == null || members.isEmpty()) {
			return "";
		}
		
		List<String> listMemberName = new ArrayList<>();
		for (MemberLabProfile member : members) {
			listMemberName.add(member.getUser().getFullName());
		}
		
		String result = String.join(", ", listMemberName);
		
		if (result != null && result.length() > 60) {
			return result.substring(0, 60) + "...";
		}
		
		return result;
	}

}
