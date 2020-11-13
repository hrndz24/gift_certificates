package com.epam.esm.dao.impl;

import com.epam.esm.dao.ColumnLabel;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.utils.EntityRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private JdbcTemplate jdbcTemplate;

    private GiftCertificateResultSetExtractor giftCertificateResultSetExtractor =
            GiftCertificateResultSetExtractor.INSTANCE;

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
            throw new DAOException(DAOExceptionCode.FAILED_ADD_CERTIFICATE.getErrorCode(), e);
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
            jdbcTemplate.update(SQLQuery.DELETE_GIFT_CERTIFICATE.getQuery(), certificateId);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_REMOVE_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public void updateCertificate(GiftCertificate certificate) {
        try {
            jdbcTemplate.update(SQLQuery.UPDATE_GIFT_CERTIFICATE.getQuery(), certificate.getName(),
                    certificate.getDescription(), certificate.getPrice(),
                    certificate.getLastUpdateDate(), certificate.getDuration(),
                    certificate.getId());
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_UPDATE_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public List<GiftCertificate> getCertificates(String queryCondition) {
        try {
            return jdbcTemplate.query(SQLQuery.GET_CERTIFICATES.getQuery() + queryCondition,
                    giftCertificateResultSetExtractor);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_CERTIFICATES.getErrorCode(), e);
        }
    }

    @Override
    public GiftCertificate getCertificateById(int id) {
        try {
            return Objects.requireNonNull(jdbcTemplate.query(SQLQuery.GET_CERTIFICATE_BY_ID.getQuery(),
                    giftCertificateResultSetExtractor, id)).stream().findFirst().orElse(null);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_CERTIFICATE.getErrorCode(), e);
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
