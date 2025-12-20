package com.lablee.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lablee.client.exception.ProjectNotFoundException;
import com.lablee.client.repository.ProjectRepository;
import com.lablee.common.entity.Project;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;

	private static final int PAGE_SIZE = 3;

	/**
	 * @return Object[0]: List(Project)<br>
	 *         Object[1]: int totalPages<br>
	 *         Object[2]: long totalElements
	 */
	public Object[] listByPage(String strPageNum, String keyword, String sortField, String sortDir) {
		List<Project> listProjects = new ArrayList<>();

		int pageNum = 1;

		try {
			pageNum = Integer.parseInt(strPageNum);
		} catch (NumberFormatException e) {
			pageNum = 1;
		}

		Sort sort = Sort.by(sortField);
		sort = "Ascending".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

		Page<Project> pageProject = null;

		if (keyword == null || keyword.isBlank()) {
			pageProject = projectRepository.findAllEnabled(pageable);
		} else {
			pageProject = projectRepository.findAllEnabled(keyword.trim(), pageable);
		}

		int totalPages = pageProject.getTotalPages();
		long totalElements = pageProject.getTotalElements();

		listProjects = pageProject.getContent();

		return new Object[] { listProjects, totalPages, totalElements };
	}

	public Project findByIdEnabled(String projectId) throws ProjectNotFoundException {
		int id = -1;

		try {
			id = Integer.parseInt(projectId);
		} catch (NumberFormatException e) {
			throw new ProjectNotFoundException("Could not find project with ID: " + projectId);
		}

		Project project = new Project();
		Optional<Project> oProject = projectRepository.findByIdEnabled(id);

		if (oProject.isPresent()) {
			project = oProject.get();
			return project;
		} else {
			throw new ProjectNotFoundException("Could not find project with ID: " + projectId);
		}
	}

	public long getTotalProjects() {
		return projectRepository.count();
	}

	public Project getLatestProject() {
		return projectRepository.findFirstByOrderByStartDateDesc();
	}

	public long getTotalProjectsEnabled() {
		return projectRepository.getTotalProjectsEnabled();
	}
}
