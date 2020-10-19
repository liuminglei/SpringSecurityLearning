package com.luas.securitylearning.security.core.userdetails.jdbc;

import com.luas.securitylearning.dao.RoleDao;
import com.luas.securitylearning.dao.UserDao;
import com.luas.securitylearning.domain.po.SysUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;

public class CustomJdbcUserDetailsService implements UserDetailsService {

    private UserDao userDao;

    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = this.userDao.getByUsername(username);

        User.UserBuilder builder = User.builder()
                .username(sysUser.getUsername())
                .password(sysUser.getPassword());

        List<String> roles = this.roleDao.listRoleCodeByUserId(sysUser.getId());

        builder.roles(roles == null ? new String[] {} : roles.toArray(new String[] {}));

        return builder.build();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }
}
