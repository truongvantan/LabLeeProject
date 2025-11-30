package com.lablee.client.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.lablee.client.common.PaginationCommon;
import com.lablee.client.exception.MemberLabProfileNotFoundException;
import com.lablee.client.service.MemberLabProfileService;
import com.lablee.common.entity.MemberLabProfile;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberLabProfileController {
	private final MemberLabProfileService memberLabProfileService;
	private final PaginationCommon paginationCommon;
	
	@GetMapping("/members")
	public String listFirstPage(Model model) {
		return listByPage(model, "1", "id", "asc", null);
	}
	
	@GetMapping("/members/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) String strPageNum,
			@RequestParam(name = "sortField", defaultValue = "id") String sortField,
			@RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		sortField  = "id";
		sortDir = "asc";

		Object[] arrReturned = memberLabProfileService.listByPage(strPageNum, keyword, sortField, sortDir);

		List<MemberLabProfile> listMemberLabProfiles = (List<MemberLabProfile>) arrReturned[0];
		int totalPageNumber = (int) arrReturned[1];
		long totalElements = (long) arrReturned[2];

		int currentPageNumber = 1;

		try {
			currentPageNumber = Integer.parseInt(strPageNum);
		} catch (NumberFormatException e) {
			currentPageNumber = 1;
		}

		List<Integer> pageNumbers = paginationCommon.getListPageNumbers(totalPageNumber, currentPageNumber);

		model.addAttribute("currentPageNumber", currentPageNumber);
		model.addAttribute("totalPageNumber", totalPageNumber);
		model.addAttribute("totalItems", totalElements);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listMemberLabProfiles", listMemberLabProfiles);

		return "member/list_member";
	}
	
	@GetMapping("/members/details/{memberProfileId}")
	public String showMemberProfileDetail(Model model, @PathVariable(name = "memberProfileId", required = false) String memberProfileId) {
		try {
			MemberLabProfile memberLabProfile = memberLabProfileService.findByIdEnabled(memberProfileId);
			model.addAttribute("memberLabProfile", memberLabProfile);
			
			return "member/member_detail";
		} catch (MemberLabProfileNotFoundException e) {
			return "error/404";
		}
		
	}
}
