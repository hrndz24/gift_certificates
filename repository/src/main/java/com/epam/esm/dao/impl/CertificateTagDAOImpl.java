package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateTagDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.ExceptionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CertificateTagDAOImpl implements CertificateTagDAO {

    private JdbcTemplate jdbcTemplate;

    private static final String ADD_TAG_TO_CERTIFICATE =
            "INSERT INTO certificate_has_tag(certificate_id, tag_id) VALUES (?, ?)";
    private static final String REMOVE_TAG_FROM_CERTIFICATE =
            "DELETE FROM certificate_has_tag WHERE certificate_id = ? AND tag_id = ?";

    @Autowired
    public CertificateTagDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addTagToCertificate(int certificateId, int tagId) {
        try {
            jdbcTemplate.update(ADD_TAG_TO_CERTIFICATE, certificateId, tagId);
        } catch (DataAccessException e) {
            throw new DAOException(ExceptionMessage.FAILED_ADD_TAG_TO_CERTIFICATE.getErrorCode(), e);
        }
    }

    @Override
    public void removeTagFromCertificate(int certificateId, int tagId) {
        try {
            jdbcTemplate.update(REMOVE_TAG_FROM_CERTIFICATE, certificateId, tagId);
        } catch (DataAccessException e) {
            throw new DAOException(ExceptionMessage.FAILED_REMOVE_TAG_FROM_CERTIFICATE.getErrorCode(), e);
        }
    }
}
