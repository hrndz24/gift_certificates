package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService {

    GiftCertificateDTO addCertificate(GiftCertificateDTO giftCertificateDTO);

    void removeCertificate(int certificateId);

    void updateCertificate(int id, GiftCertificateDTO certificateDTO);

    void updateCertificateField(int id, Map<String, Object> fields);

    List<GiftCertificateDTO> getCertificates(Map<String, String> params);

    GiftCertificateDTO getCertificateById(int id);

    long getCount(Map<String, String> params);
}
