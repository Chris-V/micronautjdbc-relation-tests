INSERT INTO `account` (`id`, `name`)
VALUES (1, "Foo"), (2, "Bar"), (3, "Baz"), (4, "some_account");

INSERT INTO `session_a` (`id`, `fk_account_id`)
VALUES (1, 4), (2, 3), (3, 2);

INSERT INTO `session_b` (`id`, `account_id`)
VALUES (1, 4), (2, 3), (3, 2);
