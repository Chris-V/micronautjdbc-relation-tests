DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
    `id`   INTEGER      NOT NULL
        PRIMARY KEY,
    `name` VARCHAR(200) NOT NULL
);

DROP TABLE IF EXISTS `session_a`;
CREATE TABLE `session_a` (
    `id`                 INTEGER NOT NULL
        PRIMARY KEY,
    `fk_account_id` INTEGER NOT NULL
        REFERENCES `account`(`id`)
);

DROP TABLE IF EXISTS `session_b`;
CREATE TABLE `session_b` (
    `id`                 INTEGER NOT NULL
        PRIMARY KEY,
    `account_id` INTEGER NOT NULL
        REFERENCES `account`(`id`)
);
