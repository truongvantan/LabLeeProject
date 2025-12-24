package com.lablee.admin.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.lablee.admin.dto.ProjectFormAddDTO;
import com.lablee.admin.dto.ProjectFormEditDTO;
import com.lablee.admin.exception.ProjectNotFoundException;
import com.lablee.admin.mapper.ProjectMapper;
import com.lablee.admin.repository.ProjectRepository;
import com.lablee.admin.util.FileUploadUtil;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.Project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);
	
	private final ProjectRepository projectRepository;
	private final ProjectMapper projectMapper;

	/**
	 * @return Object[0]: List(Project)<br>
	 *         Object[1]: int totalPages<br>
	 *         Object[2]: long totalElements
	 */
	public Object[] listByPage(String strPageNum, String keyword, String sortField, String sortDir) {
		List<Project> listProjects = new ArrayList<>();

		int pageNum = 1;

		try {
			pageNum = Integer.parseInt(strPageNum);
		} catch (NumberFormatException e) {
			pageNum = 1;
		}

		Sort sort = Sort.by(sortField);
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_SIZE_DEFAULT, sort);

		Page<Project> pageProject = null;

		if (keyword == null || keyword.isBlank()) {
			pageProject = projectRepository.findAll(pageable);
		} else {
			pageProject = projectRepository.findAll(keyword.trim(), pageable);
		}

		int totalPages = pageProject.getTotalPages();
		long totalElements = pageProject.getTotalElements();

		listProjects = pageProject.getContent();

		return new Object[] { listProjects, totalPages, totalElements };
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String addNewProject(@Valid ProjectFormAddDTO projectFormAddDTO, BindingResult bindingResult,
			MultipartFile multipartFileThumbnail) {
		// validation binding result form
		if (bindingResult.hasErrors()) {
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		// validation file size
		if (!FileUploadUtil.isValidFileSize(multipartFileThumbnail)) {
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_UPLOAD_FILE_SIZE_1MB;
		}

		// validation thumbnail
		if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFileThumbnail.getOriginalFilename());
			projectFormAddDTO.setThumbnail(fileName);
		} else if ("".equals(projectFormAddDTO.getThumbnail()) || projectFormAddDTO.getThumbnail() == null) {
			projectFormAddDTO.setThumbnail(null);
		}

		if (projectFormAddDTO.getStartDate() == null) {
			bindingResult.rejectValue("startDate", "projectFormAddDTO.startDate",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_DATE);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		// Validation endDate must >= startDate
		if (projectFormAddDTO.getEndDate() != null && projectFormAddDTO.getStartDate() != null
				&& projectFormAddDTO.getEndDate().isBefore(projectFormAddDTO.getStartDate())) {

			bindingResult.rejectValue("endDate", "projectFormAddDTO.endDate",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_END_DATE_PROJECT);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		Project project = projectMapper.formAddDTOToEntity(projectFormAddDTO);

		try {
			Project savedProject = projectRepository.save(project);
			String uploadDir = ConstantUtil.PATH_PROJECT_THUMBNAIL_UPLOAD_DIR_DEFAULT + savedProject.getId();

			try {
				if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
					FileUploadUtil.cleanDir(uploadDir);
					FileUploadUtil.saveFile(uploadDir, savedProject.getThumbnail(), multipartFileThumbnail);
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			projectRepository.flush();
		} catch (DataIntegrityViolationException e) {
			LOGGER.error(e.getMessage(), e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			bindingResult.rejectValue("title", "projectFormAddDTO.title",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_DUPLICATE_TITLE_PROJECT);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		return ConstantUtil.MESSAGE_SUCCESS_ADD_PROJECT;

	}

	public ProjectFormEditDTO findById(String projectId) throws ProjectNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(projectId);
		} catch (NumberFormatException e) {
			throw new ProjectNotFoundException("Could not find any project with ID " + projectId);
		}

		Optional<Project> oProject = projectRepository.findById(id);
		if (oProject.isPresent()) {
			Project project = oProject.get();
			ProjectFormEditDTO projectFormEditDTO = projectMapper.entityToFormEditDTO(project);

			return projectFormEditDTO;
		} else {
			throw new ProjectNotFoundException("Could not find any project with ID " + projectId);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String editProject(@Valid ProjectFormEditDTO projectFormEditDTO, BindingResult bindingResult,
			MultipartFile multipartFileThumbnail) {
		// validation binding result form
		if (bindingResult.hasErrors()) {
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		// validation file size
		if (!FileUploadUtil.isValidFileSize(multipartFileThumbnail)) {
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_UPLOAD_FILE_SIZE_1MB;
		}

		// validation thumbnail
		if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFileThumbnail.getOriginalFilename());
			projectFormEditDTO.setThumbnail(fileName);
		} else if ("".equals(projectFormEditDTO.getThumbnail()) || projectFormEditDTO.getThumbnail() == null) {
			projectFormEditDTO.setThumbnail(null);
		}

		if (projectFormEditDTO.getStartDate() == null) {
			bindingResult.rejectValue("startDate", "projectFormEditDTO.startDate",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_DATE);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		// Validation endDate must >= startDate
		if (projectFormEditDTO.getEndDate() != null && projectFormEditDTO.getStartDate() != null
				&& projectFormEditDTO.getEndDate().isBefore(projectFormEditDTO.getStartDate())) {

			bindingResult.rejectValue("endDate", "projectFormEditDTO.endDate",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_END_DATE_PROJECT);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		Project project = projectMapper.formEditDTOToEntity(projectFormEditDTO);

		try {
			Project savedProject = projectRepository.save(project);
			String uploadDir = ConstantUtil.PATH_PROJECT_THUMBNAIL_UPLOAD_DIR_DEFAULT + savedProject.getId();

			try {
				if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
					FileUploadUtil.cleanDir(uploadDir);
					FileUploadUtil.saveFile(uploadDir, savedProject.getThumbnail(), multipartFileThumbnail);
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			projectRepository.flush();
		} catch (DataIntegrityViolationException e) {
			LOGGER.error(e.getMessage(), e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			bindingResult.rejectValue("title", "projectFormEditDTO.title",
					ConstantUtil.MESSAGE_FAIL_VALIDATION_DUPLICATE_TITLE_PROJECT);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		return ConstantUtil.MESSAGE_SUCCESS_EDIT_PROJECT;
	}

	@Transactional
	public void editProjectEnabledStatus(String projectId, boolean enabled) throws ProjectNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(projectId);
			Optional<Project> projectInDB = projectRepository.findById(id);

			if (projectInDB.isPresent()) {
				Project project = projectInDB.get();

				project.setEnabled(enabled);
				projectRepository.save(project);
			} else {
				throw new ProjectNotFoundException("Could not find any project with ID " + projectId);
			}
		} catch (NumberFormatException e) {
			throw new ProjectNotFoundException("Could not find any project with ID " + projectId);
		}
	}

}
