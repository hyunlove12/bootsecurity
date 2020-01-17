package com.account;

public class AccountContext {
	
	// 같은 쓰레드 안에서 공유
	// 같은 쓰레드라면 해당 데이터를 메소드 매개변수로 넘겨줄 필요 없다.
	// SecurityContextHolder의 기본 전략 -> principal을 Threadlocal로 가지고 있는다
	private static final ThreadLocal<Account> ACCOUNT_THREAD_LOCAL = new ThreadLocal<>();
	
	public static void setAccount(Account account) {
		ACCOUNT_THREAD_LOCAL.set(account);
	}
	
	public static Account getAccount() {
		return ACCOUNT_THREAD_LOCAL.get();
	}
	
}
