INSERT INTO tags(name)
VALUES ('entertainment'),
       ('geek'),
       ('sport');

INSERT INTO gift_certificates(name, description, price, create_date, last_update_date, duration)
VALUES ('Disney Land', 'Needs no description cuz it is Disney Land', 12.00, now(), now(), 120),
       ('Comic con', 'About comics', 10.00, now(), now(), 35),
       ('Skiing in the Alps', 'Skiing where famous Milka lives', 34.50, now(), now(), 30);

INSERT INTO certificate_tags(certificate_id, tag_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 1);