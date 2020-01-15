package com.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService{

	// repository 구현체가 오면 된다.(각각의 db에 맞는)
	// TODO {알고리즘이름}123
	@Autowired 
	AccountRepository accountRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//UserDetailsService -> DAO를 이용하여 user정보 인증
		//username을 이용하여 유저정보를 받아와서 userdetails로 리턴
		Account account = accountRepository.findByUsername(username); //springjpa 가 자동으로 쿼리 생성
		if (account == null) {
			throw new UsernameNotFoundException(username);
		}
		
		//account를 userdetails로 변환
		return User.builder()
				.username(account.getUsername())
				.password(account.getPassword())
				.roles(account.getRole())				
				.build();
	}

	public Account createNew(Account account) {
		account.encodePassword(passwordEncoder);
		return this.accountRepository.save(account);
	}
}
