package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateDAO certificateDAO;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO certificateDAO) {
        this.certificateDAO = certificateDAO;
    }

    @Override
    public void addCertificate(GiftCertificate certificate) {
        certificateDAO.addCertificate(certificate);
    }

    @Override
    public void removeCertificate(GiftCertificate certificate) {
        certificateDAO.removeCertificate(certificate);
    }

    @Override
    public void updateCertificate(GiftCertificate certificate) {
        certificateDAO.updateCertificate(certificate);
    }

    @Override
    public List<GiftCertificate> getCertificates() {
        return certificateDAO.getCertificates();
    }

    @Override
    public GiftCertificate getCertificateById(int id) {
        return certificateDAO.getCertificateById(id);
    }

    @Override
    public void addTagToCertificate(int certificateId, Tag tag) {
        certificateDAO.addTagToCertificate(certificateId, tag);
    }

    @Override
    public void removeTagFromCertificate(int certificateId, Tag tag) {
        certificateDAO.removeTagFromCertificate(certificateId, tag);
    }

    @Override
    public List<GiftCertificate> getCertificatesByTagName(String name) {
        return certificateDAO.getCertificatesByTagName(name);
    }

    @Override
    public List<GiftCertificate> getCertificatesByName(String name) {
        return certificateDAO.getCertificatesByName(name);
    }

    @Override
    public List<GiftCertificate> getCertificatesByDescription(String description) {
        return certificateDAO.getCertificatesByDescription(description);
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByDateAscending() {
        return certificateDAO.getCertificatesSortedByDateAscending();
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByDateDescending() {
        return certificateDAO.getCertificatesSortedByDateDescending();
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByNameAscending() {
        return certificateDAO.getCertificatesSortedByNameAscending();
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByNameDescending() {
        return certificateDAO.getCertificatesSortedByNameDescending();
    }
}
