package com;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.account.Account;
import com.account.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
	//여러개 있으면 안되는 것인가..juint은?

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	AccountService accountService;
	
	@Test
	@WithAnonymousUser
	public void index_anonymous() throws Exception{
		//익명 접근
		mockMvc.perform(get("/"))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	//annotaion 커스터마이징
	@Test
	@WithUser
	public void index_user() throws Exception {
		//가상의 유저가 있다고 가정하여 테스트
		mockMvc.perform(get("/"))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void index_admin() throws Exception {
		//가상의 유저가 있다고 가정하여 테스트
		mockMvc.perform(get("/").with(user("admin").password("123").roles("ADMIN")))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void admin_user() throws Exception {
		//가상의 유저가 있다고 가정하여 테스트
		mockMvc.perform(get("/admin").with(user("dh").password("123").roles("USER")))
		.andDo(print())
		.andExpect(status().isForbidden());
	}
	
	@Test
	public void admin_admin() throws Exception {
		//가상의 유저가 있다고 가정하여 테스트
		mockMvc.perform(get("/admin").with(user("dh").password("123").roles("ADMIN")))
		.andDo(print())
		.andExpect(status().isOk());
	}
		
	@Test
	@org.springframework.transaction.annotation.Transactional
	public void login_success() throws Exception {
		//transactional -> db 변경사항이 모두 롤백된다. //독립적인 테스트가 된다.
		// spring 혹은 javax의 트랜잭션 모두 동일
		String username = "dh";
		String password = "123";
		Account account = this.createUser(username, password);
		//인증이 되는지 확인
		mockMvc.perform(formLogin().user(account.getUsername()).password(password))
			.andExpect(authenticated());
	}
	
	@Test
	@Transactional
	public void login_success2() throws Exception {
		String username = "dh";
		String password = "123";
		Account account = this.createUser(username, password);
		//인증이 되는지 확인
		mockMvc.perform(formLogin().user(account.getUsername()).password(password))
			.andExpect(authenticated());
	}
	
	@Test
	@org.springframework.transaction.annotation.Transactional
	public void login_fail() throws Exception {
		String username = "dh";
		String password = "123";
		Account account = this.createUser(username, password);
		//인증이 되는지 확인
		mockMvc.perform(formLogin().user(account.getUsername()).password("12314124"))
			.andExpect(unauthenticated());
	}
	
	//임시 유저 생성
	private Account createUser(String username, String password) {
		Account account = new Account();
		account.setUsername(username);
		account.setPassword(password);
		account.setRole("USER");		
		return accountService.createNew(account);
	}
}
