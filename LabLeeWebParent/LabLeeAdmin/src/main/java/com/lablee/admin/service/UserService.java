package com.lablee.admin.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.lablee.admin.dto.RoleDTO;
import com.lablee.admin.dto.UserAccountFormEditDTO;
import com.lablee.admin.dto.UserDTO;
import com.lablee.admin.dto.UserFormAddDTO;
import com.lablee.admin.dto.UserFormEditDTO;
import com.lablee.admin.exception.UserNotFoundException;
import com.lablee.admin.mapper.RoleMapper;
import com.lablee.admin.mapper.UserMapper;
import com.lablee.admin.repository.UserRepository;
import com.lablee.admin.util.FileUploadUtil;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.Role;
import com.lablee.common.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final RoleMapper roleMapper;
	private final PasswordEncoder passwordEncoder;

	public List<UserDTO> getListUserDTOs() {
		List<UserDTO> listUserDTOs = new ArrayList<>();
		List<User> listUsers = userRepository.findAllByOrderByIdAsc();

		listUserDTOs = userMapper.toDTOList(listUsers);

		return listUserDTOs;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String saveNewUser(@Valid UserFormAddDTO userFormAddDTO, BindingResult bindingResult,
			MultipartFile multipartFile) {
		// validation binding result form
		if (bindingResult.hasErrors()) {
			return ConstantUtil.MESSAGE_VALIDATION_BINDING_RESULT_FAIL;
		}

		// validation password vs repassword
		if (!userFormAddDTO.getPassword().equals(userFormAddDTO.getRepassword())) {
			return ConstantUtil.MESSAGE_VALIDATION_REPASSWORD_PASSWORD_USER_FAIL;
		}

		// validation file size
		if (!FileUploadUtil.isValidFileSize(multipartFile)) {
			return ConstantUtil.MESSAGE_VALIDATION_UPLOAD_FILE_SIZE_1MB_FAIL;
		}

		// validation photo
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			userFormAddDTO.setPhoto(fileName);
		} else if ("".equals(userFormAddDTO.getPhoto()) || userFormAddDTO.getPhoto() == null) {
			userFormAddDTO.setPhoto(null);
		}

		// validation unique value
		User newUser = userMapper.formAddDTOToEntity(userFormAddDTO);
		String encodePassword = passwordEncoder.encode(newUser.getPassword());
		newUser.setPassword(encodePassword);

		try {
			User savedUser = userRepository.save(newUser);

			String uploadDir = ConstantUtil.PATH_USER_PHOTO_UPLOAD_DIR_DEFAULT + savedUser.getId();
			try {
				if (multipartFile != null && !multipartFile.isEmpty()) {
					FileUploadUtil.cleanDir(uploadDir);
					FileUploadUtil.saveFile(uploadDir, savedUser.getPhoto(), multipartFile);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (DataIntegrityViolationException e) { // duplicate email
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ConstantUtil.MESSAGE_VALIDATION_DUPLICATE_EMAIL_USER;
		}

		return ConstantUtil.MESSAGE_SUCCESS_INSERT_NEW_USER;
	}

	public UserFormEditDTO findById(String userId) throws UserNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(userId);
		} catch (NumberFormatException e) {
			throw new UserNotFoundException("Could not find any user with ID " + userId);
		}

		try {
			User userInDB = userRepository.findById(id).get();
			UserFormEditDTO userFormEditDTO = userMapper.toFormEditDTO(userInDB);

			return userFormEditDTO;
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with ID " + userId);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String editUser(@Valid UserFormEditDTO userFormEditDTO, BindingResult bindingResult,
			MultipartFile multipartFile) {
		// validation binding result form
		if (bindingResult.hasErrors()) {
			return ConstantUtil.MESSAGE_VALIDATION_BINDING_RESULT_FAIL;
		}

		// validation file size
		if (!FileUploadUtil.isValidFileSize(multipartFile)) {
			return ConstantUtil.MESSAGE_VALIDATION_UPLOAD_FILE_SIZE_1MB_FAIL;
		}

		// validation photo
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			userFormEditDTO.setPhoto(fileName);
		} else if ("".equals(userFormEditDTO.getPhoto()) || userFormEditDTO.getPhoto() == null) {
			userFormEditDTO.setPhoto(null);
		}

		User existUserInDB = userRepository.findById(userFormEditDTO.getId()).get();

		// validation password
		if (userFormEditDTO.getPassword() == null || userFormEditDTO.getPassword().isBlank()) { // không thay đổi mật
																								// khẩu
			userFormEditDTO.setPassword(existUserInDB.getPassword());
		} else {
			if (!userFormEditDTO.getPassword().matches(ConstantUtil.REGEX_PASSWORD_20)) {
				bindingResult.rejectValue("password", "userFormEditDTO.password",
						ConstantUtil.MESSAGE_VALIDATION_PASSWORD_20_FAIL);
				return ConstantUtil.MESSAGE_VALIDATION_BINDING_RESULT_FAIL;
			}

			String encodePassword = passwordEncoder.encode(userFormEditDTO.getPassword());
			userFormEditDTO.setPassword(encodePassword);
		}

		// validation unique value
		User editUser = userMapper.formEditDTOToEntity(userFormEditDTO);
		try {
			User savedUser = userRepository.save(editUser);

			String uploadDir = ConstantUtil.PATH_USER_PHOTO_UPLOAD_DIR_DEFAULT + savedUser.getId();
			try {
				if (multipartFile != null && !multipartFile.isEmpty()) {
					FileUploadUtil.cleanDir(uploadDir);
					FileUploadUtil.saveFile(uploadDir, savedUser.getPhoto(), multipartFile);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			userRepository.flush();
		} catch (DataIntegrityViolationException e) { // duplicate email
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ConstantUtil.MESSAGE_VALIDATION_DUPLICATE_EMAIL_USER;
		}

		return ConstantUtil.MESSAGE_SUCCESS_EDIT_USER;
	}

	@Transactional
	public void updateUserEnabledStatus(String userId, boolean enabled) throws UserNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(userId);
			Optional<User> userInDB = userRepository.findById(id);

			if (userInDB.isPresent()) {
				User user = userInDB.get();

				user.setEnabled(enabled);
				userRepository.save(user);
			} else {
				throw new UserNotFoundException("Could not find any user with ID " + userId);
			}
		} catch (NumberFormatException e) {
			throw new UserNotFoundException("Could not find any user with ID " + userId);
		}
	}

	/**
	 * @return Object[0]: List(UserDTO)<br>
	 *         Object[1]: int totalPages<br>
	 *         Object[2]: long totalElements
	 */
	public Object[] listByPage(String strPageNum, String keyword, String sortField, String sortDir) {
		List<UserDTO> listUserDTOs = new ArrayList<>();

		int pageNum = 1;

		try {
			pageNum = Integer.parseInt(strPageNum);
		} catch (NumberFormatException e) {
			pageNum = 1;
		}

		Sort sort = Sort.by(sortField);
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_SIZE_DEFAULT, sort);

		Page<User> pageUser = null;

		if (keyword == null || keyword.isBlank()) {
			pageUser = userRepository.findAll(pageable);
		} else {
			pageUser = userRepository.findAll(keyword.trim(), pageable);
		}

		int totalPages = pageUser.getTotalPages();
		long totalElements = pageUser.getTotalElements();

		List<User> listUsers = pageUser.getContent();

		listUserDTOs = userMapper.toDTOList(listUsers);

		return new Object[] { listUserDTOs, totalPages, totalElements };

	}

	public UserAccountFormEditDTO findByEmail(String email) throws UserNotFoundException {
		Optional<User> oUser = userRepository.findByEmail(email);
		if (oUser.isPresent()) {
			UserAccountFormEditDTO userAccountFormEditDTO = userMapper.toAccountFormEditDTO(oUser.get());

			return userAccountFormEditDTO;
		}

		throw new UserNotFoundException("Could not find any user with email: " + email);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String editUserAccount(@Valid UserAccountFormEditDTO userAccountFormEditDTO, BindingResult bindingResult,
			MultipartFile multipartFile) {
		// validation binding result form
		if (bindingResult.hasErrors()) {
			return ConstantUtil.MESSAGE_VALIDATION_BINDING_RESULT_FAIL;
		}

		// validation file size
		if (!FileUploadUtil.isValidFileSize(multipartFile)) {
			return ConstantUtil.MESSAGE_VALIDATION_UPLOAD_FILE_SIZE_1MB_FAIL;
		}

		// validation photo
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			userAccountFormEditDTO.setPhoto(fileName);
		} else if ("".equals(userAccountFormEditDTO.getPhoto()) || userAccountFormEditDTO.getPhoto() == null) {
			userAccountFormEditDTO.setPhoto(null);
		}

		User existUserInDB = userRepository.findById(userAccountFormEditDTO.getId()).get();

		// validation password
		// có dữ liệu ở trường new password
		if (userAccountFormEditDTO.getNewPassword() != null && !userAccountFormEditDTO.getNewPassword().isBlank()) {
			if (!userAccountFormEditDTO.getNewPassword().matches(ConstantUtil.REGEX_PASSWORD_20)) {
				bindingResult.rejectValue("newPassword", "userAccountFormEditDTO.newPassword",
						ConstantUtil.MESSAGE_VALIDATION_PASSWORD_20_FAIL);

				return ConstantUtil.MESSAGE_VALIDATION_BINDING_RESULT_FAIL;
			}

			if (!userAccountFormEditDTO.getNewPassword().equals(userAccountFormEditDTO.getConfirmPassword())) {
				bindingResult.rejectValue("confirmPassword", "userAccountFormEditDTO.confirmPassword",
						ConstantUtil.MESSAGE_VALIDATION_CONFIRM_PASSWORD_FAIL);

				return ConstantUtil.MESSAGE_VALIDATION_BINDING_RESULT_FAIL;
			}

			String encodePassword = passwordEncoder.encode(userAccountFormEditDTO.getNewPassword());
			userAccountFormEditDTO.setNewPassword(encodePassword);
		} else if (userAccountFormEditDTO.getConfirmPassword() == null
				|| userAccountFormEditDTO.getConfirmPassword().isBlank()) { // không đổi mật khẩu
			userAccountFormEditDTO.setNewPassword(existUserInDB.getPassword());
		} else {
			bindingResult.rejectValue("confirmPassword", "userAccountFormEditDTO.confirmPassword",
					ConstantUtil.MESSAGE_VALIDATION_CONFIRM_PASSWORD_FAIL);

			return ConstantUtil.MESSAGE_VALIDATION_BINDING_RESULT_FAIL;
		}

		// validation unique value
		User editUser = userMapper.formAccountEditDTOToEntity(userAccountFormEditDTO);
		editUser.setEnabled(existUserInDB.isEnabled());
		editUser.setSetRoles(existUserInDB.getSetRoles());

		try {
			User savedUser = userRepository.save(editUser);

			String uploadDir = ConstantUtil.PATH_USER_PHOTO_UPLOAD_DIR_DEFAULT + savedUser.getId();
			try {
				if (multipartFile != null && !multipartFile.isEmpty()) {
					FileUploadUtil.cleanDir(uploadDir);
					FileUploadUtil.saveFile(uploadDir, savedUser.getPhoto(), multipartFile);
					userAccountFormEditDTO.setPhoto(savedUser.getPhoto());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			userRepository.flush();
		} catch (DataIntegrityViolationException e) { // duplicate email
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return ConstantUtil.MESSAGE_VALIDATION_DUPLICATE_EMAIL_USER;
		}

		return ConstantUtil.MESSAGE_SUCCESS_EDIT_USER_ACCOUNT;

	}

	public Set<RoleDTO> getSetRolesByEmail(String email) {
		Set<RoleDTO> setRolesDTO = new HashSet<>();
		Optional<User> oUser = userRepository.findByEmail(email);

		if (oUser.isPresent()) {
			Set<Role> setRoles = oUser.get().getSetRoles();
			setRolesDTO = roleMapper.toSetDTO(setRoles);
		}

		return setRolesDTO;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public List<User> findAllByRoleMemberLab() {
		return userRepository.findAllByRoleMemberLab();
	}

	public List<User> findAllMemberLabWithoutProfile() {
		return userRepository.findAllMemberLabWithoutProfile();
	}

}
