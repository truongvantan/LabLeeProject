package com.lablee.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lablee.admin.common.PaginationCommon;
import com.lablee.admin.exception.PublicationNotFoundException;
import com.lablee.admin.service.PublicationService;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.MemberLabProfile;
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

		Object[] arrReturned = publicationService.listByPage(strPageNum, keyword, sortField, sortDir);

		List<Publication> listPublications = (List<Publication>) arrReturned[0];
		int totalPageNumber = (int) arrReturned[1];
		long totalElements = (long) arrReturned[2];

		String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

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
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listPublications", listPublications);

		return "publication/publications";
	}

	@GetMapping("publications/showAdd")
	public String showAdd(Model model) {

		Publication publication = new Publication();

		model.addAttribute("publication", publication);

		return "publication/publication_form_add";
	}

	@PostMapping("/publications/add")
	public String addNewPublication(Model model,
			@RequestParam(name = "image", required = false) MultipartFile multipartFileThumbnail,
			@ModelAttribute(name = "publication") Publication publication, RedirectAttributes redirectAttributes) {

		String messageReturned = publicationService.addNewPublication(publication, multipartFileThumbnail);

		switch (messageReturned) {
		case ConstantUtil.MESSAGE_SUCCESS_ADD_PUBLICATION:
			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
			return "redirect:/publications";
		default:
			model.addAttribute("errorMessage", messageReturned);
			return "publication/publication_form_add";
		}

	}

	@GetMapping("/publications/showEdit/{publicationId}")
	public String showEdit(Model model, @PathVariable(name = "publicationId", required = false) String publicationId,
			RedirectAttributes redirectAttributes) {
		
		try {
			Publication publication = publicationService.findById(publicationId);
			model.addAttribute("publication", publication);
			return "publication/publication_form_edit";
		} catch (PublicationNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bài báo với id: " + publicationId);
			return "redirect:/publications";
		}
	}
	
	@PostMapping("/publications/edit")
	public String editPublication(Model model,
			@RequestParam(name = "image", required = false) MultipartFile multipartFileThumbnail,
			@ModelAttribute(name = "publication") Publication publication, RedirectAttributes redirectAttributes) {
		
		String messageReturned = publicationService.editPublication(publication, multipartFileThumbnail);

		switch (messageReturned) {
		case ConstantUtil.MESSAGE_SUCCESS_EDIT_PUBLICATION:
			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
			return "redirect:/publications";
		default:
			model.addAttribute("errorMessage", messageReturned);
			return "publication/publication_form_edit";
		}
	}
}
