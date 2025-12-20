package com.lablee.admin.service;

import java.io.IOException;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.lablee.admin.dto.PublicationFormAddDTO;
import com.lablee.admin.dto.PublicationFormEditDTO;
import com.lablee.admin.exception.PublicationNotFoundException;
import com.lablee.admin.mapper.PublicationMapper;
import com.lablee.admin.repository.PublicationRepository;
import com.lablee.admin.util.FileUploadUtil;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.Publication;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublicationService {
	private final PublicationRepository publicationRepository;
	private final PublicationMapper publicationMapper;

	/**
	 * @return Object[0]: List(Publication)<br>
	 *         Object[1]: int totalPages<br>
	 *         Object[2]: long totalElements
	 */
	public Object[] listByPage(String strPageNum, String keyword, String sortField, String sortDir) {
		List<Publication> listPublications = new ArrayList<>();

		int pageNum = 1;

		try {
			pageNum = Integer.parseInt(strPageNum);
		} catch (NumberFormatException e) {
			pageNum = 1;
		}

		Sort sort = Sort.by(sortField);
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_SIZE_DEFAULT, sort);

		Page<Publication> pagePublication = null;

		if (keyword == null || keyword.isBlank()) {
			pagePublication = publicationRepository.findAll(pageable);
		} else {
			pagePublication = publicationRepository.findAll(keyword.trim(), pageable);
		}

		int totalPages = pagePublication.getTotalPages();
		long totalElements = pagePublication.getTotalElements();

		listPublications = pagePublication.getContent();

//		listUserDTOs = userMapper.toDTOList(listUsers);

		return new Object[] { listPublications, totalPages, totalElements };
	}

//	@Transactional
//	public String addNewPublication(Publication publication, MultipartFile multipartFileThumbnail) {
//		// validation file size
//		if (!FileUploadUtil.isValidFileSize(multipartFileThumbnail)) {
//			return ConstantUtil.MESSAGE_FAIL_VALIDATION_UPLOAD_FILE_SIZE_1MB;
//		}
//
//		// validation thumbnail
//		if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
//			String fileName = StringUtils.cleanPath(multipartFileThumbnail.getOriginalFilename());
//			publication.setThumbnail(fileName);
//		} else if ("".equals(publication.getThumbnail()) || publication.getThumbnail() == null) {
//			publication.setThumbnail(null);
//		}
//
//		Publication savedPublication = publicationRepository.save(publication);
//		String uploadDir = ConstantUtil.PATH_PUBLICATION_THUMBNAIL_UPLOAD_DIR_DEFAULT + savedPublication.getId();
//
//		try {
//			if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
//				FileUploadUtil.cleanDir(uploadDir);
//				FileUploadUtil.saveFile(uploadDir, savedPublication.getThumbnail(), multipartFileThumbnail);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			return ConstantUtil.MESSAGE_FAIL_ADD_PUBLICATION;
//		}
//
//		return ConstantUtil.MESSAGE_SUCCESS_ADD_PUBLICATION;
//	}

	@Transactional
	public String addNewPublication(@Valid PublicationFormAddDTO publicationFormAddDTO, BindingResult bindingResult,
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
			publicationFormAddDTO.setThumbnail(fileName);
		} else if ("".equals(publicationFormAddDTO.getThumbnail()) || publicationFormAddDTO.getThumbnail() == null) {
			publicationFormAddDTO.setThumbnail(null);
		}
		Publication publication = publicationMapper.formAddDTOToEntity(publicationFormAddDTO);

		Publication savedPublication = publicationRepository.save(publication);
		String uploadDir = ConstantUtil.PATH_PUBLICATION_THUMBNAIL_UPLOAD_DIR_DEFAULT + savedPublication.getId();

		try {
			if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, savedPublication.getThumbnail(), multipartFileThumbnail);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ConstantUtil.MESSAGE_FAIL_ADD_PUBLICATION;
		}

		return ConstantUtil.MESSAGE_SUCCESS_ADD_PUBLICATION;

	}

