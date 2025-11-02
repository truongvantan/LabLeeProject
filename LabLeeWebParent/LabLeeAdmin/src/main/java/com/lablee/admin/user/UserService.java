package com.lablee.admin.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

import com.lablee.admin.exception.UserNotFoundException;
import com.lablee.admin.util.FileUploadUtil;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
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
		if (!multipartFile.isEmpty()) {
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

			String uploadDir = ConstantUtil.PATH_USER_PHOTO_UPLOAD_DEFAULT + "/" + savedUser.getId();
			try {
				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, savedUser.getPhoto(), multipartFile);
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
		if (!multipartFile.isEmpty()) {
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

			String uploadDir = ConstantUtil.PATH_USER_PHOTO_UPLOAD_DEFAULT + "/" + savedUser.getId();
			try {
				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, savedUser.getPhoto(), multipartFile);
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
	public void updateUserStatus(String userId, boolean booleanStatus, String statusType) throws UserNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(userId);
			Optional<User> userInDB = userRepository.findById(id);

			if (userInDB.isPresent()) {
				User user = userInDB.get();

				switch (statusType) {
				case "enabled":
					user.setEnabled(booleanStatus);
					break;
				case "deleted":
					user.setDeleted(booleanStatus);
					break;
				default:
					break;
				}
				userRepository.save(user);
			} else {
				throw new UserNotFoundException("Could not find any user with ID " + userId);
			}
		} catch (NumberFormatException e) {
			throw new UserNotFoundException("Could not find any user with ID " + userId);
		}
	}
	
	/**
	 * @return
	 * Object[0]: List(UserDTO)<br>
	 * Object[1]: int totalPages<br>
	 * Object[2]: long totalElements
	 * */
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

		Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.USER_PAGE_SIZE, sort);
		
		Page<User> pageUser = null;
		
		if (keyword == null || keyword.isBlank()) {
			pageUser = userRepository.findAll(pageable);
		} else {
			pageUser =  userRepository.findAll(keyword, pageable);
		}
		
		int totalPages = pageUser.getTotalPages();
		long totalElements = pageUser.getTotalElements();
		
		List<User> listUsers = pageUser.getContent();
		
		listUserDTOs = userMapper.toDTOList(listUsers);
		
		return new Object[] {listUserDTOs, totalPages, totalElements};

	}
}
