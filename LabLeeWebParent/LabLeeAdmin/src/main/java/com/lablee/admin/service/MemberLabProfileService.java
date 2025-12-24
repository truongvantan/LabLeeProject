package com.lablee.admin.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.lablee.admin.dto.MemberLabProfileFormAddDTO;
import com.lablee.admin.dto.MemberLabProfileFormEditDTO;
import com.lablee.admin.exception.MemberLabProfileNotFoundException;
import com.lablee.admin.mapper.MemberLabProfileMapper;
import com.lablee.admin.repository.MemberLabProfileRepository;
import com.lablee.admin.util.FileUploadUtil;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.MemberLabProfile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberLabProfileService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MemberLabProfileService.class);

	private final MemberLabProfileRepository memberLabProfileRepository;
	private final MemberLabProfileMapper memberLabProfileMapper;

	/**
	 * @return Object[0]: List(MemberLabProfileDTO)<br>
	 *         Object[1]: int totalPages<br>
	 *         Object[2]: long totalElements
	 */
	public Object[] listByPage(String strPageNum, String keyword, String sortField, String sortDir) {
		List<MemberLabProfile> listMemberLabDTOs = new ArrayList<>();

		int pageNum = 1;

		try {
			pageNum = Integer.parseInt(strPageNum);
		} catch (NumberFormatException e) {
			pageNum = 1;
		}

		Sort sort = Sort.by(sortField);
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_SIZE_DEFAULT, sort);

		Page<MemberLabProfile> pageMemberLabProfile = null;

		if (keyword == null || keyword.isBlank()) {
			pageMemberLabProfile = memberLabProfileRepository.findAll(pageable);
		} else {
			pageMemberLabProfile = memberLabProfileRepository.findAll(keyword.trim(), pageable);
		}

		int totalPages = pageMemberLabProfile.getTotalPages();
		long totalElements = pageMemberLabProfile.getTotalElements();

		listMemberLabDTOs = pageMemberLabProfile.getContent();

		return new Object[] { listMemberLabDTOs, totalPages, totalElements };
	}

	@Transactional
	public String addNewMember(MemberLabProfileFormAddDTO memberLabProfileFormAddDTO, BindingResult bindingResult,
			MultipartFile multipartFile) {

		// validation binding result form
		if (bindingResult.hasErrors()) {
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		// validation public email
		if (memberLabProfileFormAddDTO.getEmailPublic() != null
				&& !memberLabProfileFormAddDTO.getEmailPublic().isBlank()
				&& !memberLabProfileFormAddDTO.getEmailPublic().matches(ConstantUtil.REGEX_EMAIL)) {
			bindingResult.rejectValue("emailPublic", "memberLabProfileFormAddDTO.emailPublic",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_EMAIL);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		// validation file size
		if (!FileUploadUtil.isValidFileSize(multipartFile)) {
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_UPLOAD_FILE_SIZE_1MB;
		}

		// validation avatar
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			memberLabProfileFormAddDTO.setAvatar(fileName);
		} else if ("".equals(memberLabProfileFormAddDTO.getAvatar())
				|| memberLabProfileFormAddDTO.getAvatar() == null) {
			memberLabProfileFormAddDTO.setAvatar(null);
		}

		if (memberLabProfileFormAddDTO.getJoinDate() == null) {
			bindingResult.rejectValue("joinDate", "memberLabProfileFormAddDTO.joinDate",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_DATE);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		// Validation leaveDate must >= joinDate
		if (memberLabProfileFormAddDTO.getLeaveDate() != null && memberLabProfileFormAddDTO.getJoinDate() != null
				&& memberLabProfileFormAddDTO.getLeaveDate().isBefore(memberLabProfileFormAddDTO.getJoinDate())) {
			bindingResult.rejectValue("leaveDate", "memberLabProfileFormAddDTO.leaveDate",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_LEAVE_DATE_MEMBER_LAB_PROFILE);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		MemberLabProfile memberLabProfile = memberLabProfileMapper.formAddDTOToEntity(memberLabProfileFormAddDTO);

		MemberLabProfile savedMemberLabProfile = memberLabProfileRepository.save(memberLabProfile);
		String uploadDir = ConstantUtil.PATH_MEMBER_LAB_AVATAR_UPLOAD_DIR_DEFAULT + savedMemberLabProfile.getId();

		try {
			if (multipartFile != null && !multipartFile.isEmpty()) {
				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, savedMemberLabProfile.getAvatar(), multipartFile);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return ConstantUtil.MESSAGE_FAIL_ADD_MEMBER_LAB_PROFILE;
		}

		return ConstantUtil.MESSAGE_SUCCESS_ADD_MEMBER_LAB_PROFILE;
	}

	public MemberLabProfileFormEditDTO findById(String memberLabProfileId) throws MemberLabProfileNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(memberLabProfileId);
		} catch (NumberFormatException e) {
			throw new MemberLabProfileNotFoundException("Could not find any member with ID: " + memberLabProfileId);
		}

		Optional<MemberLabProfile> oMemberLabProfile = memberLabProfileRepository.findById(id);

		if (oMemberLabProfile.isPresent()) {
			MemberLabProfile memberLabProfile = oMemberLabProfile.get();
			MemberLabProfileFormEditDTO memberLabProfileFormEditDTO = memberLabProfileMapper
					.entityToFormEditDTO(memberLabProfile);

			return memberLabProfileFormEditDTO;
		} else {
			throw new MemberLabProfileNotFoundException("Could not find any member with ID: " + memberLabProfileId);
		}

	}

	@Transactional
	public String editMember(@Valid MemberLabProfileFormEditDTO memberLabProfileFormEditDTO,
			BindingResult bindingResult, MultipartFile multipartFile) {
		// validation binding result form
		if (bindingResult.hasErrors()) {
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		// validation public email
		if (memberLabProfileFormEditDTO.getEmailPublic() != null
				&& !memberLabProfileFormEditDTO.getEmailPublic().isBlank()
				&& !memberLabProfileFormEditDTO.getEmailPublic().matches(ConstantUtil.REGEX_EMAIL)) {
			bindingResult.rejectValue("emailPublic", "memberLabProfileFormEditDTO.emailPublic",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_EMAIL);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		// validation file size
		if (!FileUploadUtil.isValidFileSize(multipartFile)) {
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_UPLOAD_FILE_SIZE_1MB;
		}

		// validation avatar
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			memberLabProfileFormEditDTO.setAvatar(fileName);
		} else if ("".equals(memberLabProfileFormEditDTO.getAvatar())
				|| memberLabProfileFormEditDTO.getAvatar() == null) {
			memberLabProfileFormEditDTO.setAvatar(null);
		}

		if (memberLabProfileFormEditDTO.getJoinDate() == null) {
			bindingResult.rejectValue("joinDate", "memberLabProfileFormEditDTO.joinDate",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_DATE);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		// Validation leaveDate must >= joinDate
		if (memberLabProfileFormEditDTO.getLeaveDate() != null && memberLabProfileFormEditDTO.getJoinDate() != null
				&& memberLabProfileFormEditDTO.getLeaveDate().isBefore(memberLabProfileFormEditDTO.getJoinDate())) {
			bindingResult.rejectValue("leaveDate", "memberLabProfileFormEditDTO.leaveDate",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_LEAVE_DATE_MEMBER_LAB_PROFILE);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		MemberLabProfile memberLabProfile = memberLabProfileMapper.formEditDTOToEntity(memberLabProfileFormEditDTO);

		MemberLabProfile savedMemberLabProfile = memberLabProfileRepository.save(memberLabProfile);
		String uploadDir = ConstantUtil.PATH_MEMBER_LAB_AVATAR_UPLOAD_DIR_DEFAULT + savedMemberLabProfile.getId();

		try {
			if (multipartFile != null && !multipartFile.isEmpty()) {
				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, savedMemberLabProfile.getAvatar(), multipartFile);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return ConstantUtil.MESSAGE_FAIL_EDIT_MEMBER_LAB_PROFILE;
		}

		return ConstantUtil.MESSAGE_SUCCESS_EDIT_MEMBER_LAB_PROFILE;
	}

	public void updateMemberLabProfileEnabledStatus(String memberLabProfileId, boolean enabled)
			throws MemberLabProfileNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(memberLabProfileId);
			Optional<MemberLabProfile> oMemberLabProfile = memberLabProfileRepository.findById(id);

			if (oMemberLabProfile.isPresent()) {
				MemberLabProfile memberLabProfile = oMemberLabProfile.get();
				memberLabProfile.setEnabled(enabled);
				memberLabProfileRepository.save(memberLabProfile);
			} else {
				throw new MemberLabProfileNotFoundException(
						"Could not find any member lab profile with ID " + memberLabProfileId);
			}
			
		} catch (NumberFormatException e) {
			throw new MemberLabProfileNotFoundException(
					"Could not find any member lab profile with ID " + memberLabProfileId);
		}
	}

	public List<MemberLabProfile> findAllEnabled() {

		List<MemberLabProfile> listMembers = new ArrayList<>();

		listMembers = memberLabProfileRepository.findAllEnabled();

		return listMembers;
	}

}
