package com.form;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
	
	public void dashboard() {
		// request당 하나의 쓰레드 사용 -> 톰캣의 커넥터가 쓰레드 배정 //  session에 따른 유저
		
		// SecurityContextHolder(thredlocal사용 -> trhed끼리 공유하는 객체) -> SecurityContext -> Authentication
		// principal과 GrantAuthority제공
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// principal -> 누구에 해당하는 정보 
		// UserDetailsService(유저 정보를 UserDetails타입으로 가져오는 DAO 인터페이스)에서 리턴한 객체
		// 객체는 UserDetails(스프링 시큐리티가 사용하는 Authentication 객체 사이의 어댑터)타입 authenticationManager가 인증 진행
		// GrantAuthority -> ROLE_USER, ROLE_ADMIN 등 pricipal이 가지고 있는 권한
		Object principal = authentication.getPrincipal(); 
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		//인증이 되면 필요가 없다
		Object credentials = authentication.getCredentials();
		
		// 인증된 사용자인가?
		boolean authenticated = authentication.isAuthenticated();
		
	}
}
