package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService {

    void addCertificate(GiftCertificate certificate);

    void removeCertificate(GiftCertificate certificate);

    void updateCertificate(GiftCertificate certificate);

    List<GiftCertificate> getCertificates(Map<String, String> params);

    GiftCertificate getCertificateById(int id);

    void addTagToCertificate(int certificateId, int tagId);

    void removeTagFromCertificate(int certificateId, int tagId);

    List<GiftCertificate> getCertificatesByTagName(String name);

    List<GiftCertificate> getCertificatesByName(String name);

    List<GiftCertificate> getCertificatesByDescription(String description);

    List<GiftCertificate> getCertificatesSortedByDateAscending();

    List<GiftCertificate> getCertificatesSortedByDateDescending();

    List<GiftCertificate> getCertificatesSortedByNameAscending();

    List<GiftCertificate> getCertificatesSortedByNameDescending();
}
