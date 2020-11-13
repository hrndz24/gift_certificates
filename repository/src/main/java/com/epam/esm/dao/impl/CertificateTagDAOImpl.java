package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateTagDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class CertificateTagDAOImpl implements CertificateTagDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateTagDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addTagToCertificate(int certificateId, int tagId) {
        try {
            jdbcTemplate.update(SQLQuery.ADD_TAG_TO_CERTIFICATE.getQuery(), certificateId, tagId);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_ADD_TAG_TO_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public void removeTagFromCertificate(int certificateId, int tagId) {
        try {
            jdbcTemplate.update(SQLQuery.REMOVE_TAG_FROM_CERTIFICATE.getQuery(), certificateId, tagId);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_REMOVE_TAG_FROM_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public boolean isTagAssignedToCertificate(int certificateId, int tagId) {
        try {
            int tagAssignmentsCount = Objects.requireNonNull(
                    jdbcTemplate.queryForObject(SQLQuery.IS_TAG_ASSIGNED_TO_CERTIFICATE.getQuery(),
                            new Object[]{tagId, certificateId}, Integer.class));
            return tagAssignmentsCount > 0;
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.ERROR_DURING_FINDING_ASSIGNED_TAGS.getErrorCode(), e);
        }
    }

    @Override
    public boolean isTagAssignedToAnyCertificate(int tagId) {
        try {
            int tagAssignmentsCount = Objects.requireNonNull(
                    jdbcTemplate.queryForObject(SQLQuery.IS_TAG_ASSIGNED.getQuery(), new Object[]{tagId}, Integer.class));
            return tagAssignmentsCount > 0;
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.ERROR_DURING_FINDING_ASSIGNED_TAGS.getErrorCode(), e);
        }
    }
}
