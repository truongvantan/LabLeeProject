package com.lablee.client.common;

import java.util.List;

import com.lablee.common.entity.Setting;
import com.lablee.common.entity.SettingBag;

public class GeneralSettingBag extends SettingBag {
	public GeneralSettingBag() {
		super();
	}

	public GeneralSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}

	public void updateSiteLogo(String value) {
		super.update("SITE_LOGO", value);
	}
}
