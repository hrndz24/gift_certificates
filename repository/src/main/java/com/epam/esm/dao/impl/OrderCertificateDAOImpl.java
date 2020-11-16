package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderCertificateDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class OrderCertificateDAOImpl implements OrderCertificateDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderCertificateDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addCertificateToOrder(int orderId, int certificateId) {
        try {
            jdbcTemplate.update(SQLQuery.ADD_CERTIFICATE_TO_ORDER.getQuery(), orderId, certificateId);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_ADD_CERTIFICATE_TO_ORDER.getErrorCode(), e);
        }
    }

    @Override
    public boolean isCertificateAssignedToOrder(int orderId, int certificateId) {
        try {
            int certificateAssignmentsCount = Objects.requireNonNull(
                    jdbcTemplate.queryForObject(SQLQuery.IS_CERTIFICATE_ASSIGNED_TO_ORDER.getQuery(),
                            new Object[]{orderId, certificateId}, Integer.class));
            return certificateAssignmentsCount > 0;
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.ERROR_DURING_FINDING_ASSIGNED_CERTIFICATE.getErrorCode(), e);
        }
    }
}
