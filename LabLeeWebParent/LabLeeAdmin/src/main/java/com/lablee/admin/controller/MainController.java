package com.lablee.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping({"", "/"})
	public String getHomePage() {
		return "index";
	}
	
	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}
}
