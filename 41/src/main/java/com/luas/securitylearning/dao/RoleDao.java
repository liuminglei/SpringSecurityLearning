package com.luas.securitylearning.dao;

import com.luas.securitylearning.domain.po.SysRole;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleDao extends BaseDao {

    public SysRole get(String id) {
        String sql = "select id, name, code from sys_role where id = :id";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);

        return get(sql, paramMap, SysRole.class);
    }

    public SysRole getByCode(String code) {
        String sql = "select id, name, code from sys_role where code = :code";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", code);

        return get(sql, paramMap, SysRole.class);
    }

    public List<SysRole> listRoleByUserId(String userId) {
        String sql = "select sys_role.id, sys_role.name, sys_role.code\n" +
                    "  from sys_role, sys_user_role\n" +
                    " where sys_role.id = sys_user_role.role_id\n" +
                    "   and sys_user_role.user_id = :userId";


        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);

        return list(sql, paramMap, new BeanPropertyRowMapper<>(SysRole.class));
    }

    public List<String> listRoleIdByUserId(String userId) {
        String sql = "select sys_role.id\n" +
                "  from sys_role, sys_user_role\n" +
                " where sys_role.id = sys_user_role.role_id\n" +
                "   and sys_user_role.user_id = :userId";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);

        return list(sql, paramMap, String.class);
    }

    public List<String> listRoleCodeByUserId(String userId) {
        String sql = "select sys_role.code\n" +
                    "  from sys_role, sys_user_role\n" +
                    " where sys_role.id = sys_user_role.role_id\n" +
                    "   and sys_user_role.user_id = :userId";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);

        return list(sql, paramMap, String.class);
    }

}
