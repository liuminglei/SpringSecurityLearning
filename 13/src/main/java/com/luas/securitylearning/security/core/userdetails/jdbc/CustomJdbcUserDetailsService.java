package com.luas.securitylearning.security.core.userdetails.jdbc;

import com.luas.securitylearning.dao.UserDao;
import com.luas.securitylearning.domain.po.SysUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomJdbcUserDetailsService implements UserDetailsService {

    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = this.userDao.getByUsername(username);

        UserDetails user = User.builder()
                .username(sysUser.getUsername())
                .password(sysUser.getPassword())
                .roles("User")
                .build();

        return user;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
