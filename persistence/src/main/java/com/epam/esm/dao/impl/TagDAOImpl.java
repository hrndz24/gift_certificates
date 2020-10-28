package com.epam.esm.dao.impl;

import com.epam.esm.dao.ColumnLabel;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
        tag.setId(simpleJdbcInsert.executeAndReturnKey(parameters).intValue());
    }

    private Map<String, Object> fillInTagParameters(Tag tag) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ColumnLabel.ID.getColumnName(), tag.getId());
        parameters.put(ColumnLabel.NAME.getColumnName(), tag.getName());
        return parameters;
    }

    @Override
    public void removeTag(Tag tag) {
        jdbcTemplate.update(DELETE_TAG, tag.getId());
    }

    @Override
    public List<Tag> getTags() {
        return jdbcTemplate.query(GET_TAGS_ALL, tagRowMapper);
    }

    @Override
    public Tag getTagById(int id) {
        return jdbcTemplate.queryForObject(GET_TAG_BY_ID, new Object[]{id}, tagRowMapper);
    }

    @Override
    public Tag getTagByName(String name) {
        return jdbcTemplate.queryForObject(GET_TAG_BY_NAME, new Object[]{name}, tagRowMapper);
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
