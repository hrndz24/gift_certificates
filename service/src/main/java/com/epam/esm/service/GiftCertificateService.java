package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService {

    GiftCertificateDTO addCertificate(GiftCertificateDTO giftCertificateDTO);

    void removeCertificate(int certificateId);

    void updateCertificate(int id, GiftCertificateDTO certificateDTO);

    List<GiftCertificateDTO> getCertificates(Map<String, String> params);

    GiftCertificateDTO getCertificateById(int id);

    void addTagToCertificate(int certificateId, TagDTO tag);

    void removeTagFromCertificate(int certificateId, int tagId);
}
