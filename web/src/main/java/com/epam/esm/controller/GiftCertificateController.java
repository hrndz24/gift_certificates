package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller used to manipulate CRUD operations on
 * {@code GiftCertificate} data
 */
@RestController
@RequestMapping(value = "/api/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {

    private GiftCertificateService certificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /**
     * Creates new {@code GiftCertificate} in the database.
     * If new tags are passed during creation, they are
     * added to the database as well.
     *
     * @param certificate GiftCertificate to be created
     * @return GiftCertificateDTO corresponding to the created GiftCertificate
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDTO createCertificate(@RequestBody GiftCertificateDTO certificate) {
        return certificateService.addCertificate(certificate);
    }

    /**
     * Updates information about {@code GiftCertificate} with requested id
     * or creates new one if it doesn't exist.
     * All certificate fields should be passed in the request body.
     *
     * @param id          id of the certificate to update
     * @param certificate updated certificate information
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateCertificate(@PathVariable("id") int id, @RequestBody GiftCertificateDTO certificate) {
        certificateService.updateCertificate(id, certificate);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateCertificateFields(@PathVariable("id") int id, @RequestBody Map<String, Object> fields) {
        certificateService.updateCertificateField(id, fields);
    }

    /**
     * Deletes {@code GiftCertificate} with the specified id.
     *
     * @param id id of the certificate to be deleted
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable("id") int id) {
        certificateService.removeCertificate(id);
    }

    /**
     * Returns list of {@code GiftCertificateDTO} that match requirements
     * of the parameters. Currently it supports searching by 'certificateName',
     * 'certificateDescription', 'tagName' and sorting by 'name' and 'date'.
     * To sort descending add '-' before sort parameter(e.g. -name).
     * To get all certificates don't send any parameters.
     *
     * @param params search or sort parameters
     * @return list of certificates that match requirements of the parameters
     */
    @GetMapping
    public List<GiftCertificateDTO> getCertificates(@RequestParam Map<String, String> params) {
        return certificateService.getCertificates(params);
    }

    /**
     * Returns {@code GiftCertificate} with the requested id
     *
     * @param id id of the requested certificate
     * @return GiftCertificateDTO with the requested id
     */
    @GetMapping("/{id}")
    public GiftCertificateDTO getCertificateById(@PathVariable("id") int id) {
        return certificateService.getCertificateById(id);
    }
}
