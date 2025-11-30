package com.lablee.common.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import com.lablee.common.constant.ConstantUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member_profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MemberLabProfile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 255, nullable = true)
	private String currentOrganization;

	@Column(length = 128, nullable = true)
	private String emailPublic;

	@Column(length = 255, nullable = true)
	private String researchInterests;

	@Column(length = 50, nullable = true)
	private String academicRank;

	@Column(columnDefinition = "TEXT", nullable = true)
	private String bio;

	private String avatar;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate joinDate;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate leaveDate;
	
	@Column(nullable = true)
	private boolean enabled;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

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
		MemberLabProfile other = (MemberLabProfile) obj;
		return Objects.equals(id, other.id);
	}

	@Transient
	public String getAvatarPath() {
		if (this.id == null || this.avatar == null) {
			return "/images/default-user.png";
		}

		return new StringBuffer(ConstantUtil.PATH_MEMBER_LAB_AVATAR_STORED_DEFAULT).append(this.id).append("/")
				.append(this.avatar).toString();
	}

	@Transient
	public String getDisplayPeriod() {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(ConstantUtil.FORMAT_DISPLAY_PERIOD_MEMBER_MM_YYYY);

		String joinFmt = joinDate != null ? joinDate.format(fmt) : "N/A";

		if (leaveDate == null) {
			return joinFmt + " - Present";
		}

		LocalDate now = LocalDate.now();

		// So sánh ngày hiện tại
		if (now.equals(leaveDate)) {
			return joinFmt + " - Present";
		}

		String leaveFmt = leaveDate.format(fmt);
		return joinFmt + " - " + leaveFmt;
	}

}
