package com.lablee.client.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lablee.client.service.MemberLabProfileService;
import com.lablee.client.service.NewsService;
import com.lablee.client.service.ProjectService;
import com.lablee.client.service.PublicationService;
import com.lablee.common.entity.MemberLabProfile;
import com.lablee.common.entity.News;
import com.lablee.common.entity.Project;
import com.lablee.common.entity.Publication;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final MemberLabProfileService memberLabProfileService;
	private final PublicationService publicationService;
	private final ProjectService projectService;
	private final NewsService newsService;
	
	@GetMapping({"", "/"})
	public String getHomePage(Model model) {
		model.addAttribute("activeLink", "/");
		
		long numberOfMembers = memberLabProfileService.getTotalMembersEnabled();
		long numberOfPublications = publicationService.getTotalPublicationsEnabled();
		long numberOfProjects = projectService.getTotalProjectsEnabled();
		
		Project latestProject = projectService.getLatestProject();
		
		List<MemberLabProfile> listMembers = memberLabProfileService.getAllMembersEnabled();
		
		List<Publication> list4LatestPublications = publicationService.getList3LatestPublications();
		
		
		List<News> list4LatestNews = newsService.getList4LatestNews();
		
		
		model.addAttribute("numberOfMembers", numberOfMembers);
		model.addAttribute("numberOfPublications", numberOfPublications);
		model.addAttribute("numberOfProjects", numberOfProjects);
		model.addAttribute("latestProject", latestProject);
		model.addAttribute("listMembers", listMembers);
		model.addAttribute("list3LatestPublications", list4LatestPublications);
		model.addAttribute("list4LatestNews", list4LatestNews);
		
		return "index";
	}	
}
