package com.luas.securitylearning.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

public abstract class BaseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public <T> T get(String sql, Map<String, Object> paramMap, Class<T> mappedClass) {
        return getNamedParameterJdbcTemplate().queryForObject(sql, paramMap, new BeanPropertyRowMapper<>(mappedClass));
    }

    public <T> List<T> list(String sql, Map<String, Object> paramMap, Class<T> elementType) {
        return getNamedParameterJdbcTemplate().queryForList(sql, paramMap, elementType);
    }

    public <T> List<T> list(String sql, Map<String, Object> paramMap, RowMapper<T> rowMapper) {
        return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}
