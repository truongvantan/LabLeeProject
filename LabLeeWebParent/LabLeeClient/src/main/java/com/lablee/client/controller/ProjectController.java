package com.lablee.client.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.lablee.client.common.PaginationCommon;
import com.lablee.client.exception.ProjectNotFoundException;
import com.lablee.client.service.ProjectService;
import com.lablee.common.entity.Project;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProjectController {
	private final ProjectService projectService;
	private final PaginationCommon paginationCommon;

	@GetMapping("/projects")
	public String listFirstPage(Model model) {
		model.addAttribute("activeLink", "/projects");
		return listByPage(model, "1", "endDate", "Descending", null);
	}

	@GetMapping("/projects/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) String strPageNum,
			@RequestParam(name = "sortField", defaultValue = "endDate") String sortField,
			@RequestParam(name = "sortDir", defaultValue = "Descending") String sortDir,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		model.addAttribute("activeLink", "/projects");
		
		Object[] arrReturned = projectService.listByPage(strPageNum, keyword, sortField, sortDir);

		List<Project> listProjects = (List<Project>) arrReturned[0];
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
		model.addAttribute("listProjects", listProjects);

		return "project/list_project";
	}
	
	@GetMapping("/projects/details/{projectId}")
	public String showMemberProfileDetail(Model model, @PathVariable(name = "projectId", required = false) String projectId) {
		model.addAttribute("activeLink", "/projects");
		
		try {
			Project project = projectService.findByIdEnabled(projectId);
			model.addAttribute("project", project);
			
			return "project/project_detail";
		} catch (ProjectNotFoundException e) {
			return "error/404";
		}
		
	}
}
