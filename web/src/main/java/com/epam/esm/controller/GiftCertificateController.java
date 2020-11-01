package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
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
@RequestMapping(value = "/api/v1/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
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
     * @param id id of the certificate to update
     * @param certificate updated certificate information
     */
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateCertificate(@PathVariable int id, @RequestBody GiftCertificateDTO certificate) {
        certificateService.updateCertificate(id, certificate);
    }

    /**
     * Deletes {@code GiftCertificate} with the specified id.
     *
     * @param id id of the certificate to be deleted
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
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
    @GetMapping("{id}")
    public GiftCertificateDTO getCertificate(@PathVariable("id") int id) {
        return certificateService.getCertificateById(id);
    }

    /**
     * Adds a tag to certificate with the specified id.
     * If a tag doesn't exist it will be creates and
     * added to the database.
     *
     * @param id if of the modified certificate
     * @param tag tag to add to the certificate
     * @return modified GiftCertificate
     */
    @PostMapping(value = "/{id}/tags", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDTO addTagToCertificate(@PathVariable("id") int id, @RequestBody TagDTO tag) {
        certificateService.addTagToCertificate(id, tag);
        return certificateService.getCertificateById(id);
    }

    /**
     * Removes tag with requested id from certificate with specified id.
     *
     * @param certificateId id of the certificate to remove tag from
     * @param tagId id of the tag to be removed
     */
    @DeleteMapping("{certificateId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTagFromCertificate(@PathVariable("certificateId") int certificateId,
                                         @PathVariable("tagId") int tagId) {
        certificateService.removeTagFromCertificate(certificateId, tagId);
    }
}
