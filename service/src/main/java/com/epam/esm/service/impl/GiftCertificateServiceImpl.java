package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.utils.QueryGenerator;
import com.epam.esm.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateDAO certificateDAO;
    private Validator validator;
    private QueryGenerator queryGenerator;
    private GiftCertificateMapper mapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO certificateDAO,
                                      Validator validator,
                                      QueryGenerator queryGenerator,
                                      GiftCertificateMapper mapper) {
        this.certificateDAO = certificateDAO;
        this.validator = validator;
        this.queryGenerator = queryGenerator;
        this.mapper = mapper;
    }

    @Override
    public void addCertificate(GiftCertificateDTO certificateDTO) {
        validator.validateCertificate(certificateDTO);
        certificateDAO.addCertificate(mapper.toModel(certificateDTO));
    }

    @Override
    public void removeCertificate(int certificateId) {
        certificateDAO.removeCertificate(certificateId);
    }

    @Override
    public void updateCertificate(GiftCertificateDTO certificateDTO) {
        validator.validateCertificate(certificateDTO);
        certificateDTO.setLastUpdateDate(new Date());
        certificateDAO.updateCertificate(mapper.toModel(certificateDTO));
    }

    @Override
    public List<GiftCertificateDTO> getCertificates(Map<String, String> params) {
        validator.validateParams(params);
        String query = queryGenerator.generateQuery(params);
        List<GiftCertificateDTO> certificates = new ArrayList<>();
        certificateDAO.getCertificates(query).forEach(giftCertificate -> {
            certificates.add(mapper.toDTO(giftCertificate));
        });
        return certificates;
    }

    @Override
    public GiftCertificateDTO getCertificateById(int id) {
        return mapper.toDTO(certificateDAO.getCertificateById(id));
    }

    @Override
    public void addTagToCertificate(int certificateId, int tagId) {
        certificateDAO.addTagToCertificate(certificateId, tagId);
    }

    @Override
    public void removeTagFromCertificate(int certificateId, int tagId) {
        certificateDAO.removeTagFromCertificate(certificateId, tagId);
    }
}
