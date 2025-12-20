package com.lablee.admin.controller;

import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lablee.admin.config.LabLeeUserDetails;
import com.lablee.admin.dto.RoleDTO;
import com.lablee.admin.dto.UserAccountFormEditDTO;
import com.lablee.admin.exception.UserNotFoundException;
import com.lablee.admin.service.UserService;
import com.lablee.common.constant.ConstantUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AccountController {
	private final UserService userService;

	@GetMapping("/account")
	public String showAccountDetails(@AuthenticationPrincipal LabLeeUserDetails loggedUser, Model model, RedirectAttributes redirectAttributes) {
		String email = loggedUser.getUsername();
		UserAccountFormEditDTO userAccountFormEditDTO = null;

		try {
			userAccountFormEditDTO = userService.findByEmail(email);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "Không tìm thấy tài khoản có email: " + email);
			redirectAttributes.addAttribute("errorMessage", "Không tìm thấy tài khoản có email: " + email);
			return "redirect:/";
		}

		model.addAttribute("userAccountFormEditDTO", userAccountFormEditDTO);
		return "users/account_form";
	}

	@PostMapping("/account/edit")
	public String editAccount(@AuthenticationPrincipal LabLeeUserDetails loggedUser, Model model,
			@RequestParam(name = "image", required = false) MultipartFile multipartFile,
			@RequestParam(name = "newPassword", required = false) String newPassword,
			@RequestParam(name = "confirmPassword", required = false) String confirmPassword,
			@Valid @ModelAttribute("userAccountFormEditDTO") UserAccountFormEditDTO userAccountFormEditDTO,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		
		userAccountFormEditDTO.setNewPassword(newPassword);
		userAccountFormEditDTO.setConfirmPassword(confirmPassword);

		String messageReturned = userService.editUserAccount(userAccountFormEditDTO, bindingResult, multipartFile);
		Set<RoleDTO> setRoles = userService.getSetRolesByEmail(loggedUser.getUsername());
		userAccountFormEditDTO.setSetRoles(setRoles);
		
		switch (messageReturned) {
		case ConstantUtil.MESSAGE_SUCCESS_EDIT_USER_ACCOUNT:
			model.addAttribute("successMessage", messageReturned);
			loggedUser.setFullName(userAccountFormEditDTO.getFullName());
		case ConstantUtil.MESSAGE_FAIL_VALIDATION_BINDING_RESULT:
			break;
		default:
			model.addAttribute("errorMessage", messageReturned);
			break;
		}


		return "users/account_form";
//		return "redirect:/account";
	}
}
