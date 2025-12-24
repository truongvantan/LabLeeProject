package com.lablee.client.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lablee.common.entity.VisitLog;

@Repository
public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {
	boolean existsByIpAddressAndUserAgentAndPageKeyAndVisitDate(String ipAddress, String userAgent, String pageKey,
			LocalDate visitDate);

	@Query("SELECT COUNT(v) FROM VisitLog v")
	long countTotalVisits();
}
