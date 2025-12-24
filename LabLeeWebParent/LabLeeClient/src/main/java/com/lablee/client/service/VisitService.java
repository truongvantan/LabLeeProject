package com.lablee.client.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lablee.client.repository.VisitLogRepository;
import com.lablee.common.entity.VisitLog;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitService {
	private final VisitLogRepository visitLogRepository;

	public void recordVisit(HttpServletRequest request, String pageKey) {

		String ip = resolveClientIp(request);
		String userAgent = request.getHeader("User-Agent");
		LocalDate today = LocalDate.now();

		boolean exists = visitLogRepository.existsByIpAddressAndUserAgentAndPageKeyAndVisitDate(ip, userAgent, pageKey, today);

		if (!exists) {
			VisitLog log = new VisitLog();
			log.setIpAddress(ip);
			log.setUserAgent(userAgent);
			log.setPageKey(pageKey);
			log.setVisitDate(today);

			visitLogRepository.save(log);
		}
	}

	public long getTotalVisits() {
		return visitLogRepository.countTotalVisits();
	}

	private String resolveClientIp(HttpServletRequest request) {
		String xff = request.getHeader("X-Forwarded-For");
		if (xff != null && !xff.isBlank()) {
			return xff.split(",")[0];
		}
		return request.getRemoteAddr();
	}
}
