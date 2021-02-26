package com.household.accountbook.auth;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.household.accountbook.entity.Account;
import com.household.accountbook.mapper.AccountMapper;
@Service
public class DbAccountDetailsService implements UserDetailsService {

	@Autowired
	AccountMapper accountMapper;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		// DBから情報を取得
		Account account = Optional.ofNullable(accountMapper.loginExamination(loginId))
				.orElseThrow(() -> new UsernameNotFoundException("認証に失敗しました"));
		
		return new DbAccountDetails(account, getAuthorities(account));
	}

	/**
     * 認証が通った時にこのユーザに与える権限の範囲を設定する。
     * @param account DBから取得したユーザ情報。
     * @return 権限の範囲のリスト。
     */
	private Collection<GrantedAuthority> getAuthorities(Account account) {
		
		return AuthorityUtils.createAuthorityList("ROLE_USER");
	}

}
