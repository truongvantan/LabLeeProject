package com.lablee.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lablee.admin.common.GeneralSettingBag;
import com.lablee.admin.service.SettingService;
import com.lablee.admin.util.FileUploadUtil;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SettingController {
	private final SettingService settingService;
	
	@GetMapping("/settings")
	public String listAll(Model model) {
		model.addAttribute("activeLink", "/settings");
		
		List<Setting> listSettings = settingService.listAllSettings();


		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}

		return "settings/setting_form";
	}
	
	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam(name = "fileImage", required = false) MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
		if (!FileUploadUtil.isValidFileSize(multipartFile)) {
			redirectAttributes.addFlashAttribute("error", "Vui lòng chọn tệp có kích thước không vượt quá 1MB");
			return "redirect:/settings";
		}
		
		GeneralSettingBag settingBag = settingService.getGeneralSettings();
		
		if (!settingService.checkValidSettingValue(request, settingBag.list())) {
			redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin.");
			return "redirect:/settings";
		}
		
		saveSiteLogo(multipartFile, settingBag);
		updateSettingValuesFromForm(request, settingBag.list());
		redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công.");
		
		return "redirect:/settings";
	}
	
	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		String value = "";
		for (Setting setting : listSettings) {
			value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}
		settingService.saveAll(listSettings);
	}
	
	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = ConstantUtil.PATH_SITE_LOGO_STORED_DEFAULT + fileName;
			settingBag.updateSiteLogo(value);

			String uploadDir = ConstantUtil.PATH_SITE_LOGO_DIR_DEFAULT;
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
	}
	
	
}
