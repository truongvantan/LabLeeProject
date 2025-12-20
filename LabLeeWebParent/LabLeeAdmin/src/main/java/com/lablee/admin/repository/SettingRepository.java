package com.lablee.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lablee.common.constant.SettingCategory;
import com.lablee.common.entity.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {
	List<Setting> findByCategory(SettingCategory category);
}
