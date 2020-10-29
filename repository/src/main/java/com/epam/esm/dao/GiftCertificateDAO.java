package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDAO {

    void addCertificate(GiftCertificate certificate);

    void removeCertificate(GiftCertificate certificate);

    void updateCertificate(GiftCertificate certificate);

    List<GiftCertificate> getCertificates(String queryCondition);

    GiftCertificate getCertificateById(int id);

    void addTagToCertificate(int certificateId, int tagId);

    void removeTagFromCertificate(int certificateId, int tagId);
}