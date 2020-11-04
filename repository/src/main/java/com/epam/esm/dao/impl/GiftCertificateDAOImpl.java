package com.epam.esm.dao.impl;

import com.epam.esm.dao.ColumnLabel;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.utils.EntityRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private JdbcTemplate jdbcTemplate;

    private GiftCertificateResultSetExtractor giftCertificateResultSetExtractor =
            GiftCertificateResultSetExtractor.INSTANCE;

    private static final String DELETE_GIFT_CERTIFICATE =
            "DELETE FROM gift_certificate WHERE id = ?";
    private static final String UPDATE_GIFT_CERTIFICATE =
            "UPDATE gift_certificate SET name = ?, description = ?, price = ?," +
                    " last_update_date = ?, duration = ? WHERE id = ?";

    private static final String ALL_FIELDS =
            "gc.id id, gc.name name, description, price, create_date, " +
                    "last_update_date, duration, tag_id, tag.name as tag_name";
    private static final String JOIN_TAGS =
            " FROM gift_certificate gc LEFT JOIN certificate_has_tag ct " +
                    "ON gc.id = ct.certificate_id " +
                    "LEFT JOIN tag ON ct.tag_id = tag.id ";

    private static final String GET_CERTIFICATES =
            "SELECT " + ALL_FIELDS + JOIN_TAGS;
    private static final String GET_CERTIFICATE_BY_ID =
            "SELECT " + ALL_FIELDS + JOIN_TAGS + " WHERE gc.id = ?";

    private static final String GIFT_CERTIFICATE_TABLE_NAME = "gift_certificate";

    @Autowired
    public GiftCertificateDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GiftCertificate addCertificate(GiftCertificate certificate) {
        Map<String, Object> parameters = fillInCertificateParameters(certificate);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(GIFT_CERTIFICATE_TABLE_NAME)
                .usingGeneratedKeyColumns(ColumnLabel.ID.getColumnName());
        try {
            certificate.setId(simpleJdbcInsert.executeAndReturnKey(parameters).intValue());
        } catch (DataAccessException e) {
            throw new DAOException(ExceptionMessage.FAILED_ADD_CERTIFICATE.getErrorCode(), e);
        }
        return certificate;
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
    public void removeCertificate(int certificateId) {
        try {
            jdbcTemplate.update(DELETE_GIFT_CERTIFICATE, certificateId);
        } catch (DataAccessException e) {
            throw new DAOException(ExceptionMessage.FAILED_REMOVE_CERTIFICATE.getErrorCode(), e);
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
            throw new DAOException(ExceptionMessage.FAILED_UPDATE_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificates(String queryCondition) {
        try {
            return jdbcTemplate.query(GET_CERTIFICATES + queryCondition, giftCertificateResultSetExtractor);
        } catch (DataAccessException e) {
            throw new DAOException(ExceptionMessage.FAILED_GET_CERTIFICATES.getErrorCode(), e);
        }
    }

    @Override
    public GiftCertificate getCertificateById(int id) {
        try {
            return Objects.requireNonNull(jdbcTemplate.query(
                    GET_CERTIFICATE_BY_ID, giftCertificateResultSetExtractor, id)).stream().findFirst().orElse(null);
        } catch (DataAccessException e) {
            throw new DAOException(ExceptionMessage.FAILED_GET_CERTIFICATE.getErrorCode(), e);
        }
    }

    private enum GiftCertificateResultSetExtractor implements ResultSetExtractor<List<GiftCertificate>> {
        INSTANCE(new EntityRowMapper());

        private EntityRowMapper entityRowMapper;

        GiftCertificateResultSetExtractor(EntityRowMapper entityRowMapper) {
            this.entityRowMapper = entityRowMapper;
        }

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
                int tagId = rs.getInt(ColumnLabel.TAG_ID.getColumnName());
                if (tagId != 0) {
                    currentCertificate.addTag(mapTag(rs));
                }
            }
            if (currentCertificate != null) {
                certificates.add(currentCertificate);
            }
            return certificates;
        }

        private GiftCertificate mapCertificate(ResultSet rs) throws SQLException {
            return entityRowMapper.mapCertificateFields(rs);
        }

        private Tag mapTag(ResultSet rs) throws SQLException {
            return entityRowMapper.mapTagFields(rs);
        }
    }
}
