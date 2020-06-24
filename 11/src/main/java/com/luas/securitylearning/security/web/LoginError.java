package com.luas.securitylearning.security.web;

import java.util.HashMap;
import java.util.Map;

public enum LoginError {

    BADCREDENTIALS(1, "用户名密码错误！"),

    LOCKED(2, "用户已被锁定，无法登录！"),

    ACCOUNTEXPIRED(3, "用户已过时，无法登录！"),

    USERNAMENOTFOUND(4, "用户不存在！");

    private Integer type;

    private String message;

    private final static Map<Integer, LoginError> mappings = new HashMap<>();

    static {
        mappings.put(BADCREDENTIALS.type, BADCREDENTIALS);
        mappings.put(LOCKED.type, BADCREDENTIALS);
        mappings.put(ACCOUNTEXPIRED.type, BADCREDENTIALS);
        mappings.put(USERNAMENOTFOUND.type, BADCREDENTIALS);

    }

    public static boolean support(Integer type) {
        if (type == null) {
            return false;
        }

        return mappings.get(type) != null;
    }

    public static LoginError resolve(Integer type) {
        if (type == null) {
            throw new IllegalArgumentException("不支持" + type + "类型！");
        }

        return mappings.get(type);
    }

    public static String getMessage(Integer type) {
        LoginError loginError = resolve(type);

        return loginError.message;
    }

    LoginError(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
