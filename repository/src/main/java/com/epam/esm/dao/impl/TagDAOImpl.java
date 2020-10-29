package com.epam.esm.dao.impl;

import com.epam.esm.dao.ColumnLabel;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.model.Tag;
import com.epam.esm.exception.DAOException;
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

    private static final String DELETE_TAG = "DELETE FROM tags WHERE id = ?";
    private static final String GET_TAGS_ALL = "SELECT id, name FROM tags";
    private static final String GET_TAG_BY_ID = "SELECT id, name FROM tags WHERE id = ?";
    private static final String GET_TAG_BY_NAME = "SELECT id, name FROM tags WHERE name = ?";
    private static final String TAG_TABLE_NAME = "tags";

    @Autowired
    public TagDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addTag(Tag tag) {
        Map<String, Object> parameters = fillInTagParameters(tag);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TAG_TABLE_NAME)
                .usingGeneratedKeyColumns(ColumnLabel.ID.getColumnName());
        try {
            tag.setId(simpleJdbcInsert.executeAndReturnKey(parameters).intValue());
        } catch (DataAccessException e) {
            throw new DAOException("Failed to add tag to the database", e);
        }
    }

    private Map<String, Object> fillInTagParameters(Tag tag) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ColumnLabel.ID.getColumnName(), tag.getId());
        parameters.put(ColumnLabel.NAME.getColumnName(), tag.getName());
        return parameters;
    }

    @Override
    public void removeTag(Tag tag) {
        try {
            jdbcTemplate.update(DELETE_TAG, tag.getId());
        } catch (DataAccessException e) {
            throw new DAOException("Failed to remove tag from the database", e);
        }
    }

    @Override
    public List<Tag> getTags() {
        try {
            return jdbcTemplate.query(GET_TAGS_ALL, tagRowMapper);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get tags from the database", e);
        }
    }

    @Override
    public Tag getTagById(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_TAG_BY_ID, new Object[]{id}, tagRowMapper);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get tag by id from the database", e);
        }
    }

    @Override
    public Tag getTagByName(String name) {
        try {
            return jdbcTemplate.queryForObject(GET_TAG_BY_NAME, new Object[]{name}, tagRowMapper);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get tag by name from the database", e);
        }
    }

    private enum TagRowMapper implements RowMapper<Tag> {
        INSTANCE;

        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tag tag = new Tag();
            tag.setId(rs.getInt(ColumnLabel.ID.getColumnName()));
            tag.setName(rs.getString(ColumnLabel.NAME.getColumnName()));
            return tag;
        }
    }
}
