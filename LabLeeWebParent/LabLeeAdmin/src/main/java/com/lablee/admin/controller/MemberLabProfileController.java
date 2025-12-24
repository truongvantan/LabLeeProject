package com.lablee.admin.controller;

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
import com.lablee.admin.dto.MemberLabProfileFormAddDTO;
import com.lablee.admin.dto.MemberLabProfileFormEditDTO;
import com.lablee.admin.exception.MemberLabProfileNotFoundException;
import com.lablee.admin.service.MemberLabProfileService;
import com.lablee.admin.service.UserService;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.MemberLabProfile;
import com.lablee.common.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberLabProfileController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MemberLabProfileController.class);

	private final MemberLabProfileService memberLabProfileService;
	private final UserService userService;
	private final PaginationCommon paginationCommon;

	@GetMapping("/members")
	public String listFirstPage(Model model) {
		model.addAttribute("activeLink", "/members");
		return listByPage(model, "1", "id", "asc", null);
	}

	@GetMapping("/members/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) String strPageNum,
			@RequestParam(name = "sortField", defaultValue = "id") String sortField,
			@RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		model.addAttribute("activeLink", "/members");

		Object[] arrReturned = memberLabProfileService.listByPage(strPageNum, keyword, sortField, sortDir);

		List<MemberLabProfile> listMemberLabDTOs = (List<MemberLabProfile>) arrReturned[0];
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
		model.addAttribute("listMemberLabDTOs", listMemberLabDTOs);

		return "members_lab/members";
	}

	@GetMapping("members/showAdd")
	public String showAdd(Model model) {
		model.addAttribute("activeLink", "/members");

		MemberLabProfileFormAddDTO memberLabProfileFormAddDTO = new MemberLabProfileFormAddDTO();
		List<User> listUsers = userService.findAllMemberLabWithoutProfile();

		model.addAttribute("memberLabProfileFormAddDTO", memberLabProfileFormAddDTO);
		model.addAttribute("listUsers", listUsers);

		return "members_lab/member_form_add";
	}

	@PostMapping("/members/add")
	public String addNewMember(Model model, @RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@Valid @ModelAttribute(name = "memberLabProfileFormAddDTO") MemberLabProfileFormAddDTO memberLabProfileFormAddDTO,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/members");

		String messageReturned = memberLabProfileService.addNewMember(memberLabProfileFormAddDTO, bindingResult,
				multipartFile);

		switch (messageReturned) {
		case ConstantUtil.MESSAGE_SUCCESS_ADD_MEMBER_LAB_PROFILE:
			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
			return "redirect:/members";
		case ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT: {
			List<User> listUsers = userService.findAllMemberLabWithoutProfile();
			model.addAttribute("listUsers", listUsers);
			return "members_lab/member_form_add";
		}
		default: {
			List<User> listUsers = userService.findAllMemberLabWithoutProfile();
			model.addAttribute("listUsers", listUsers);
			model.addAttribute("errorMessage", messageReturned);
			return "members_lab/member_form_add";

		}
		}

	}

	@GetMapping("/members/showEdit/{memberLabProfileId}")
	public String showEdit(Model model,
			@PathVariable(name = "memberLabProfileId", required = false) String memberLabProfileId,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/members");

		MemberLabProfileFormEditDTO memberLabProfileFormEditDTO;

		try {
			memberLabProfileFormEditDTO = memberLabProfileService.findById(memberLabProfileId);
		} catch (MemberLabProfileNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

			return "redirect:/members";
		}

		model.addAttribute("memberLabProfileFormEditDTO", memberLabProfileFormEditDTO);

		return "members_lab/member_form_edit";
	}

	@PostMapping("/members/edit")
	public String editMemberProfile(Model model,
			@RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@Valid @ModelAttribute(name = "memberLabProfileFormEditDTO") MemberLabProfileFormEditDTO memberLabProfileFormEditDTO,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/members");

		String messageReturned = memberLabProfileService.editMember(memberLabProfileFormEditDTO, bindingResult,
				multipartFile);

		switch (messageReturned) {
		case ConstantUtil.MESSAGE_SUCCESS_EDIT_MEMBER_LAB_PROFILE:
			redirectAttributes.addFlashAttribute("successMessage", messageReturned);
			return "redirect:/members";
		case ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT:
			memberLabProfileFormEditDTO.setUser(userService.findById(memberLabProfileFormEditDTO.getUser().getId()));
			return "members_lab/member_form_edit";
		default:
			model.addAttribute("errorMessage", messageReturned);
			return "members_lab/member_form_edit";
		}
	}

	@GetMapping("/members/{memberLabProfileId}/enabled/{status}")
	public String editMemberLabProfileEnabledStatus(Model model,
			@PathVariable(name = "memberLabProfileId", required = false) String memberLabProfileId,
			@PathVariable(name = "status", required = false) boolean enabled, RedirectAttributes redirectAttributes) {
		model.addAttribute("activeLink", "/members");

		try {
			memberLabProfileService.updateMemberLabProfileEnabledStatus(memberLabProfileId, enabled);
			String status = enabled ? "mở khóa" : "khóa";
			String message = new StringBuffer("").append("Đã ").append(status).append(" hồ sơ thành viên lab ID ")
					.append(memberLabProfileId).toString();
			redirectAttributes.addFlashAttribute("successMessage", message);

		} catch (MemberLabProfileNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/members";
		}

		return "redirect:/members";
	}

}
