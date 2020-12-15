package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

public class GiftCertificatesDTO extends RepresentationModel<GiftCertificatesDTO> {

    private List<GiftCertificateDTO> certificates;

    public GiftCertificatesDTO() {
    }

    public GiftCertificatesDTO(List<GiftCertificateDTO> certificates) {
        this.certificates = certificates;
    }

    public List<GiftCertificateDTO> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<GiftCertificateDTO> certificates) {
        this.certificates = certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GiftCertificatesDTO that = (GiftCertificatesDTO) o;
        return Objects.equals(certificates, that.certificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), certificates);
    }

    @Override
    public String toString() {
        return "GiftCertificatesDTO{" +
                "certificates=" + certificates +
                '}';
    }
}
