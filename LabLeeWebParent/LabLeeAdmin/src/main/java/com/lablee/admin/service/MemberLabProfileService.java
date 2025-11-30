package com.lablee.admin.service;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.lablee.admin.dto.UserDTO;
import com.lablee.admin.exception.MemberLabProfileNotFoundException;
import com.lablee.admin.repository.MemberLabProfileRepository;
import com.lablee.admin.repository.UserRepository;
import com.lablee.admin.util.FileUploadUtil;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.MemberLabProfile;
import com.lablee.common.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberLabProfileService {

	private final MemberLabProfileRepository memberLabProfileRepository;
	private final UserRepository userRepository;
	
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

//		listUserDTOs = userMapper.toDTOList(listUsers);

		return new Object[] { listMemberLabDTOs, totalPages, totalElements };
	}

	@Transactional
	public String addNewMember(MemberLabProfile memberLabProfileFormAddDTO, MultipartFile multipartFile) {
		// validation file size
		if (!FileUploadUtil.isValidFileSize(multipartFile)) {
			return ConstantUtil.MESSAGE_VALIDATION_UPLOAD_FILE_SIZE_1MB_FAIL;
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
			return "Ngày vào lab không hợp lệ";
		}

		// Validation leaveDate must >= joinDate
		if (memberLabProfileFormAddDTO.getLeaveDate() != null && memberLabProfileFormAddDTO.getJoinDate() != null
				&& memberLabProfileFormAddDTO.getLeaveDate().isBefore(memberLabProfileFormAddDTO.getJoinDate())) {

			return "Ngày tốt nghiệp phải sau ngày vào lab";
		}

		MemberLabProfile savedMemberLabProfile = memberLabProfileRepository.save(memberLabProfileFormAddDTO);
		String uploadDir = ConstantUtil.PATH_MEMBER_LAB_AVATAR_UPLOAD_DIR_DEFAULT + savedMemberLabProfile.getId();

		try {
			if (multipartFile != null && !multipartFile.isEmpty()) {
				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, savedMemberLabProfile.getAvatar(), multipartFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ConstantUtil.MESSAGE_FAIL_ADD_MEMBER_LAB_PROFILE;
		}

		return ConstantUtil.MESSAGE_SUCCESS_ADD_MEMBER_LAB_PROFILE;
	}

	public MemberLabProfile findById(String memberLabProfileId) throws MemberLabProfileNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(memberLabProfileId);
		} catch (NumberFormatException e) {
			throw new MemberLabProfileNotFoundException("Could not find member with ID: " + memberLabProfileId);
		}

		MemberLabProfile memberLabProfile = new MemberLabProfile();
		Optional<MemberLabProfile> oMemberLabProfile = memberLabProfileRepository.findById(id);
//		Optional<MemberLabProfile> oMemberLabProfile = memberLabProfileRepository.findByIdCustom(id);

		if (oMemberLabProfile.isPresent()) {
			memberLabProfile = oMemberLabProfile.get();
			return memberLabProfile;
		} else {
			throw new MemberLabProfileNotFoundException("Could not find member with ID: " + memberLabProfileId);
		}

	}

	@Transactional
	public String editMember(MemberLabProfile memberLabProfileFormEditDTO, MultipartFile multipartFile) {

		// validation file size
		if (!FileUploadUtil.isValidFileSize(multipartFile)) {
			return ConstantUtil.MESSAGE_VALIDATION_UPLOAD_FILE_SIZE_1MB_FAIL;
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
			return "Ngày vào lab không hợp lệ";
		}

		// Validation leaveDate must >= joinDate
		if (memberLabProfileFormEditDTO.getLeaveDate() != null && memberLabProfileFormEditDTO.getJoinDate() != null
				&& memberLabProfileFormEditDTO.getLeaveDate().isBefore(memberLabProfileFormEditDTO.getJoinDate())) {

			return "Ngày tốt nghiệp phải sau ngày vào lab";
		}

		MemberLabProfile savedMemberLabProfile = memberLabProfileRepository.save(memberLabProfileFormEditDTO);
		String uploadDir = ConstantUtil.PATH_MEMBER_LAB_AVATAR_UPLOAD_DIR_DEFAULT + savedMemberLabProfile.getId();

		try {
			if (multipartFile != null && !multipartFile.isEmpty()) {
				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, savedMemberLabProfile.getAvatar(), multipartFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ConstantUtil.MESSAGE_FAIL_EDIT_MEMBER_LAB_PROFILE;
		}

		return ConstantUtil.MESSAGE_SUCCESS_EDIT_MEMBER_LAB_PROFILE;
	}

	public void updateMemberLabProfileEnabledStatus(String memberLabProfileId, boolean enabled) throws MemberLabProfileNotFoundException {
		int id = -1;
		try {
			id = Integer.parseInt(memberLabProfileId);
			Optional<MemberLabProfile> oMemberLabProfile = memberLabProfileRepository.findById(id);
			if (oMemberLabProfile.isPresent()) {
				MemberLabProfile memberLabProfile = oMemberLabProfile.get();
				memberLabProfile.setEnabled(enabled);
				memberLabProfileRepository.save(memberLabProfile);
			} else {
				throw new MemberLabProfileNotFoundException("Could not find any member lab profile with ID " + memberLabProfileId);
			}
		} catch (NumberFormatException e) {
			throw new MemberLabProfileNotFoundException("Could not find any member lab profile with ID " + memberLabProfileId);
		}
	}

}
