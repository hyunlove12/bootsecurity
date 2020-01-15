package com.form;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SampleController {

	@RequestMapping("/")
	public String index(Model model, Principal principal) {
		if (principal == null) {
			model.addAttribute("message", "Hello! Spring!!!");
		} else {
			model.addAttribute("message", principal.getName());
		}		
		return "index";
	}
	
	@RequestMapping("/info")
	public String info(Model model) {
		model.addAttribute("message", "Hello! Spring!!!");
		return "info";
	}
	
	@RequestMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		//princiapl자체를 파라미터로 받는 것은 아무런 시큐리티가 동작하지 않는다.
		model.addAttribute("message", "Hello" + principal.getName());
		return "dashboard";
	}
	
	@RequestMapping("/admin")
	public String admin(Model model, Principal principal) {
		model.addAttribute("message", "Hello Admin" + principal.getName());
		return "admin";
	}
}
