package com.lablee.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.lablee.admin.dto.RoleDTO;
import com.lablee.admin.dto.UserDTO;
import com.lablee.admin.dto.UserFormAddDTO;
import com.lablee.admin.dto.UserFormEditDTO;
import com.lablee.admin.exception.UserNotFoundException;
import com.lablee.admin.service.RoleService;
import com.lablee.admin.service.UserService;
import com.lablee.common.constant.ConstantUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;
	private final RoleService roleService;
	private final PaginationCommon paginationCommon;

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		model.addAttribute("activeLink", "/users");
		return listByPage(model, "1", "id", "asc", "5", null);
	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) String strPageNum,
			@RequestParam(name = "sortField", defaultValue = "id") String sortField,
			@RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
			@RequestParam(name = "pageSize", defaultValue = "5") String strPageSize,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		model.addAttribute("activeLink", "/users");

		Object[] arrReturned = userService.listByPage(strPageNum, keyword, sortField, sortDir, strPageSize);

		List<UserDTO> listUserDTOs = (List<UserDTO>) arrReturned[0];
		int totalPageNumber = (int) arrReturned[1];
		long totalElements = (long) arrReturned[2];

		String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

		int currentPageNumber = 1;
		int pageSize = ConstantUtil.PAGE_SIZE_DEFAULT;

		try {
			currentPageNumber = Integer.parseInt(strPageNum);
			pageSize = Integer.parseInt(strPageSize);
		} catch (NumberFormatException e) {
			currentPageNumber = 1;
			pageSize = ConstantUtil.PAGE_SIZE_DEFAULT;
		}

		List<Integer> pageNumbers = paginationCommon.getListPageNumbers(totalPageNumber, currentPageNumber);
		List<String> listPageSize = List.of(ConstantUtil.LIST_PAGE_SIZE);

		model.addAttribute("currentPageNumber", currentPageNumber);
		model.addAttribute("totalPageNumber", totalPageNumber);
		model.addAttribute("totalItems", totalElements);
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listUserDTOs", listUserDTOs);
		model.addAttribute("listPageSize", listPageSize);
		model.addAttribute("pageSize", pageSize);

		return "users/users";
	}

	@GetMapping("users/showAdd")
	public String showAdd(Model model) {
		model.addAttribute("activeLink", "/users");

		UserFormAddDTO userFormAddDTO = new UserFormAddDTO();
		userFormAddDTO.setEnabled(true);
		List<RoleDTO> listRoleDTOs = roleService.getListRoleDTOS();

		model.addAttribute("userFormAddDTO", userFormAddDTO);
		model.addAttribute("listRoleDTOs", listRoleDTOs);

		return "users/user_form_add";
	}

	@PostMapping("/users/add")
	public String addUser(Model model, @RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@Valid @ModelAttribute UserFormAddDTO userFormAddDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/users");

		List<RoleDTO> listRoleDTOs = new ArrayList<>();
		String messageReturned = userService.saveNewUser(userFormAddDTO, bindingResult, multipartFile);

		switch (messageReturned) {
		case ConstantUtil.MESSAGE_SUCCESS_INSERT_NEW_USER:
			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
			return "redirect:/users";
		case ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT:
			listRoleDTOs = roleService.getListRoleDTOS();
			model.addAttribute("listRoleDTOs", listRoleDTOs);
			return "users/user_form_add";
		default:
			settingCommonAttributeFail(model, messageReturned);
			return "users/user_form_add";
		}

	}

	private void settingCommonAttributeFail(Model model, String messageReturned) {
		List<RoleDTO> listRoleDTOs = roleService.getListRoleDTOS();
		model.addAttribute("listRoleDTOs", listRoleDTOs);
		model.addAttribute("errorMessage", messageReturned);
	}

	@GetMapping("/users/showEdit/{userId}")
	public String showEditUser(Model model, @PathVariable(name = "userId", required = false) String userId,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/users");

		try {
			UserFormEditDTO userFormEditDTO = userService.findById(userId);
			List<RoleDTO> listRoleDTOs = roleService.getListRoleDTOS();
			model.addAttribute("listRoleDTOs", listRoleDTOs);
			model.addAttribute("userFormEditDTO", userFormEditDTO);

			return "users/user_form_edit";
		} catch (UserNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/users";
		}
	}

	@PostMapping("/users/edit")
	public String editUser(Model model, @RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@Valid @ModelAttribute UserFormEditDTO userFormEditDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/users");

		List<RoleDTO> listRoleDTOs = new ArrayList<>();
		String messageReturned = userService.editUser(userFormEditDTO, bindingResult, multipartFile);

		switch (messageReturned) {
		case ConstantUtil.MESSAGE_SUCCESS_EDIT_USER:
			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
			return "redirect:/users";
		case ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT:
			listRoleDTOs = roleService.getListRoleDTOS();
			model.addAttribute("listRoleDTOs", listRoleDTOs);
			return "users/user_form_edit";
		default:
			settingCommonAttributeFail(model, messageReturned);
			return "users/user_form_edit";
		}
	}

	@GetMapping("/users/{userId}/enabled/{status}")
	public String editUserEnabledStatus(Model model, @PathVariable(name = "userId", required = false) String userId,
			@PathVariable(name = "status", required = false) boolean enabled, RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/users");

		try {
			userService.updateUserEnabledStatus(userId, enabled);
			String status = enabled ? "Enabled" : "Disabled";
			String message = new StringBuffer("").append(status).append(" user ID ").append(userId)
					.toString();
			redirectAttributes.addFlashAttribute("successMessage", message);

		} catch (UserNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/users";
		}

		return "redirect:/users";
	}

}
