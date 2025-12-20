package com.lablee.client.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.lablee.client.common.PaginationCommon;
import com.lablee.client.exception.NewsNotFoundException;
import com.lablee.client.service.NewsService;
import com.lablee.common.entity.News;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NewsController {
	private final NewsService newsService;
	private final PaginationCommon paginationCommon;
	
	@GetMapping("/news")
	public String listFirstPage(Model model) {
		model.addAttribute("activeLink", "/news");
		return listByPage(model, "1", "updateAt", "Descending", null);
	}

	@GetMapping("/news/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) String strPageNum,
			@RequestParam(name = "sortField", defaultValue = "updateAt") String sortField,
			@RequestParam(name = "sortDir", defaultValue = "Descending") String sortDir,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		model.addAttribute("activeLink", "/news");
		
		Object[] arrReturned = newsService.listByPage(strPageNum, keyword, sortField, sortDir);

		List<News> listNews = (List<News>) arrReturned[0];
		int totalPageNumber = (int) arrReturned[1];
		long totalElements = (long) arrReturned[2];

		int currentPageNumber = 1;

		try {
			currentPageNumber = Integer.parseInt(strPageNum);
		} catch (NumberFormatException e) {
			currentPageNumber = 1;
		}

		List<Integer> pageNumbers = paginationCommon.getListPageNumbers(totalPageNumber, currentPageNumber);
		List<String> listSortDir = List.of("Descending", "Ascending");

		model.addAttribute("listSortDir", listSortDir);
		model.addAttribute("currentPageNumber", currentPageNumber);
		model.addAttribute("totalPageNumber", totalPageNumber);
		model.addAttribute("totalItems", totalElements);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listNews", listNews);

		return "news/list_news";
	}
	
	@GetMapping("/news/details/{newsId}")
	public String showMemberProfileDetail(Model model, @PathVariable(name = "newsId", required = false) String newsId) {
		model.addAttribute("activeLink", "/news");
		
		try {
			News news = newsService.findByIdEnabled(newsId);
			model.addAttribute("news", news);
			
			return "news/news_detail";
		} catch (NewsNotFoundException e) {
			return "error/404";
		}
		
	}
}
