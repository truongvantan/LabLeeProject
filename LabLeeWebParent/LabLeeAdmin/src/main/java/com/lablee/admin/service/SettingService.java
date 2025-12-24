package com.lablee.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lablee.admin.common.GeneralSettingBag;
import com.lablee.admin.repository.SettingRepository;
import com.lablee.common.constant.SettingCategory;
import com.lablee.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingService {
	private final SettingRepository settingRepository;
	
	public List<Setting> listAllSettings() {
		return settingRepository.findAll();
	}
	
	public GeneralSettingBag getGeneralSettings() {
		List<Setting> settings = new ArrayList<Setting>();

		List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);

		settings.addAll(generalSettings);
		
		return new GeneralSettingBag(settings);
	}
	
	@Transactional
	public void saveAll(Iterable<Setting> settings) {
		settingRepository.saveAll(settings);
	}
	
	public boolean checkValidSettingValue(HttpServletRequest request, List<Setting> listSettings) {
		String value = "";
		for (Setting setting : listSettings) {
			if ("SITE_LOGO".equalsIgnoreCase(setting.getKey())) {
				continue;
			}
			
			value = request.getParameter(setting.getKey());
			
			if (value == null || value.isBlank()) {
				return false;
			}
		}
		
		return true;
	}

	public List<Setting> getAboutUsSettings() {
		return settingRepository.findByCategory(SettingCategory.ABOUT_US);
	}
}
