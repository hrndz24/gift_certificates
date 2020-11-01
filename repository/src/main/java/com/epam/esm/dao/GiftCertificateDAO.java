package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateDAO {

    GiftCertificate addCertificate(GiftCertificate certificate);

    void removeCertificate(int certificateId);

    void updateCertificate(GiftCertificate certificate);

    List<GiftCertificate> getCertificates(String queryCondition);

    GiftCertificate getCertificateById(int id);

    void addTagToCertificate(int certificateId, int tagId);

    void removeTagFromCertificate(int certificateId, int tagId);
}