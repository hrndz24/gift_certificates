INSERT INTO tag(name)
VALUES ('entertainment'),
       ('geek'),
       ('sport');

INSERT INTO gift_certificate(name, description, price, create_date, last_update_date, duration)
VALUES ('Disney Land', 'Needs no description cuz it is Disney Land', 12.00, '2020-10-27 21:17:24', '2020-10-27 21:17:24', 120),
       ('Comic con', 'About comics', 10.00, '2020-10-26 21:17:24', now(), 35),
       ('Skiing in the Alps', 'Skiing where famous Milka lives', 34.50, '2020-10-25 21:17:24', now(), 30);

INSERT INTO certificate_has_tag(certificate_id, tag_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 1);