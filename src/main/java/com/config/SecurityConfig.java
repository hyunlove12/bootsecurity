package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.account.AccountService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	AccountService accountService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//메소드체이닝을 하지 않아도 된다.
		//http 각각 설정해도 된다.
		http.authorizeRequests()
				.mvcMatchers("/", "/info", "/account/**").permitAll()
				.mvcMatchers("/admin").hasRole("ADMIN") // admin은 ADMIN권한 필요
				.anyRequest().authenticated() //그 외 어떠한 요청은 인증만 하면 된다.
				.and()	//메소드 체이닝
			.formLogin(); //form 로그인 사용
		http.httpBasic(); //httpbasic사용		
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
}