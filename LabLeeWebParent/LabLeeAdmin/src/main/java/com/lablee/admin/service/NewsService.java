package com.lablee.admin.service;

import java.io.IOException;
import java.time.LocalDateTime;
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

import com.lablee.admin.config.LabLeeUserDetails;
import com.lablee.admin.dto.NewsFormAddDTO;
import com.lablee.admin.dto.NewsFormEditDTO;
import com.lablee.admin.exception.NewsNotFoundException;
import com.lablee.admin.mapper.NewsMapper;
import com.lablee.admin.repository.NewsRepository;
import com.lablee.admin.repository.UserRepository;
import com.lablee.admin.util.FileUploadUtil;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.News;
import com.lablee.common.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsService.class);
	
	private final NewsRepository newsRepository;
	private final UserRepository userRepository;
	private final NewsMapper newsMapper;

	/**
	 * @return Object[0]: List(News)<br>
	 *         Object[1]: int totalPages<br>
	 *         Object[2]: long totalElements
	 */
	public Object[] listByPage(String strPageNum, String keyword, String sortField, String sortDir) {
		List<News> listNews = new ArrayList<>();

		int pageNum = 1;

		try {
			pageNum = Integer.parseInt(strPageNum);
		} catch (NumberFormatException e) {
			pageNum = 1;
		}

		Sort sort = Sort.by(sortField);
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_SIZE_DEFAULT, sort);

		Page<News> pageNews = null;

		if (keyword == null || keyword.isBlank()) {
			pageNews = newsRepository.findAll(pageable);
		} else {
			pageNews = newsRepository.findAll(keyword.trim(), pageable);
		}

		int totalPages = pageNews.getTotalPages();
		long totalElements = pageNews.getTotalElements();

		listNews = pageNews.getContent();

		return new Object[] { listNews, totalPages, totalElements };
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String addNewNews(@Valid NewsFormAddDTO newsFormAddDTO, BindingResult bindingResult,
			MultipartFile multipartFileThumbnail, LabLeeUserDetails loggedUser) {
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
			newsFormAddDTO.setThumbnail(fileName);
		} else if ("".equals(newsFormAddDTO.getThumbnail()) || newsFormAddDTO.getThumbnail() == null) {
			newsFormAddDTO.setThumbnail(null);
		}

		Optional<User> oUser = userRepository.findByEmail(loggedUser.getUsername());
		if (!oUser.isPresent()) {
			return ConstantUtil.MESSAGE_FAIL_INTERNAL_SERVER_ERROR;
		}

		User userInDB = oUser.get();
		newsFormAddDTO.setUser(userInDB);
		newsFormAddDTO.setCreateAt(LocalDateTime.now());
		newsFormAddDTO.setUpdateAt(LocalDateTime.now());

		News news = newsMapper.formAddDTOToEntity(newsFormAddDTO);

		try {
			News savedNews = newsRepository.save(news);
			String uploadDir = ConstantUtil.PATH_NEWS_THUMBNAIL_UPLOAD_DIR_DEFAULT + savedNews.getId();

			try {
				if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
					FileUploadUtil.cleanDir(uploadDir);
					FileUploadUtil.saveFile(uploadDir, savedNews.getThumbnail(), multipartFileThumbnail);
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			newsRepository.flush();
		} catch (DataIntegrityViolationException e) {
			LOGGER.error(e.getMessage(), e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			bindingResult.rejectValue("title", "newsFormEditDTO.title", ConstantUtil.MESSAGE_FAIL_VALIDATION_DUPLICATE_TITLE_NEWS);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		return ConstantUtil.MESSAGE_SUCCESS_ADD_NEWS;
	}

	public NewsFormEditDTO findById(String newsId) throws NewsNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(newsId);
		} catch (NumberFormatException e) {
			throw new NewsNotFoundException("Could not find any news with ID " + newsId);
		}

		Optional<News> oNews = newsRepository.findById(id);
		if (oNews.isPresent()) {
			News news = oNews.get();
			NewsFormEditDTO newsFormEditDTO = newsMapper.entityToFormEditDTO(news);

			return newsFormEditDTO;
		} else {
			throw new NewsNotFoundException("Could not find any news with ID " + newsId);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String editNews(@Valid NewsFormEditDTO newsFormEditDTO, BindingResult bindingResult,
			MultipartFile multipartFileThumbnail, LabLeeUserDetails loggedUser) {
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
			newsFormEditDTO.setThumbnail(fileName);
		} else if ("".equals(newsFormEditDTO.getThumbnail()) || newsFormEditDTO.getThumbnail() == null) {
			newsFormEditDTO.setThumbnail(null);
		}

		Optional<User> oUser = userRepository.findByEmail(loggedUser.getUsername());
		if (!oUser.isPresent()) {
			return ConstantUtil.MESSAGE_FAIL_INTERNAL_SERVER_ERROR;
		}

		User userInDB = oUser.get();
		newsFormEditDTO.setUser(userInDB);
		
		News newsInDB = newsRepository.findById(newsFormEditDTO.getId()).get();
		newsFormEditDTO.setCreateAt(newsInDB.getCreateAt());
		
		News news = newsMapper.formEditDTOToEntity(newsFormEditDTO);
		
		try {
			News savedNews = newsRepository.save(news);
			String uploadDir = ConstantUtil.PATH_NEWS_THUMBNAIL_UPLOAD_DIR_DEFAULT + savedNews.getId();

			try {
				if (multipartFileThumbnail != null && !multipartFileThumbnail.isEmpty()) {
					FileUploadUtil.cleanDir(uploadDir);
					FileUploadUtil.saveFile(uploadDir, savedNews.getThumbnail(), multipartFileThumbnail);
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			
			newsRepository.flush();
		} catch (DataIntegrityViolationException e) {
			LOGGER.error(e.getMessage(), e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			bindingResult.rejectValue("title", "newsFormEditDTO.title", ConstantUtil.MESSAGE_FAIL_VALIDATION_DUPLICATE_TITLE_NEWS);
			return ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT;
		}

		return ConstantUtil.MESSAGE_SUCCESS_EDIT_NEWS;
	}
	
	@Transactional
	public void editNewsEnabledStatus(String newsId, boolean enabled) throws NewsNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(newsId);
			Optional<News> newsInDB = newsRepository.findById(id);

			if (newsInDB.isPresent()) {
				News news = newsInDB.get();

				news.setEnabled(enabled);
				newsRepository.save(news);
			} else {
				throw new NewsNotFoundException("Could not find any news with ID " + newsId);
			}
		} catch (NumberFormatException e) {
			throw new NewsNotFoundException("Could not find any news with ID " + newsId);
		}
	}
}
