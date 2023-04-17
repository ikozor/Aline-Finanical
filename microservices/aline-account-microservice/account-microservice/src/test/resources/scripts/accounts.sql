insert into applicant (id, address, city, created_at, date_of_birth, drivers_license, email, first_name, gender, income, last_modified_at, last_name, mailing_address, mailing_city, mailing_state, mailing_zipcode, middle_name, phone, social_security, state, zipcode)
values (1, '123 Address St', 'City', '2021-08-25', '1997-05-03', 'DC123456', 'test_boy@email.com', 'Test', 'MALE', 5500000, '2021-08-25', 'Boy', 'PO Box 123', 'City', 'New York', '12345', 'Dummy', '(888) 555-5555', '555-55-5555', 'New York', '12345');

insert into applicant (id, address, city, created_at, date_of_birth, drivers_license, email, first_name, gender, income,
                       last_modified_at, last_name, mailing_address, mailing_city, mailing_state, mailing_zipcode,
                       middle_name, phone, social_security, state, zipcode)
values (2, '123 Little Blvd', 'City', '2021-08-06', '1997-06-02', 'DC654987', 'test_man@email.com',
        'Test', 'MALE', 30000000,  '2021-08-21', 'Man', '123 Little Blvd', 'City', 'Maine', '12345',
        'The', '(555) 888-8888', '888-88-8888', 'Maine', '12345');

insert into member (id, membership_id, applicant_id, branch_id)
values (1, '12345678', 1, 1);

insert into member (id, membership_id, applicant_id, branch_id)
values (2, '87654321', 2, 1);

insert into user (role, id, enabled, password, username, member_id)
values ('MEMBER', 1, 1, 'P@ssword123', 'test_boy', 1);

insert into user (role, id, enabled, password, username, member_id)
values ('ROLE', 2, 1, 'P@ssword123', 'test_man', 2);

insert into account (account_type, id, account_number, balance, status, available_balance,
                     primary_account_holder_id)
values ('CHECKING', 1, '123456789', 100000, 'ACTIVE', 100000, 1);

insert into account (account_type, id, account_number, balance, status, apy,
                     primary_account_holder_id)
values ('SAVINGS', 2, '123456780', 10000000, 'ACTIVE', 0.01, 1);

insert into account (account_type, id, account_number, balance, status, available_balance,
                     primary_account_holder_id)
values ('CHECKING', 3, '987654321', 100000000, 'ACTIVE', 100000000, 2);

insert into account_holder (member_id, account_id)
values (1, 1),
       (1, 2),
       (2, 3);
