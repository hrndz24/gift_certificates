package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.ControllerPaginationPreparer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller used to manipulate CRUD operations on
 * {@code GiftCertificate} data
 */
@RestController
@RequestMapping(value = "/api/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {

    private GiftCertificateService certificateService;
    private ControllerPaginationPreparer paginationPreparer;

    @Autowired
    public GiftCertificateController(GiftCertificateService certificateService,
                                     ControllerPaginationPreparer paginationPreparer) {
        this.certificateService = certificateService;
        this.paginationPreparer = paginationPreparer;
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
    public ResponseEntity<Void> updateCertificate(@PathVariable("id") int id, @RequestBody GiftCertificateDTO certificate) {
        certificateService.updateCertificate(id, certificate);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<Void> deleteCertificate(@PathVariable("id") int id) {
        certificateService.removeCertificate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
    public RepresentationModel<?> getCertificates(@RequestParam Map<String, String> params) {
        List<GiftCertificateDTO> certificates = certificateService.getCertificates(params);
        certificates.forEach(certificate -> {
            certificate.add(linkTo(methodOn(GiftCertificateController.class)
                    .getCertificateById(certificate.getId()))
                    .withSelfRel());
        });
        int currentPage = Integer.parseInt(params.get("page"));
        long certificatesCount = certificateService.getCount(params);
        List<Link> links = paginationPreparer.prepareLinks(
                methodOn(GiftCertificateController.class).getCertificates(params),
                params, currentPage, certificatesCount);
        params.put("orderBy", "name");
        links.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificates(params))
                .withRel("sort by name asc"));
        params.put("orderBy", "-name");
        links.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificates(params))
                .withRel("sort by name desc"));
        params.put("orderBy", "date");
        links.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificates(params))
                .withRel("sort by creation date asc"));
        params.put("orderBy", "-date");
        links.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificates(params))
                .withRel("sort by creation date desc"));
        params.put("certificateName", "name");
        links.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificates(params))
                .withRel("search by name of certificate"));
        params.put("certificateDescription", "description");
        links.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificates(params))
                .withRel("search by description of certificate"));
        Map<String, Long> page = paginationPreparer.preparePageInfo(params, currentPage, certificatesCount);
        CollectionModel<GiftCertificateDTO> collectionModel = CollectionModel.of(certificates);
        return HalModelBuilder.halModelOf(collectionModel).links(links).embed(page, LinkRelation.of("page")).build();
    }

    /**
     * Returns {@code GiftCertificate} with the requested id
     *
     * @param id id of the requested certificate
     * @return GiftCertificateDTO with the requested id
     */
    @GetMapping("/{id}")
    public GiftCertificateDTO getCertificateById(@PathVariable("id") int id) {
        GiftCertificateDTO certificate = certificateService.getCertificateById(id);
        certificate.getTags().forEach(tag -> {
            tag.add(linkTo(methodOn(TagController.class)
                    .getTagById(tag.getId()))
                    .withSelfRel());
        });
        certificate.add(linkTo(methodOn(GiftCertificateController.class)
                .deleteCertificate(id))
                .withRel("delete"));
        certificate.add(linkTo(methodOn(GiftCertificateController.class)
                .updateCertificate(id, certificate))
                .withRel("update"));
        return certificate;
    }
}
