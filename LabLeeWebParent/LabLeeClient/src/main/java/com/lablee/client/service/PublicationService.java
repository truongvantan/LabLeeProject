package com.lablee.client.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lablee.client.repository.PublicationRepository;
import com.lablee.common.entity.Publication;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublicationService {
	private final PublicationRepository publicationRepository;
	private static final int PAGE_SIZE = 3;
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
		sort = "Ascending".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

		Page<Publication> pagePublication = null;

		if (keyword == null || keyword.isBlank()) {
			pagePublication = publicationRepository.findAllEnabled(pageable);
		} else {
			pagePublication = publicationRepository.findAllEnabled(keyword.trim(), pageable);
		}

		int totalPages = pagePublication.getTotalPages();
		long totalElements = pagePublication.getTotalElements();

		listPublications = pagePublication.getContent();

		return new Object[] { listPublications, totalPages, totalElements };
	}
	public long getTotalPublications() {
		return publicationRepository.count();
	}
	public long getTotalPublicationsEnabled() {
		return publicationRepository.getTotalPublicationsEnabled();
	}
	public List<Publication> getList3LatestPublications() {
		return publicationRepository.findFirst3ByOrderByPublishYearDesc();
	}
}
