INSERT INTO tag(name)
VALUES ('entertainment'),
       ('geek'),
       ('sport');

INSERT INTO gift_certificate(name, description, price, create_date, last_update_date, duration)
VALUES ('Disney Land', 'Needs no description cuz it is Disney Land', 12.00, '2020-10-27 21:17:24',
        '2020-10-27 21:17:24', 120),
       ('Comic con', 'About comics', 10.00, '2020-10-26 21:17:24', now(), 35),
       ('Skiing in the Alps', 'Skiing where famous Milka lives', 34.50, '2020-10-25 21:17:24', now(), 30);

INSERT INTO certificate_has_tag(certificate_id, tag_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 1);

INSERT INTO user(email, password)
VALUES ('pak@pak.com',
        'ea2f24301114cd3e62e01c41bfbc93f6ba49f6d8cb3a6e080db6186444fb4d283a444cb69eb28d8c6f5166408be8d12c3ea701882231807bf610e8d37aef2907'),
       ('natik@nat.com',
        '44177e93b42c1e72250c09783447337cf2607efc5f411778699d3c802352c985667efd24412c90cf548e304e9348e2e248e80ab1af3e56afd0241b4f64e8cd2c'),
       ('smb@somewhere.org',
        '89d3c17daa2466d721ec21e9ac27f0654fd1f67d701b937c6835416ad26c4c66b55fe32fdd941952eebd3b215554219c7cfdf0643bac88ebe332c7f07258a8ea');

INSERT INTO `order`(user_id, cost, date)
VALUES (1, 100.00, '2020-10-28 21:17:24'),
       (2, 378.89, '2020-04-26 21:17:24'),
       (2, 45.50, '2020-05-05 21:17:24');

INSERT INTO order_has_certificate(order_id, certificate_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (1, 2);