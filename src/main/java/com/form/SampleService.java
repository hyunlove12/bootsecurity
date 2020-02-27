package com.form;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.account.Account;
import com.account.AccountContext;
import com.common.SecurityLogger;

@Service
public class SampleService {
	
	//요청마다 시큐리티 컨텍스트를 넣어주고 비워준다 -> 비운 후 세션에서 값을 꺼내서 넣어준다.
	//http세션이 바뀌면 인증정보가 날라간다 -> 다시 로그인 필요 -> 사용 안하면 매 요청마다 인증 필요 -> 헤더 혹은 요청본문에 인증정보가 들어 있어야 한다.
	//UsernamePasswrodAuth필터, securityContextPersistence필터 
	// AccountContext, ThreadLocal을 이용한 유저정보
//	public void dashboard() {
		// request당 하나의 쓰레드 사용 -> 톰캣의 커넥터가 쓰레드 배정 //  session에 따른 유저
		
		// SecurityContextHolder(thredlocal사용 -> trhed끼리 공유하는 객체) -> SecurityContext -> Authentication
		// principal과 GrantAuthority제공
	//	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// principal -> 누구에 해당하는 정보 
		// UserDetailsService(유저 정보를 UserDetails타입으로 가져오는 DAO 인터페이스)에서 리턴한 객체
		// 객체는 UserDetails(스프링 시큐리티가 사용하는 Authentication 객체 사이의 어댑터)타입 authenticationManager가 인증 진행
		// GrantAuthority -> ROLE_USER, ROLE_ADMIN 등 pricipal이 가지고 있는 권한
	//	Object principal = authentication.getPrincipal(); 
	//	Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		//인증이 되면 필요가 없다
	//	Object credentials = authentication.getCredentials();
		
		// 인증된 사용자인가?
	//	boolean authenticated = authentication.isAuthenticated();
	//}
	
	public void dashboard() {
		Account account = AccountContext.getAccount();
		System.out.println("+++++++++++++++++");
		System.out.println(account.getUsername());
	}
	
	@Async
	public void asyncService() {
		// async사용 시 쓰레드 공유가 안된다
		SecurityLogger.log("Async service");
		System.out.println("Async service is called");
	}
	
	
}
