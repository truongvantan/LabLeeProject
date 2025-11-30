package com.lablee.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lablee.client.service.MemberLabProfileService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final MemberLabProfileService memberLabProfileService;
	
	@GetMapping("")
	public String getHomePage(Model model) {
		return "index";
	}
	
}
