package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService {

    void addCertificate(GiftCertificateDTO giftCertificateDTO);

    void removeCertificate(GiftCertificateDTO certificateDTO);

    void updateCertificate(GiftCertificateDTO certificateDTO);

    List<GiftCertificateDTO> getCertificates(Map<String, String> params);

    GiftCertificateDTO getCertificateById(int id);

    void addTagToCertificate(int certificateId, int tagId);

    void removeTagFromCertificate(int certificateId, int tagId);
}
