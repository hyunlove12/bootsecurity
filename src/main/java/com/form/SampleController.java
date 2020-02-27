package com.form;

import java.security.Principal;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.account.AccountContext;
import com.account.AccountRepository;
import com.common.SecurityLogger;

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
	
	@GetMapping("/async-handler")
	@ResponseBody
	public Callable<String> asyncHandler() {		
		// 톰캣이 할당해준 nio쓰레드
		// callable 안에 있는 쓰레드는 별도의 쓰레드
		SecurityLogger.log("MVC");
		// callable이 처리 되기 전 httpresponse를 반환 후.
		// callable이 완료되면 다시 반환이 된다.
		return () -> {
			// 다른 쓰레드를 사용함에도 동일한 principal을 사용하게 해주는 필터
			// -> webasyncmanagerintegrationfilter
			SecurityLogger.log("Callable");
			return "Async-Handler";
		};
		/* return new Callable<String>() {
			@Override
			public String call() throws Exception {
				// 
				SecurityLogger.log("Callable");
				return "Async-Handler";
			}
		}; */
	}
	
	@GetMapping("/async-service")
	@ResponseBody
	public String asyncService() {
		SecurityLogger.log("MVC, before async service");
		sampleService.asyncService();
		SecurityLogger.log("MVC, after async service");
		return "Async Service";
	}
	
}
