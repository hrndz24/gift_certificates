package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDAO {

    void addCertificate(GiftCertificate certificate);

    void removeCertificate(GiftCertificate certificate);

    void updateCertificate(GiftCertificate certificate);

    List<GiftCertificate> getCertificates();

    GiftCertificate getCertificateById(int id);

    List<GiftCertificate> getCertificatesByTagName(String name);

    List<GiftCertificate> getCertificatesByName(String name);

    List<GiftCertificate> getCertificatesByDescription(String description);

    List<GiftCertificate> getCertificatesSortedByDateAscending();

    List<GiftCertificate> getCertificatesSortedByDateDescending();

    List<GiftCertificate> getCertificatesSortedByNameAscending();

    List<GiftCertificate> getCertificatesSortedByNameDescending();
}