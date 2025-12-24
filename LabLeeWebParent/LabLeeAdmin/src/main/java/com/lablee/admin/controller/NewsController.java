package com.lablee.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lablee.admin.common.PaginationCommon;
import com.lablee.admin.config.LabLeeUserDetails;
import com.lablee.admin.dto.NewsFormAddDTO;
import com.lablee.admin.dto.NewsFormEditDTO;
import com.lablee.admin.exception.NewsNotFoundException;
import com.lablee.admin.service.NewsService;
import com.lablee.admin.service.UserService;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.News;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NewsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);
	
	private final NewsService newsService;
	private final PaginationCommon paginationCommon;
	private final UserService userService;

	@GetMapping("/news")
	public String listFirstPage(Model model) {
		model.addAttribute("activeLink", "/news");
		return listByPage(model, "1", "createAt", "desc", null);
	}

	@GetMapping("/news/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) String strPageNum,
			@RequestParam(name = "sortField", defaultValue = "createAt") String sortField,
			@RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		model.addAttribute("activeLink", "/news");
		
		Object[] arrReturned = newsService.listByPage(strPageNum, keyword, sortField, sortDir);

		List<News> listNews = (List<News>) arrReturned[0];
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
		model.addAttribute("listNews", listNews);

		return "news/news";
	}

	@GetMapping("/news/showAdd")
	public String showAdd(Model model, @AuthenticationPrincipal LabLeeUserDetails loggedUser) {
		model.addAttribute("activeLink", "/news");
		
		NewsFormAddDTO newsFormAddDTO = new NewsFormAddDTO();
		model.addAttribute("newsFormAddDTO", newsFormAddDTO);

		return "news/news_form_add";
	}

	@PostMapping("/news/add")
	public String addNewNews(Model model,
			@RequestParam(name = "image", required = false) MultipartFile multipartFileThumbnail,
			@Valid @ModelAttribute(name = "newsFormAddDTO") NewsFormAddDTO newsFormAddDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @AuthenticationPrincipal LabLeeUserDetails loggedUser) {
		model.addAttribute("activeLink", "/news");
		
		String messageReturned = newsService.addNewNews(newsFormAddDTO, bindingResult, multipartFileThumbnail,
				loggedUser);

		switch (messageReturned) {
		case ConstantUtil.MESSAGE_SUCCESS_ADD_NEWS:
			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
			return "redirect:/news";
		case ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT:
			return "news/news_form_add";
		default:
			model.addAttribute("errorMessage", messageReturned);
			return "news/news_form_add";

		}
	}

	@GetMapping("/news/showEdit/{newsId}")
	public String showEdit(Model model, @PathVariable(name = "newsId", required = false) String newsId,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/news");
		
		try {
			NewsFormEditDTO newsFormEditDTO = newsService.findById(newsId);

			model.addAttribute("newsFormEditDTO", newsFormEditDTO);

			return "news/news_form_edit";
		} catch (NewsNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/news";
		}
	}

	@PostMapping("/news/edit")
	public String editNews(Model model,
			@RequestParam(name = "image", required = false) MultipartFile multipartFileThumbnail,
			@Valid @ModelAttribute(name = "newsFormEditDTO") NewsFormEditDTO newsFormEditDTO,
			BindingResult bindingResult, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal LabLeeUserDetails loggedUser) {
		model.addAttribute("activeLink", "/news");
		
		String messageReturned = newsService.editNews(newsFormEditDTO, bindingResult, multipartFileThumbnail,
				loggedUser);

		switch (messageReturned) {
		case ConstantUtil.MESSAGE_SUCCESS_EDIT_NEWS:
			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
			return "redirect:/news";
		case ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT:
			return "news/news_form_edit";
		default:
			model.addAttribute("errorMessage", messageReturned);
			return "news/news_form_edit";

		}
	}

	@GetMapping("/news/{newsId}/enabled/{status}")
	public String editNewsEnabledStatus(Model model, @PathVariable(name = "newsId", required = false) String newsId,
			@PathVariable(name = "status", required = false) boolean enabled, RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/news");
		
		try {
			newsService.editNewsEnabledStatus(newsId, enabled);
			String status = enabled ? "mở khóa" : "khóa";
			String message = new StringBuffer("").append("Đã ").append(status).append(" bài đăng ID ").append(newsId)
					.toString();
			redirectAttributes.addFlashAttribute("successMessage", message);
		} catch (NewsNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
			
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/news";
		}

		return "redirect:/news";
	}
}
