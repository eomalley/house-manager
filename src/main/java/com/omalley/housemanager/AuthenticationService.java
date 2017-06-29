package com.omalley.housemanager;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.omalley.housemanager.dao.UserInfoDao;
import com.omalley.housemanager.domain.UserInfo;

@Service
public class AuthenticationService implements UserDetailsService
{

    @Autowired
    private UserInfoDao userDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserInfo userInfo = this.userDao.findByUserName(username);
        UserDetails userDetails = null;
        if(userInfo != null)
        {
            GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getRole());
            userDetails = new User(userInfo.getUserName(), userInfo.getPassword(), Arrays.asList(authority));
        }
        return userDetails;
    }

}
