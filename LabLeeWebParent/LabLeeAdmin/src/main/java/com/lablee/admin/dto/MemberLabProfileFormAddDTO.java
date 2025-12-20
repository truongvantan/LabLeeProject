package com.lablee.admin.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberLabProfileFormAddDTO {
	
	private Integer id;
	
	@Size(max = 255, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_255)
	private String currentOrganization;
	
	@Size(max = 128, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_128)
	private String emailPublic;
	
	@Size(max = 255, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_255)
	private String researchInterests;
	
	@Size(max = 50, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_50)
	private String academicRank;
	
	private String bio;
	
	@Size(max = 255, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_255)
	private String avatar;
	
	@NotNull(message = ConstantUtil.MESSAGE_FAIL_VALIDATION_NOT_NULL)
	@Past(message = ConstantUtil.MESSAGE_FAIL_VALIDATION_JOIN_DATE_MEMBER_LAB_PROFILE)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate joinDate;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate leaveDate;
	
	private boolean enabled;
	
	@NotNull(message = ConstantUtil.MESSAGE_FAIL_VALIDATION_NOT_NULL)
	private User user;
	
	public String getAvatarPath() {
		if (this.id == null || this.avatar == null) {
			return "/images/default-user.png";
		}

		return new StringBuffer(ConstantUtil.PATH_MEMBER_LAB_AVATAR_STORED_DEFAULT).append(this.id).append("/")
				.append(this.avatar).toString();
	}
	
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
