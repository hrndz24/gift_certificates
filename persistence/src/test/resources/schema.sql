CREATE TABLE tags
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE
);

CREATE TABLE gift_certificates
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    name             VARCHAR(100),
    description      VARCHAR(200),
    price            DECIMAL(11, 2),
    create_date      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    duration         INT
);

CREATE TABLE certificate_tags
(
    certificate_id INT,
    tag_id         INT,
    PRIMARY KEY (certificate_id, tag_id),
    FOREIGN KEY (tag_id) REFERENCES tags (id),
    FOREIGN KEY (certificate_id) REFERENCES gift_certificates (id)
);

/*SET FOREIGN_KEY_CHECKS=0; */