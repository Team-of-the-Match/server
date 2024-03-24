package com.totm.totm.service;

import com.totm.totm.entity.Manager;
import com.totm.totm.entity.Member;
import com.totm.totm.exception.ManagerNotFoundException;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.repository.ManagerRepository;
import com.totm.totm.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(username.contains("@")) {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없음"));

            return createUserDetails(member);
        } else {
            Manager manager = managerRepository.findByUsername(username)
                    .orElseThrow(() -> new ManagerNotFoundException("해당 매니저를 찾을 수 없음"));

            return createUserDetails(manager);
        }
    }

    private UserDetails createUserDetails(Member member) {
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(member.getId()),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority("member"))
        );
    }

    private UserDetails createUserDetails(Manager manager) {
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(manager.getId()),
                manager.getPassword(),
                List.of(new SimpleGrantedAuthority("manager"))
        );
    }
}