//	public Publication findById(String publicationId) throws PublicationNotFoundException {
//		int id = -1;
//
//		try {
//			id = Integer.parseInt(publicationId);
//		} catch (NumberFormatException e) {
//			throw new PublicationNotFoundException("Could not find any publication with ID " + publicationId);
//		}
//
//		Optional<Publication> oPublication = publicationRepository.findById(id);
//		if (oPublication.isPresent()) {
//			return oPublication.get();
//		} else {
//			throw new PublicationNotFoundException("Could not find any publication with ID " + publicationId);
//		}
//	}

	public PublicationFormEditDTO findById(String publicationId) throws PublicationNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(publicationId);
		} catch (NumberFormatException e) {
			throw new PublicationNotFoundException("Could not find any publication with ID " + publicationId);
		}

		Optional<Publication> oPublication = publicationRepository.findById(id);
		if (oPublication.isPresent()) {
			Publication publication = oPublication.get();
			PublicationFormEditDTO publicationFormEditDTO = publicationMapper.entityToFormEditDTO(publication);

			return publicationFormEditDTO;
		} else {
			throw new PublicationNotFoundException("Could not find any publication with ID " + publicationId);
		}
	}

//	@Transactional
//	public String editPublication(Publication publication, MultipartFile multipartFileThumbnail) {
//		// validation file size
//		if (!FileUploadUtil.isValidFileSize(multipartFileThumbnail)) {
//			return ConstantUtil.MESSAGE_FAIL_VALIDATION_UPLOAD_FILE_SIZE_1MB;
//		}
//
//		// validation thumbnail
//		if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
//			String fileName = StringUtils.cleanPath(multipartFileThumbnail.getOriginalFilename());
//			publication.setThumbnail(fileName);
//		} else if ("".equals(publication.getThumbnail()) || publication.getThumbnail() == null) {
//			publication.setThumbnail(null);
//		}
//
//		Publication savedPublication = publicationRepository.save(publication);
//		String uploadDir = ConstantUtil.PATH_PUBLICATION_THUMBNAIL_UPLOAD_DIR_DEFAULT + savedPublication.getId();
//
//		try {
//			if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
//				FileUploadUtil.cleanDir(uploadDir);
//				FileUploadUtil.saveFile(uploadDir, savedPublication.getThumbnail(), multipartFileThumbnail);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			return ConstantUtil.MESSAGE_FAIL_EDIT_PUBLICATION;
//		}
//
//		return ConstantUtil.MESSAGE_SUCCESS_EDIT_PUBLICATION;
//	}

	@Transactional
	public String editPublication(@Valid PublicationFormEditDTO publicationFormEditDTO, BindingResult bindingResult,
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
			publicationFormEditDTO.setThumbnail(fileName);
		} else if ("".equals(publicationFormEditDTO.getThumbnail()) || publicationFormEditDTO.getThumbnail() == null) {
			publicationFormEditDTO.setThumbnail(null);
		}
		Publication publication = publicationMapper.formEditDTOToEntity(publicationFormEditDTO);

		Publication savedPublication = publicationRepository.save(publication);
		String uploadDir = ConstantUtil.PATH_PUBLICATION_THUMBNAIL_UPLOAD_DIR_DEFAULT + savedPublication.getId();

		try {
			if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, savedPublication.getThumbnail(), multipartFileThumbnail);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ConstantUtil.MESSAGE_FAIL_EDIT_PUBLICATION;
		}

		return ConstantUtil.MESSAGE_SUCCESS_EDIT_PUBLICATION;
	}

	@Transactional
	public void editPublicationEnabledStatus(String publicationId, boolean enabled)
			throws PublicationNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(publicationId);
			Optional<Publication> publicationInDB = publicationRepository.findById(id);

			if (publicationInDB.isPresent()) {
				Publication publication = publicationInDB.get();

				publication.setEnabled(enabled);
				publicationRepository.save(publication);
			} else {
				throw new PublicationNotFoundException("Could not find any publication with ID " + publicationId);
			}
		} catch (NumberFormatException e) {
			throw new PublicationNotFoundException("Could not find any publication with ID " + publicationId);
		}
	}

}
