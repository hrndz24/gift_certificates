package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateTagDAO;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.utils.GiftCertificateQueryGenerator;
import com.epam.esm.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GiftCertificateServiceImplTest {

    @InjectMocks
    private GiftCertificateServiceImpl certificateService;
    @Mock
    private GiftCertificateDAO certificateDAO;
    @Mock
    private CertificateTagDAO certificateTagDAO;
    @Mock
    private TagDAO tagDAO;
    @Spy
    private Validator validator = new Validator();
    @Spy
    private GiftCertificateQueryGenerator giftCertificateQueryGenerator = new GiftCertificateQueryGenerator();
    @Spy
    private GiftCertificateMapper certificateMapper = new GiftCertificateMapper(new ModelMapper());
    @Spy
    private TagMapper tagMapper = new TagMapper(new ModelMapper());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCertificateWithValidParamsShouldAddCertificate() {
        GiftCertificate certificateReturned = new GiftCertificate();
        certificateReturned.setId(24);
        when(certificateDAO.addCertificate(any())).thenReturn(certificateReturned);
        GiftCertificateDTO certificateDTOToAdd = new GiftCertificateDTO();
        doNothing().when(validator).validateCertificate(certificateDTOToAdd);
        assertEquals(24, certificateService.addCertificate(certificateDTOToAdd).getId());
        assertNotNull(certificateDTOToAdd.getCreateDate());
        assertNotNull(certificateDTOToAdd.getLastUpdateDate());
    }

    @Test
    void addCertificateWithInvalidDataShouldThrowException() {
        GiftCertificateDTO certificateDTOToAdd = new GiftCertificateDTO();
        assertThrows(ValidatorException.class, () -> certificateService.addCertificate(certificateDTOToAdd));
    }

    @Test
    void removeCertificateShouldRemoveCertificate() {
        when(certificateDAO.getCertificateById(1)).thenReturn(new GiftCertificate());
        doNothing().when(certificateDAO).removeCertificate(1);
        certificateService.removeCertificate(1);
    }

    @Test
    void updateCertificateShouldUpdateCertificate() {
        GiftCertificate certificateToUpdate = new GiftCertificate();
        GiftCertificateDTO certificateDTOToUpdate = new GiftCertificateDTO();
        doNothing().when(validator).validateCertificate(certificateDTOToUpdate);
        doNothing().when(certificateDAO).updateCertificate(certificateToUpdate);
        when(certificateDAO.getCertificateById(2)).thenReturn(new GiftCertificate());
        certificateService.updateCertificate(2, certificateDTOToUpdate);
        assertEquals(2, certificateDTOToUpdate.getId());
        assertNotNull(certificateDTOToUpdate.getLastUpdateDate());
    }

    @Test
    void getCertificatesShouldReturnListOfThreeCertificates() {
        List<GiftCertificate> certificates = new ArrayList<>();
        certificates.add(new GiftCertificate());
        certificates.add(new GiftCertificate());
        certificates.add(new GiftCertificate());
        String queryConditionToGetAllTags = "";
        when(certificateDAO.getCertificates(queryConditionToGetAllTags)).thenReturn(certificates);
        assertEquals(3, certificateService.getCertificates(anyMap()).size());
    }

    @Test
    void getCertificateByIdWithExistingIdShouldReturnCertificate() {
        GiftCertificate certificateReturned = new GiftCertificate();
        certificateReturned.setId(2);
        when(certificateDAO.getCertificateById(2)).thenReturn(certificateReturned);
        GiftCertificateDTO certificateDTOReturned = new GiftCertificateDTO();
        certificateDTOReturned.setId(2);
        assertEquals(certificateDTOReturned, certificateService.getCertificateById(2));
    }

    @Test
    void getCertificateByIdWithNonExistingIdShouldThrowException() {
        when(certificateDAO.getCertificateById(24)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> certificateService.getCertificateById(24));
    }

}
