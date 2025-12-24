package com.lablee.client.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lablee.client.repository.SettingRepository;
import com.lablee.common.constant.SettingCategory;
import com.lablee.common.entity.Setting;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingService {
	private final SettingRepository settingRepository;
	
	public List<Setting> listAllSettings() {
		return settingRepository.findAll();
	}
	
	public List<Setting> getGeneralSettings() {
		return settingRepository.findByGeneralCategory(SettingCategory.GENERAL);
	}

	public Setting getAboutUsSetting() {
		Setting aboutUsSetting = settingRepository.findByKey("SITE_ABOUT_US");
		return aboutUsSetting;
	}
}
