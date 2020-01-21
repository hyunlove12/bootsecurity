package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 15)
@EnableWebSecurity //없어도 boot가 자동으로 추가해준다.
public class AnotherConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {		
		//filterchain이 설정되고 만들어지는 지점 -> class자체가 filterchain이 되고 각각의 요청에 맞는 필터체인이 사용된다.
		//메소드체이닝을 하지 않아도 된다.
		//http 각각 설정해도 된다.
		http.antMatcher("/account/**").authorizeRequests()
			.anyRequest()
			.permitAll(); 
	}
}
