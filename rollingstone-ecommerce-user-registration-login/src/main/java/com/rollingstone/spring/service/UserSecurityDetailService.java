package com.rollingstone.spring.service;

import com.rollingstone.spring.dao.UserDaoRepository;
import com.rollingstone.spring.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserSecurityDetailService implements UserDetailsService {

    @Autowired
    private UserDaoRepository userDaoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity userFroDb = userDaoRepository.findByEmail(username);
        if(userFroDb ==null){
            throw new UsernameNotFoundException(username);
        }
        UserDetails user = User.withUsername(userFroDb.getEmail()).password(userFroDb.getEncryptedPassword()).authorities("USER").build();
        return user;
    }
}
