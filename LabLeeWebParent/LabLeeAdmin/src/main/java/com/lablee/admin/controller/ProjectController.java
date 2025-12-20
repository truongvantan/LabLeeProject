package com.lablee.admin.controller;

import java.util.List;

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
import com.lablee.admin.dto.ProjectFormAddDTO;
import com.lablee.admin.dto.ProjectFormEditDTO;
import com.lablee.admin.exception.ProjectNotFoundException;
import com.lablee.admin.service.MemberLabProfileService;
import com.lablee.admin.service.ProjectService;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.MemberLabProfile;
import com.lablee.common.entity.Project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProjectController {
	private final ProjectService projectService;
	private final MemberLabProfileService memberLabProfileService;
	private final PaginationCommon paginationCommon;

	@GetMapping("/projects")
	public String listFirstPage(Model model) {
		model.addAttribute("activeLink", "/projects");
		return listByPage(model, "1", "id", "asc", null);
	}

	@GetMapping("/projects/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) String strPageNum,
			@RequestParam(name = "sortField", defaultValue = "id") String sortField,
			@RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		model.addAttribute("activeLink", "/projects");
		
		Object[] arrReturned = projectService.listByPage(strPageNum, keyword, sortField, sortDir);

		List<Project> listProjects = (List<Project>) arrReturned[0];
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
		model.addAttribute("listProjects", listProjects);

		return "project/projects";
	}

//	@GetMapping("/projects/showAdd")
//	public String showAdd(Model model) {
//		List<MemberLabProfile> listMembers = memberLabProfileService.findAllEnabled();
//		
//		Project project = new Project();
//		model.addAttribute("project", project);
//		model.addAttribute("listMembers", listMembers);
//		
//		return "project/project_form_add";
//	}

	@GetMapping("/projects/showAdd")
	public String showAdd(Model model) {
		model.addAttribute("activeLink", "/projects");
		
		List<MemberLabProfile> listMembers = memberLabProfileService.findAllEnabled();

		ProjectFormAddDTO projectFormAddDTO = new ProjectFormAddDTO();
		model.addAttribute("projectFormAddDTO", projectFormAddDTO);
		model.addAttribute("listMembers", listMembers);

		return "project/project_form_add";
	}

//	@PostMapping("/projects/add")
//	public String addNewProject(Model model,
//			@RequestParam(name = "image", required = false) MultipartFile multipartFileThumbnail,
//			@ModelAttribute(name = "project") Project project, RedirectAttributes redirectAttributes) {
//		
//		String messageReturned = projectService.addNewProject(project, multipartFileThumbnail);
//		
//		switch (messageReturned) {
//		case ConstantUtil.MESSAGE_SUCCESS_ADD_PROJECT:
//			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
//			return "redirect:/projects";
//		default:
//			model.addAttribute("errorMessage", messageReturned);
//			return "project/project_form_add";
//		}
//	}

	@PostMapping("/projects/add")
	public String addNewProject(Model model,
			@RequestParam(name = "image", required = false) MultipartFile multipartFileThumbnail,
			@Valid @ModelAttribute(name = "projectFormAddDTO") ProjectFormAddDTO projectFormAddDTO,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/projects");
		
		String messageReturned = projectService.addNewProject(projectFormAddDTO, bindingResult, multipartFileThumbnail);

		switch (messageReturned) {
		case ConstantUtil.MESSAGE_SUCCESS_ADD_PROJECT:
			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
			return "redirect:/projects";
		case ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT: {
			List<MemberLabProfile> listMembers = memberLabProfileService.findAllEnabled();
			model.addAttribute("listMembers", listMembers);
			return "project/project_form_add";
		}

		default: {
			model.addAttribute("errorMessage", messageReturned);
			List<MemberLabProfile> listMembers = memberLabProfileService.findAllEnabled();
			model.addAttribute("listMembers", listMembers);
			return "project/project_form_add";

		}
		}
	}

//	@GetMapping("/projects/showEdit/{projectId}")
//	public String showEdit(Model model, @PathVariable(name = "projectId", required = false) String projectId,
//			RedirectAttributes redirectAttributes) {
//
//		try {
//			Project project = projectService.findById(projectId);
//			List<MemberLabProfile> listMembers = memberLabProfileService.findAllEnabled();
//			model.addAttribute("project", project);
//			model.addAttribute("listMembers", listMembers);
//
//			return "project/project_form_edit";
//		} catch (ProjectNotFoundException e) {
//			redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy dự án với id: " + projectId);
//			return "redirect:/projects";
//		}
//	}

	@GetMapping("/projects/showEdit/{projectId}")
	public String showEdit(Model model, @PathVariable(name = "projectId", required = false) String projectId,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/projects");
		
		try {
			ProjectFormEditDTO projectFormEditDTO = projectService.findById(projectId);
			List<MemberLabProfile> listMembers = memberLabProfileService.findAllEnabled();

			model.addAttribute("projectFormEditDTO", projectFormEditDTO);
			model.addAttribute("listMembers", listMembers);

			return "project/project_form_edit";
		} catch (ProjectNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy dự án với id: " + projectId);
			return "redirect:/projects";
		}
	}

//	@PostMapping("/projects/edit")
//	public String editPublication(Model model,
//			@RequestParam(name = "image", required = false) MultipartFile multipartFileThumbnail,
//			@ModelAttribute(name = "project") Project project, RedirectAttributes redirectAttributes) {
//
//		String messageReturned = projectService.editProject(project, multipartFileThumbnail);
//
//		switch (messageReturned) {
//		case ConstantUtil.MESSAGE_SUCCESS_EDIT_PROJECT:
//			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
//			return "redirect:/projects";
//		default:
//			model.addAttribute("errorMessage", messageReturned);
//			List<MemberLabProfile> listMembers = memberLabProfileService.findAllEnabled();
//			model.addAttribute("listMembers", listMembers);
//			return "project/project_form_edit";
//		}
//	}

	@PostMapping("/projects/edit")
	public String editPublication(Model model,
			@RequestParam(name = "image", required = false) MultipartFile multipartFileThumbnail,
			@Valid @ModelAttribute(name = "projectFormEditDTO") ProjectFormEditDTO projectFormEditDTO,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/projects");
		
		String messageReturned = projectService.editProject(projectFormEditDTO, bindingResult, multipartFileThumbnail);

		switch (messageReturned) {
		case ConstantUtil.MESSAGE_SUCCESS_EDIT_PROJECT:
			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
			return "redirect:/projects";
		case ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT: {
			List<MemberLabProfile> listMembers = memberLabProfileService.findAllEnabled();
			model.addAttribute("listMembers", listMembers);
			return "project/project_form_edit";
		}
		default:
			model.addAttribute("errorMessage", messageReturned);
			List<MemberLabProfile> listMembers = memberLabProfileService.findAllEnabled();
			model.addAttribute("listMembers", listMembers);
			return "project/project_form_edit";
		}
	}
	
	@GetMapping("/projects/{projectId}/enabled/{status}")
	public String editProjectEnabledStatus(Model model,
			@PathVariable(name = "projectId", required = false) String projectId,
			@PathVariable(name = "status", required = false) boolean enabled, RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/projects");
		
		try {
			projectService.editProjectEnabledStatus(projectId, enabled);
			String status = enabled ? "mở khóa" : "khóa";
			String message = new StringBuffer("").append("Đã ").append(status).append(" dự án ID ")
					.append(projectId).toString();
			redirectAttributes.addFlashAttribute("successMessage", message);
		} catch (ProjectNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy dự án có ID " + projectId);
			return "redirect:/projects";
		}

		return "redirect:/projects";
	}
}
