package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.specification.Specification;

import java.util.List;

public interface GiftCertificateDAO {

    GiftCertificate addCertificate(GiftCertificate certificate);

    void removeCertificate(int certificateId);

    void updateCertificate(GiftCertificate certificate);

    List<GiftCertificate> getCertificates(List<Specification> specifications, int limit, int offset);

    GiftCertificate getCertificateById(int id);

    long getCount(List<Specification> specifications);
}
