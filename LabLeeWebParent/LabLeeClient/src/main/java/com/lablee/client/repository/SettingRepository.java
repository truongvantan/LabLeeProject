package com.lablee.client.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lablee.common.constant.SettingCategory;
import com.lablee.common.entity.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {
	List<Setting> findByCategory(SettingCategory category);

	@Query("""
			SELECT s
			FROM Setting s
			WHERE s.category = :generalSettingCategory
			""")
	List<Setting> findByGeneralCategory(@Param("generalSettingCategory") SettingCategory generalSettingCategory);

	Setting findByKey(String string);
}
