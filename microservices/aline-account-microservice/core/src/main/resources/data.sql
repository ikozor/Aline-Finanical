/* Populate Database with base data */
INSERT IGNORE INTO bank (id, address, city, state, zipcode, routing_number)
VALUES (1, '123 Aline Financial St.', 'New York', 'New York', '10001', '123456789');

INSERT IGNORE INTO branch (id, name, phone, address, city, state, zipcode, bank_id)
VALUES (1, 'Main Branch', '(800) 123-4567', '123 Aline Financial St.', 'New York', 'New York', '10001', 1);

create table if not exists merchant
(
    code          varchar(8)   not null
        primary key,
    address       varchar(255) null,
    city          varchar(255) null,
    description   varchar(255) null,
    name          varchar(150) not null,
    registered_at datetime(6)  null,
    state         varchar(255) null,
    zipcode       varchar(255) null
);

INSERT IGNORE INTO merchant (code, name)
VALUES ('ALINE', 'Aline Financial');

INSERT IGNORE INTO merchant (code, name)
VALUES ('NONE', 'None');

INSERT IGNORE INTO card_issuer (issuer_name, card_number_length)
VALUES ('AMEX', 16);

INSERT IGNORE INTO card_issuer (issuer_name, card_number_length)
VALUES ('VISA', 16);

INSERT IGNORE INTO card_issuer (issuer_name, card_number_length)
VALUES ('MASTERCARD', 16);

INSERT IGNORE INTO card_issuer (issuer_name, card_number_length)
VALUES ('DISCOVER', 15);

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (4, 'VISA');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (34, 'AMEX');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (37, 'AMEX');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (51, 'MASTERCARD');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (52, 'MASTERCARD');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (53, 'MASTERCARD');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (54, 'MASTERCARD');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (55, 'MASTERCARD');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (6011, 'DISCOVER');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (644, 'DISCOVER');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (645, 'DISCOVER');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (646, 'DISCOVER');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (647, 'DISCOVER');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (648, 'DISCOVER');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (649, 'DISCOVER');

INSERT IGNORE INTO issuer_identification_number (iin, card_issuer_name)
VALUES (65, 'DISCOVER');

INSERT IGNORE INTO credit_card_offer (amount, offer_name, description, card_issuer_name, credit_line_type, min_apr, max_apr, min_payment)
VALUES (500000, 'Aline Standard', 'Standard credit card.', 'VISA', 'STANDARD', 10.5, 24.99, 25);

INSERT IGNORE INTO credit_card_offer (amount, offer_name, description, card_issuer_name, credit_line_type, min_apr, max_apr, min_payment)
VALUES (250000, 'Aline Student', 'Student credit card.', 'DISCOVER', 'STUDENT', 10.5, 16.99, 25);

INSERT IGNORE INTO credit_card_offer (amount, offer_name, description, card_issuer_name, credit_line_type, min_apr, max_apr, min_payment)
VALUES (800000, 'Aline Air', 'Airline credit card.', 'AMEX', 'AIRLINE', 9.5, 12.99, 25);

INSERT IGNORE INTO credit_card_offer (amount, offer_name, description, card_issuer_name, credit_line_type, min_apr, max_apr, min_payment)
VALUES (800000, 'Aline Rewards', 'Rewards credit card.', 'MASTERCARD', 'REWARDS', 11.5, 24.99, 25);

/*********************************************************************
******************* USAGE WITH YOUR PROJECT **************************
**********************************************************************
 */

/*********************************************************************
*   Make sure to include the following property in your
*   application.yml / application.properties file:
*
*   *Properties*
*   spring.datasource.initialization-mode=always
*
*   *YAML*
*   spring:
*       datasource:
*           initialization-mode=always
**********************************************************************
*   This will make sure your project will initialize the database
*   if it has not already been initialized.
**********************************************************************
 */

