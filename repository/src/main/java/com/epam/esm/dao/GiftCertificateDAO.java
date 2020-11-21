package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public interface GiftCertificateDAO {

    GiftCertificate addCertificate(GiftCertificate certificate);

    void removeCertificate(int certificateId);

    void updateCertificate(GiftCertificate certificate);

    List<GiftCertificate> getCertificates(CriteriaQuery<GiftCertificate> criteriaQuery);

    GiftCertificate getCertificateById(int id);
}
