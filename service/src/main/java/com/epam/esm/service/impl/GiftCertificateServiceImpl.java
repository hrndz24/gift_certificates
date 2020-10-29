package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateDAO certificateDAO;
    private Validator validator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO certificateDAO, Validator validator) {
        this.certificateDAO = certificateDAO;
        this.validator = validator;
    }

    @Override
    public void addCertificate(GiftCertificate certificate) {
        validator.validateCertificate(certificate);
        certificateDAO.addCertificate(certificate);
    }

    @Override
    public void removeCertificate(GiftCertificate certificate) {
        certificateDAO.removeCertificate(certificate);
    }

    @Override
    public void updateCertificate(GiftCertificate certificate) {
        validator.validateCertificate(certificate);
        certificate.setLastUpdateDate(new Date());
        certificateDAO.updateCertificate(certificate);
    }

    @Override
    public List<GiftCertificate> getCertificates(Map<String, String> params) {
        return certificateDAO.getCertificates();
    }

    @Override
    public GiftCertificate getCertificateById(int id) {
        return certificateDAO.getCertificateById(id);
    }

    @Override
    public void addTagToCertificate(int certificateId, int tagId) {
        certificateDAO.addTagToCertificate(certificateId, tagId);
    }

    @Override
    public void removeTagFromCertificate(int certificateId, int tagId) {
        certificateDAO.removeTagFromCertificate(certificateId, tagId);
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
