package com.luas.securitylearning.dao;

import com.luas.securitylearning.domain.po.SysUser;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao extends BaseDao {

    public SysUser get(String id) {
        String sql = "select id, name, username, password, age, state from sys_user where id = :id";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);

        return get(sql, paramMap, SysUser.class);
    }

    public SysUser getByUsername(String username) {
        String sql = "select id, name, username, password, age, state from sys_user where username = :username";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", username);

        return get(sql, paramMap, SysUser.class);
    }

    public List<String> listUserRole(String id) {
        String sql = "select role_id\n" +
                    "  from sys_user_role\n" +
                    " where sys_user_role.user_id = :userId";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", id);

        return list(sql, paramMap, String.class);
    }

}
