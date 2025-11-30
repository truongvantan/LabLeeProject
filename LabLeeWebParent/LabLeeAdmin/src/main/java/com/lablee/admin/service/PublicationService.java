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
import org.springframework.web.multipart.MultipartFile;

import com.lablee.admin.exception.PublicationNotFoundException;
import com.lablee.admin.repository.PublicationRepository;
import com.lablee.admin.util.FileUploadUtil;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.MemberLabProfile;
import com.lablee.common.entity.Publication;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublicationService {
	private final PublicationRepository publicationRepository;

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

	@Transactional
	public String addNewPublication(Publication publication, MultipartFile multipartFileThumbnail) {
		// validation file size
		if (!FileUploadUtil.isValidFileSize(multipartFileThumbnail)) {
			return ConstantUtil.MESSAGE_VALIDATION_UPLOAD_FILE_SIZE_1MB_FAIL;
		}

		// validation thumbnail
		if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFileThumbnail.getOriginalFilename());
			publication.setThumbnail(fileName);
		} else if ("".equals(publication.getThumbnail()) || publication.getThumbnail() == null) {
			publication.setThumbnail(null);
		}

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

	public Publication findById(String publicationId) throws PublicationNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(publicationId);
		} catch (NumberFormatException e) {
			throw new PublicationNotFoundException("Could not find any publication with ID " + publicationId);
		}

		Optional<Publication> oPublication = publicationRepository.findById(id);
		if (oPublication.isPresent()) {
			return oPublication.get();
		} else {
			throw new PublicationNotFoundException("Could not find any publication with ID " + publicationId);
		}
	}

	@Transactional
	public String editPublication(Publication publication, MultipartFile multipartFileThumbnail) {
		// validation file size
		if (!FileUploadUtil.isValidFileSize(multipartFileThumbnail)) {
			return ConstantUtil.MESSAGE_VALIDATION_UPLOAD_FILE_SIZE_1MB_FAIL;
		}

		// validation thumbnail
		if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFileThumbnail.getOriginalFilename());
			publication.setThumbnail(fileName);
		} else if ("".equals(publication.getThumbnail()) || publication.getThumbnail() == null) {
			publication.setThumbnail(null);
		}

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
}
