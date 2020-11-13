package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.User;
import com.epam.esm.utils.EntityRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private JdbcTemplate jdbcTemplate;
    private UserRowMapper userRowMapper = UserRowMapper.INSTANCE;

    @Autowired
    public UserDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        try {
            return jdbcTemplate.query(SQLQuery.GET_ALL_USERS.getQuery(), userRowMapper);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_USERS.getErrorCode(), e);
        }
    }

    @Override
    public User getUserById(int id) {
        try {
            return jdbcTemplate.query(SQLQuery.GET_USER_BY_ID.getQuery(), userRowMapper, id).stream().findFirst().orElse(null);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_USER.getErrorCode(), e);
        }
    }

    private enum UserRowMapper implements RowMapper<User> {
        INSTANCE(new EntityRowMapper());

        private EntityRowMapper entityRowMapper;

        UserRowMapper(EntityRowMapper entityRowMapper) {
            this.entityRowMapper = entityRowMapper;
        }

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return entityRowMapper.mapUserFields(rs);
        }
    }
}
