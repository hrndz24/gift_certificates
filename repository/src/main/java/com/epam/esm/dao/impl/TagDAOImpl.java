package com.epam.esm.dao.impl;

import com.epam.esm.dao.ColumnLabel;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.Tag;
import com.epam.esm.utils.EntityRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TagDAOImpl implements TagDAO {

    private JdbcTemplate jdbcTemplate;
    private TagRowMapper tagRowMapper = TagRowMapper.INSTANCE;

    private static final String TAG_TABLE_NAME = "tag";

    @Autowired
    public TagDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Tag addTag(Tag tag) {
        Map<String, Object> parameters = fillInTagParameters(tag);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TAG_TABLE_NAME)
                .usingGeneratedKeyColumns(ColumnLabel.ID.getColumnName());
        try {
            tag.setId(simpleJdbcInsert.executeAndReturnKey(parameters).intValue());
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_ADD_TAG.getErrorCode(), e);
        }
        return tag;
    }

    private Map<String, Object> fillInTagParameters(Tag tag) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ColumnLabel.ID.getColumnName(), tag.getId());
        parameters.put(ColumnLabel.NAME.getColumnName(), tag.getName());
        return parameters;
    }

    @Override
    public void removeTag(int tagId) {
        try {
            jdbcTemplate.update(SQLQuery.DELETE_TAG.getQuery(), tagId);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_REMOVE_TAG.getErrorCode(), e);
        }
    }

    @Override
    public List<Tag> getTags() {
        try {
            return jdbcTemplate.query(SQLQuery.GET_ALL_TAGS.getQuery(), tagRowMapper);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_TAGS.getErrorCode(), e);
        }
    }

    @Override
    public Tag getTagById(int id) {
        try {
            return jdbcTemplate.query(SQLQuery.GET_TAG_BY_ID.getQuery(), tagRowMapper, id).stream().findFirst().orElse(null);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_TAG.getErrorCode(), e);
        }
    }

    private enum TagRowMapper implements RowMapper<Tag> {
        INSTANCE(new EntityRowMapper());

        private EntityRowMapper entityRowMapper;

        TagRowMapper(EntityRowMapper entityRowMapper) {
            this.entityRowMapper = entityRowMapper;
        }

        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            return entityRowMapper.mapTagFields(rs);
        }
    }
}
