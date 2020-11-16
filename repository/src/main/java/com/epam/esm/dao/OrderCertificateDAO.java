package com.epam.esm.dao;

public interface OrderCertificateDAO {

    void addCertificateToOrder(int orderId, int certificateId);

    boolean isCertificateAssignedToOrder(int orderId, int certificateId);
}
