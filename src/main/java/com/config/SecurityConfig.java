package com.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import com.account.AccountService;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 100)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	// 계층구조를 설정하는 방법 중 하나 -> voter가 사용하는 expressionHandler만 오버라이드
	// authorizeRequests()설정에 .expressionHandler(expressionHandler()); 붙여주면 된다. 
//	public SecurityExpressionHandler expressionHandler() {
//		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//		roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER"); // ADMIN이 USER의 상위 개념이다.		
		
//		DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
//		handler.setRoleHierarchy(roleHierarchy);

//		return handler;
//	}
		
	
	// 계층구조 설정을 위한 소스 -> 인가를 내는 객체(접근 권한)
	// admin권한은 user에 대한 권한이 자동으로 생성된다
	public AccessDecisionManager accessDecisionManager() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER"); // ADMIN이 USER의 상위 개념이다.		
		
		DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
		handler.setRoleHierarchy(roleHierarchy);
		WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
		webExpressionVoter.setExpressionHandler(handler);
		// voter 목록
		List<AccessDecisionVoter<? extends Object>> voters = Arrays.asList(webExpressionVoter);
		return new AffirmativeBased(voters);
	}
	
	
	@Autowired
	AccountService accountService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {		
		//filterchain이 설정되고 만들어지는 지점 -> class자체가 filterchain이 되고 각각의 요청에 맞는 필터체인이 사용된다.
		//메소드체이닝을 하지 않아도 된다.
		//http 각각 설정해도 된다.
		//상충되는 설정의 경우 순서 필요 order
		//logout filter, csrffilter 등등 15 개
		http.authorizeRequests()
				.mvcMatchers("/", "/info", "/account/**").permitAll()
				.mvcMatchers("/admin").hasRole("ADMIN") // admin은 ADMIN권한 필요
				.mvcMatchers("/user").hasRole("USER") // user권한
				// .authorizeRequests()로 인하여 15개의 filter가 적용된다 -> anonymousAuthenticationfilter, filterSecurityInterceptor가 허용한다. 
				// accessDesicionManager가 허용
				// .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // ignoring과 결과는 똑같으나 과정이 다르다 -> 속도가 더 느림
				.anyRequest().authenticated(); //그 외 어떠한 요청은 인증만 하면 된다.
				//.expressionHandler(expressionHandler()); // expressionHandler를 override하는 방법
				// .and()	//메소드 체이닝
		http.formLogin(); //form 로그인 사용 //하나의 필터가 처리
		http.httpBasic(); //httpbasic사용		// 하나의 필터가 처리 
	}

	//있으면 기본 인증 안됨? -> Authentication없다고 나온다.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// inmemory방식
		/* auth.inMemoryAuthentication()//noop 패스워드 기본인코더 -> 인코딩 방식으로 암호화 해서 비교 -> noop은 사용안하는것, 공백이면 인코더 null 에러 발생
			.withUser("dh").password("{noop}123").roles("USER").and()
			.withUser("admin").password("{noop}!@#").roles("ADMIN"); */
		//userdetail구현체르 사용하여 
		auth.userDetailsService(accountService);	//bean으로 드등록만 되어 있으면 사용 가능	
	}
	
	// 필터 적용을 제외하고 싶을 때 websecurity설정
	@Override
	public void configure(WebSecurity web) throws Exception {
		// 매번 static 리소를 적어줘야
		// web.ignoring().mvcMatchers("/favicon.ico");
		// 리소스를 자동으로 시큐리티 무시
		// .toStaticResources() -> at을 사용해서 구체적 명시 가능
		// 동적인 리소스(/main)는 httpSecurity에서 설정해야한다 -> 특정 filter가 적용되야 하기 때문
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
}