package com.lablee.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lablee.client.exception.NewsNotFoundException;
import com.lablee.client.repository.NewsRepository;
import com.lablee.common.entity.News;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {
	private final NewsRepository newsRepository;
	private static final int PAGE_SIZE = 10;
	
	public List<News> getList4LatestNews() {
		return newsRepository.findFirst4ByOrderByUpdateAtDesc();
	}

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
		sort = "Ascending".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

		Page<News> pageNews = null;

		if (keyword == null || keyword.isBlank()) {
			pageNews = newsRepository.findAllEnabled(pageable);
		} else {
			pageNews = newsRepository.findAllEnabled(keyword.trim(), pageable);
		}

		int totalPages = pageNews.getTotalPages();
		long totalElements = pageNews.getTotalElements();

		listNews = pageNews.getContent();

		return new Object[] { listNews, totalPages, totalElements };
	}

	public News findByIdEnabled(String newsId) throws NewsNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(newsId);
		} catch (NumberFormatException e) {
			throw new NewsNotFoundException("Could not find news with ID: " + newsId);
		}

		News news = new News();
		Optional<News> oNews = newsRepository.findByIdEnabled(id);

		if (oNews.isPresent()) {
			news = oNews.get();
			return news;
		} else {
			throw new NewsNotFoundException("Could not find news with ID: " + newsId);
		}
	}
}
