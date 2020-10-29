package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/certificates", produces = "application/json")
public class GiftCertificateController {

    private GiftCertificateService certificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("/")
    public void createCertificate(@RequestBody GiftCertificateDTO certificate) {
        certificateService.addCertificate(certificate);
    }

    @PutMapping("/")
    public void updateCertificate(@RequestBody GiftCertificateDTO certificate) {
        certificateService.updateCertificate(certificate);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteCertificate(@PathVariable("id") int id) {
        certificateService.removeCertificate(id);
        return HttpStatus.OK;
    }

    @GetMapping
    public List<GiftCertificateDTO> getCertificates(@RequestParam Map<String, String> params) {
        return certificateService.getCertificates(params);
    }

    @GetMapping("/{id}")
    public GiftCertificateDTO getCertificate(@PathVariable("id") int id) {
        return certificateService.getCertificateById(id);
    }
}
