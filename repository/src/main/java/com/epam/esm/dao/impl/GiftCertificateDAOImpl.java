package com.epam.esm.dao.impl;

import com.epam.esm.dao.ColumnLabel;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private JdbcTemplate jdbcTemplate;

    private GiftCertificateResultSetExtractor giftCertificateResultSetExtractor =
            GiftCertificateResultSetExtractor.INSTANCE;

    private static final String DELETE_GIFT_CERTIFICATE =
            "DELETE FROM gift_certificates WHERE id = ?";
    private static final String UPDATE_GIFT_CERTIFICATE =
            "UPDATE gift_certificates SET name = ?, description = ?, price = ?," +
                    " last_update_date = ?, duration = ? WHERE id = ?";
    private static final String ADD_TAG_TO_CERTIFICATE =
            "INSERT INTO certificate_tags(certificate_id, tag_id) VALUES (?, ?)";
    private static final String REMOVE_TAG_FROM_CERTIFICATE =
            "DELETE FROM certificate_tags WHERE certificate_id = ? AND tag_id = ?";

    private static final String ALL_FIELDS =
            "gc.id id, gc.name name, description, price, create_date, " +
                    "last_update_date, duration, tag_id, tags.name as tag_name";
    private static final String JOIN_TAGS =
            " FROM gift_certificates gc LEFT JOIN certificate_tags ct " +
                    "ON gc.id = ct.certificate_id " +
                    "LEFT JOIN tags ON ct.tag_id = tags.id";

    private static final String GET_CERTIFICATES_ALL =
            "SELECT " + ALL_FIELDS + JOIN_TAGS;
    private static final String GET_CERTIFICATE_BY_ID =
            "SELECT " + ALL_FIELDS + JOIN_TAGS + " WHERE gc.id = ?";
    private static final String GET_CERTIFICATES_BY_NAME =
            "SELECT " + ALL_FIELDS + JOIN_TAGS + " WHERE gc.name LIKE ?";
    private static final String GET_CERTIFICATES_BY_DESCRIPTION =
            "SELECT " + ALL_FIELDS + JOIN_TAGS + " WHERE description LIKE ?";
    private static final String GET_CERTIFICATES_BY_TAG_NAME =
            "SELECT " + ALL_FIELDS + " FROM gift_certificates AS gc JOIN " +
                    "(SELECT certificate_tags.certificate_id FROM certificate_tags " +
                    "WHERE certificate_tags.tag_id =\n" +
                    "(SELECT id from tags WHERE tags.name LIKE ?)) AS ct " +
                    "ON gc.id = ct.certificate_id LEFT JOIN certificate_tags as cct \n" +
                    "ON gc.id = cct.certificate_id LEFT JOIN tags ON cct.tag_id = tags.id";
    private static final String GET_CERTIFICATES_SORTED_BY_DATE_ASCENDING =
            "SELECT " + ALL_FIELDS + JOIN_TAGS + " ORDER BY create_date";
    private static final String GET_CERTIFICATES_SORTED_BY_DATE_DESCENDING =
            "SELECT " + ALL_FIELDS + JOIN_TAGS + " ORDER BY create_date DESC";
    private static final String GET_CERTIFICATES_SORTED_BY_NAME_ASCENDING =
            "SELECT " + ALL_FIELDS + JOIN_TAGS + " ORDER BY gc.name";
    private static final String GET_CERTIFICATES_SORTED_BY_NAME_DESCENDING =
            "SELECT " + ALL_FIELDS + JOIN_TAGS + " ORDER BY gc.name DESC";
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
        try {
            certificate.setId(simpleJdbcInsert.executeAndReturnKey(parameters).intValue());
        } catch (DataAccessException e) {
            throw new DAOException("Failed to add certificate to the database", e);
        }
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
        try {
            jdbcTemplate.update(DELETE_GIFT_CERTIFICATE, certificate.getId());
        } catch (DataAccessException e) {
            throw new DAOException("Failed to remove certificate from the database", e);
        }
    }

    @Override
    public void updateCertificate(GiftCertificate certificate) {
        try {
            jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE, certificate.getName(),
                    certificate.getDescription(), certificate.getPrice(),
                    certificate.getLastUpdateDate(), certificate.getDuration(),
                    certificate.getId());
        } catch (DataAccessException e) {
            throw new DAOException("Failed to update certificate in the database", e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificates() {
        try {
            return jdbcTemplate.query(GET_CERTIFICATES_ALL, giftCertificateResultSetExtractor);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get certificates from the database", e);
        }
    }

    @Override
    public GiftCertificate getCertificateById(int id) {
        try {
            return jdbcTemplate.query(GET_CERTIFICATE_BY_ID, rs -> {
                GiftCertificate certificate = null;
                while (rs.next()) {
                    if (certificate == null) {
                        certificate = giftCertificateResultSetExtractor.mapCertificate(rs);
                    }
                    certificate.addTag(giftCertificateResultSetExtractor.mapTag(rs));
                }
                return certificate;
            }, id);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get certificate by id from the database", e);
        }
    }

    @Override
    public void addTagToCertificate(int certificateId, int tagId) {
        try {
            jdbcTemplate.update(ADD_TAG_TO_CERTIFICATE, certificateId, tagId);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to add tag to certificate in the database", e);
        }
    }

    @Override
    public void removeTagFromCertificate(int certificateId, int tagId) {
        try {
            jdbcTemplate.update(REMOVE_TAG_FROM_CERTIFICATE, certificateId, tagId);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to remove tag from certificate in the database", e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificatesByTagName(String name) {
        try {
            return jdbcTemplate.query(GET_CERTIFICATES_BY_TAG_NAME,
                    new Object[]{"%" + name + "%"}, giftCertificateResultSetExtractor);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get certificates by tag name from the database", e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificatesByName(String name) {
        try {
            return jdbcTemplate.query(GET_CERTIFICATES_BY_NAME,
                    new Object[]{"%" + name + "%"}, giftCertificateResultSetExtractor);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get certificates by name from the database", e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificatesByDescription(String description) {
        try {
            return jdbcTemplate.query(GET_CERTIFICATES_BY_DESCRIPTION,
                    new Object[]{"%" + description + "%"}, giftCertificateResultSetExtractor);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get certificates by description from the database", e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByDateAscending() {
        try {
            return jdbcTemplate.query(GET_CERTIFICATES_SORTED_BY_DATE_ASCENDING,
                    giftCertificateResultSetExtractor);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get certificates sorted by date from the database", e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByDateDescending() {
        try {
            return jdbcTemplate.query(GET_CERTIFICATES_SORTED_BY_DATE_DESCENDING,
                    giftCertificateResultSetExtractor);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get certificates sorted by date descending from the database", e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByNameAscending() {
        try {
            return jdbcTemplate.query(GET_CERTIFICATES_SORTED_BY_NAME_ASCENDING,
                    giftCertificateResultSetExtractor);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get certificates sorted by name from the database", e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByNameDescending() {
        try {
            return jdbcTemplate.query(GET_CERTIFICATES_SORTED_BY_NAME_DESCENDING,
                    giftCertificateResultSetExtractor);
        } catch (DataAccessException e) {
            throw new DAOException("Failed to get certificates sorted by name descending from the database", e);
        }
    }

    private enum GiftCertificateResultSetExtractor implements ResultSetExtractor<List<GiftCertificate>> {
        INSTANCE;

        @Override
        public List<GiftCertificate> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<GiftCertificate> certificates = new ArrayList<>();
            GiftCertificate currentCertificate = null;
            while (rs.next()) {
                int id = rs.getInt(ColumnLabel.ID.getColumnName());
                if (currentCertificate == null) {
                    currentCertificate = mapCertificate(rs);
                } else if (currentCertificate.getId() != id) {
                    certificates.add(currentCertificate);
                    currentCertificate = mapCertificate(rs);
                }
                currentCertificate.addTag(mapTag(rs));
            }
            if (currentCertificate != null) {
                certificates.add(currentCertificate);
            }
            return certificates;
        }

        GiftCertificate mapCertificate(ResultSet rs) throws SQLException {
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

        Tag mapTag(ResultSet rs) throws SQLException {
            Tag tag = new Tag();
            tag.setId(rs.getInt(ColumnLabel.TAG_ID.getColumnName()));
            tag.setName(rs.getString(ColumnLabel.TAG_NAME.getColumnName()));
            return tag;
        }
    }
}
