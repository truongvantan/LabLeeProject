package com.lablee.common.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "visit_logs", uniqueConstraints = @UniqueConstraint(columnNames = { "ip_address", "user_agent",
		"page_key", "visit_date" }))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VisitLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ip_address", length = 45, nullable = false)
	private String ipAddress;

	@Column(name = "user_agent", columnDefinition = "TEXT", nullable = false)
	private String userAgent;

	@Column(name = "page_key", nullable = false)
	private String pageKey;

	@Column(name = "visit_date", nullable = false)
	private LocalDate visitDate;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
}
