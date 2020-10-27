package com.epam.esm.dao.impl;

import com.epam.esm.dao.ColumnLabel;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
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
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private JdbcTemplate jdbcTemplate;

    private GiftCertificateRowMapper giftCertificateRowMapper = GiftCertificateRowMapper.INSTANCE;

    private static final String DELETE_GIFT_CERTIFICATE =
            "DELETE FROM gift_certificates WHERE id = ?";
    private static final String UPDATE_GIFT_CERTIFICATE =
            "UPDATE gift_certificates SET name = ?, description = ?, price = ?," +
                    " duration = ? WHERE id = ?";
    private static final String ALL_FIELDS =
            "id, name, description, price, create_date, last_update_date, duration";
    private static final String GET_CERTIFICATES_ALL =
            "SELECT " + ALL_FIELDS + " FROM gift_certificates";
    private static final String GET_CERTIFICATE_BY_ID =
            "SELECT " + ALL_FIELDS + " FROM gift_certificates WHERE id = ?";
    private static final String GET_CERTIFICATES_BY_NAME =
            "SELECT " + ALL_FIELDS + " FROM gift_certificates WHERE name LIKE ?";
    private static final String GET_CERTIFICATES_BY_DESCRIPTION =
            "SELECT " + ALL_FIELDS + " FROM gift_certificates WHERE description LIKE ?";
    private static final String GET_CERTIFICATES_BY_TAG_NAME =
            "SELECT " + ALL_FIELDS + " FROM gift_certificates AS gc JOIN \n" +
                    "(SELECT certificate_id FROM certificate_tags WHERE certificate_tags.tag_id =\n" +
                    "(SELECT id from tags WHERE tags.name LIKE ?)) AS ct ON gc.id = ct.certificate_id;";
    private static final String GET_CERTIFICATES_SORTED_BY_DATE_ASCENDING =
            "SELECT " + ALL_FIELDS + " FROM gift_certificates ORDER BY create_date";
    private static final String GET_CERTIFICATES_SORTED_BY_DATE_DESCENDING =
            "SELECT " + ALL_FIELDS + " FROM gift_certificates ORDER BY create_date DESC";
    private static final String GET_CERTIFICATES_SORTED_BY_NAME_ASCENDING =
            "SELECT " + ALL_FIELDS + " FROM gift_certificates ORDER BY name";
    private static final String GET_CERTIFICATES_SORTED_BY_NAME_DESCENDING =
            "SELECT " + ALL_FIELDS + " FROM gift_certificates ORDER BY name DESC";
    private static final String GIFT_CERTIFICATE_TABLE_NAME = "gift_certificates";

    @Autowired
    public GiftCertificateDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addCertificate(GiftCertificate certificate) {
        Map<String, Object> parameters = fillInCertificateParameters(certificate);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(GIFT_CERTIFICATE_TABLE_NAME)
                .usingGeneratedKeyColumns(ColumnLabel.ID.getColumnName());
        certificate.setId(simpleJdbcInsert.executeAndReturnKey(parameters).intValue());
    }

    private Map<String, Object> fillInCertificateParameters(GiftCertificate certificate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ColumnLabel.ID.getColumnName(), certificate.getId());
        parameters.put(ColumnLabel.CERTIFICATE_NAME.getColumnName(), certificate.getName());
        parameters.put(ColumnLabel.CERTIFICATE_DESCRIPTION.getColumnName(), certificate.getDescription());
        parameters.put(ColumnLabel.CERTIFICATE_PRICE.getColumnName(), certificate.getPrice());
        parameters.put(ColumnLabel.CERTIFICATE_CREATE_DATE.getColumnName(), certificate.getCreateDate());
        parameters.put(ColumnLabel.CERTIFICATE_LAST_UPDATE_DATE.getColumnName(), certificate.getLastUpdateDate());
        parameters.put(ColumnLabel.CERTIFICATE_DURATION.getColumnName(), certificate.getDuration());
        return parameters;
    }


    @Override
    public void removeCertificate(GiftCertificate certificate) {
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATE, certificate.getId());
    }

    @Override
    public void updateCertificate(GiftCertificate certificate) {
        jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE, certificate.getName(),
                certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration(), certificate.getId());
    }

    @Override
    public List<GiftCertificate> getCertificates() {
        return jdbcTemplate.query(GET_CERTIFICATES_ALL, giftCertificateRowMapper);
    }

    @Override
    public GiftCertificate getCertificateById(int id) {
        return jdbcTemplate.queryForObject(GET_CERTIFICATE_BY_ID,
                new Object[]{id}, giftCertificateRowMapper);
    }

    @Override
    public List<GiftCertificate> getCertificatesByTagName(String name) {
        return jdbcTemplate.query(GET_CERTIFICATES_BY_TAG_NAME,
                new Object[]{"%" + name + "%"}, giftCertificateRowMapper);
    }

    @Override
    public List<GiftCertificate> getCertificatesByName(String name) {
        return jdbcTemplate.query(GET_CERTIFICATES_BY_NAME,
                new Object[]{"%" + name + "%"}, giftCertificateRowMapper);
    }

    @Override
    public List<GiftCertificate> getCertificatesByDescription(String description) {
        return jdbcTemplate.query(GET_CERTIFICATES_BY_DESCRIPTION,
                new Object[]{"%" + description + "%"}, giftCertificateRowMapper);
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByDateAscending() {
        return jdbcTemplate.query(GET_CERTIFICATES_SORTED_BY_DATE_ASCENDING,
                giftCertificateRowMapper);
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByDateDescending() {
        return jdbcTemplate.query(GET_CERTIFICATES_SORTED_BY_DATE_DESCENDING,
                giftCertificateRowMapper);
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByNameAscending() {
        return jdbcTemplate.query(GET_CERTIFICATES_SORTED_BY_NAME_ASCENDING,
                giftCertificateRowMapper);
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByNameDescending() {
        return jdbcTemplate.query(GET_CERTIFICATES_SORTED_BY_NAME_DESCENDING,
                giftCertificateRowMapper);
    }

    private enum GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
        INSTANCE;

        @Override
        public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setId(rs.getInt(ColumnLabel.ID.getColumnName()));
            giftCertificate.setName(rs.getString(ColumnLabel.CERTIFICATE_NAME.getColumnName()));
            giftCertificate.setDescription(rs.getString(ColumnLabel.CERTIFICATE_DESCRIPTION.getColumnName()));
            giftCertificate.setPrice(rs.getBigDecimal(ColumnLabel.CERTIFICATE_PRICE.getColumnName()));
            giftCertificate.setCreateDate(rs.getTimestamp(ColumnLabel.CERTIFICATE_CREATE_DATE.getColumnName()));
            giftCertificate.setLastUpdateDate(rs.getTimestamp(ColumnLabel.CERTIFICATE_LAST_UPDATE_DATE.getColumnName()));
            giftCertificate.setDuration(rs.getInt(ColumnLabel.CERTIFICATE_DURATION.getColumnName()));
            return giftCertificate;
        }
    }
}
