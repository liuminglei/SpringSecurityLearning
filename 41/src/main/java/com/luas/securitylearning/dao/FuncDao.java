package com.luas.securitylearning.dao;

import com.luas.securitylearning.domain.po.SysFuncRole;
import com.luas.securitylearning.domain.po.SysUserFunc;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FuncDao extends BaseDao {

    public List<SysFuncRole> listFuncRole() {
        String sql = "select sys_role.id roleId,\n" +
                "       sys_role.code roleCode,\n" +
                "       sys_func.id funcId,\n" +
                "       sys_func.url url\n" +
                "  from sys_func, sys_role_func, sys_role\n" +
                " where sys_func.id = sys_role_func.func_id\n" +
                "   and sys_role_func.role_id = sys_role.id\n";

        return list(sql, new HashMap<>(), new BeanPropertyRowMapper<>(SysFuncRole.class));
    }

    public List<SysUserFunc> listUserFunc(String userId) {
        String sql = "select sys_user_role.user_id userId,\n" +
                    "       sys_user_role.role_id roleId,\n" +
                    "       sys_func.id           funcId\n" +
                    "  from sys_func, sys_role_func, sys_user_role\n" +
                    " where sys_func.id = sys_role_func.func_id\n" +
                    "   and sys_role_func.role_id = sys_user_role.role_id\n" +
                    "   and sys_user_role.user_id = :userId";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);

        return list(sql, paramMap, new BeanPropertyRowMapper<>(SysUserFunc.class));
    }

}
