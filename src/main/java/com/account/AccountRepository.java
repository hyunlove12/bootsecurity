package com.account;

import org.springframework.data.jpa.repository.JpaRepository;

//이미 bean으로 등록 된 것처럼 사용 가능
public interface AccountRepository extends JpaRepository<Account, Integer>{

	Account findByUsername(String username);

}
