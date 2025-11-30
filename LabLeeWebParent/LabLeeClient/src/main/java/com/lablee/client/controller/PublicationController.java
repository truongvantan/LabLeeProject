package com.lablee.client.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.lablee.client.common.PaginationCommon;
import com.lablee.client.service.PublicationService;
import com.lablee.common.entity.Publication;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PublicationController {
	private final PublicationService publicationService;
	private final PaginationCommon paginationCommon;
	
	@GetMapping("/publications")
	public String listFirstPage(Model model) {
		return listByPage(model, "1", "id", "asc", null);
	}
	
	@GetMapping("/publications/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) String strPageNum,
			@RequestParam(name = "sortField", defaultValue = "id") String sortField,
			@RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		sortField  = "id";
		sortDir = "asc";

		Object[] arrReturned = publicationService.listByPage(strPageNum, keyword, sortField, sortDir);

		List<Publication> listPublications = (List<Publication>) arrReturned[0];
		int totalPageNumber = (int) arrReturned[1];
		long totalElements = (long) arrReturned[2];

		int currentPageNumber = 1;

		try {
			currentPageNumber = Integer.parseInt(strPageNum);
		} catch (NumberFormatException e) {
			currentPageNumber = 1;
		}

		List<Integer> pageNumbers = paginationCommon.getListPageNumbers(totalPageNumber, currentPageNumber);

		model.addAttribute("currentPageNumber", currentPageNumber);
		model.addAttribute("totalPageNumber", totalPageNumber);
		model.addAttribute("totalItems", totalElements);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listPublications", listPublications);

		return "publication/list_publication";
	}
	
}
