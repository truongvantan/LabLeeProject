package com.lablee.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lablee.client.exception.MemberLabProfileNotFoundException;
import com.lablee.client.repository.MemberLabProfileRepository;
import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.MemberLabProfile;
import com.lablee.common.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberLabProfileService {

	private final MemberLabProfileRepository memberLabProfileRepository;
	
	private static final int PAGE_SIZE = 3;

	public MemberLabProfile findByIdEnabled( String memberProfileId) throws MemberLabProfileNotFoundException {
		int id = -1;
		
		try {
			id = Integer.parseInt(memberProfileId);
		} catch (NumberFormatException e) {
			throw new MemberLabProfileNotFoundException("Could not find member with ID: " + memberProfileId);
		}
		
		MemberLabProfile memberLabProfile = new MemberLabProfile();
		Optional<MemberLabProfile> oMemberLabProfile = memberLabProfileRepository.findByIdEnabled(id);
		
		if (oMemberLabProfile.isPresent()) {
			memberLabProfile = oMemberLabProfile.get();
			return memberLabProfile;
		} else {
			throw new MemberLabProfileNotFoundException("Could not find member with ID: " + memberProfileId);
		}
		
	}

	/**
	 * @return Object[0]: List(MemberLabProfile)<br>
	 *         Object[1]: int totalPages<br>
	 *         Object[2]: long totalElements
	 */
	public Object[] listByPage(String strPageNum, String keyword, String sortField, String sortDir) {
		List<MemberLabProfile> listMemberLabProfile = new ArrayList<>();

		int pageNum = 1;

		try {
			pageNum = Integer.parseInt(strPageNum);
		} catch (NumberFormatException e) {
			pageNum = 1;
		}
		
		Sort sort = Sort.by(sortField);
		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

		Page<MemberLabProfile> pageMemberLabProfile = null;

		if (keyword == null || keyword.isBlank()) {
			pageMemberLabProfile = memberLabProfileRepository.findAllEnabled(pageable);
		} else {
			pageMemberLabProfile = memberLabProfileRepository.findAllEnabled(keyword.trim(), pageable);
		}

		int totalPages = pageMemberLabProfile.getTotalPages();
		long totalElements = pageMemberLabProfile.getTotalElements();

		listMemberLabProfile = pageMemberLabProfile.getContent();

		return new Object[] { listMemberLabProfile, totalPages, totalElements };
	}

}
