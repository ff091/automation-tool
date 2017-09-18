package com.automation.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String welcome(Map<String, Object> model) {
		return "layouts/master";
	}

}
