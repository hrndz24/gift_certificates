package com.epam.esm.dao;

public interface CertificateTagDAO {

    void addTagToCertificate(int certificateId, int tagId);

    void removeTagFromCertificate(int certificateId, int tagId);
}
