package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;

public interface GiftCertificateDAO {

    void addCertificate(GiftCertificate certificate);

    void removeCertificate(GiftCertificate certificate);

    void updateCertificate(GiftCertificate certificate);

    List<GiftCertificate> getCertificates();

    GiftCertificate getCertificateById(int id);

    void addTagToCertificate(int certificateId, Tag tag);

    void removeTagFromCertificate(int certificateId, Tag tag);

    List<GiftCertificate> getCertificatesByTagName(String name);

    List<GiftCertificate> getCertificatesByName(String name);

    List<GiftCertificate> getCertificatesByDescription(String description);

    List<GiftCertificate> getCertificatesSortedByDateAscending();

    List<GiftCertificate> getCertificatesSortedByDateDescending();

    List<GiftCertificate> getCertificatesSortedByNameAscending();

    List<GiftCertificate> getCertificatesSortedByNameDescending();
}