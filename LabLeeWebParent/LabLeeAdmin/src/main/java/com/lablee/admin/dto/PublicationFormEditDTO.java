package com.lablee.admin.dto;

import com.lablee.common.constant.ConstantUtil;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PublicationFormEditDTO {
	@NotNull(message = ConstantUtil.MESSAGE_FAIL_VALIDATION_NOT_NULL)
	private Integer id;

	@Size(max = 255, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_255)
	private String title;

	@Size(max = 255, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_255)
	private String doiLink;

	private String cite;
	private String publicationAbstract;
	private String thumbnail;
	private boolean enabled;
	
	@Pattern(regexp = ConstantUtil.REGEX_YEAR_4_DIGIT, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_YEAR_4_DIGIT)
	private String publishYear;

	public String getThumbnailPath() {
		if (this.id == null || this.thumbnail == null) {
			return "/images/default-user.png";
		}

		return new StringBuffer(ConstantUtil.PATH_PUBLICATION_THUMBNAIL_STORED_DEFAULT).append(this.id).append("/")
				.append(this.thumbnail).toString();
	}
}
