package com.form;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.account.Account;
import com.account.AccountContext;
import com.account.AccountRepository;

@Controller
public class SampleController {
	
	@Autowired
	SampleService sampleService;
	
	@Autowired
	AccountRepository accountRepository;
	
	@GetMapping("/")
	public String index(Model model, Principal principal) {
		if (principal == null) {
			model.addAttribute("message", "Hello! Spring!!!");
		} else {
			model.addAttribute("message", principal.getName());
		}		
		return "index";
	}
	
	@GetMapping("/info")
	public String info(Model model) {
		model.addAttribute("message", "Hello! Spring!!!");
		return "info";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		//princiapl자체를 파라미터로 받는 것은 아무런 시큐리티가 동작하지 않는다.
		model.addAttribute("message", "Hello" + principal.getName());
		//스프링시큐리티홀더는 자동으로 threadlocal에 넣어준다
		AccountContext.setAccount(accountRepository.findByUsername(principal.getName()));
		sampleService.dashboard();
		return "dashboard";
	}
	
	@GetMapping("/admin")
	public String admin(Model model, Principal principal) {
		model.addAttribute("message", "Hello Admin" + principal.getName());
		return "admin";
	}
	
	@GetMapping("/user")
	public String user(Model model, Principal principal) {
		model.addAttribute("message", "Hello User" + principal.getName());
		return "user";
	}
}
