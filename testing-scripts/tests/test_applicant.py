''' Tests for all the files in the generators '''

from generate import gen_phonenumber
from generate.gen_applicant import Applicant, generate_applicants


def validate_date_and_ssn(data):
    ''' Validates date and ssn '''
    assert '-' in data
    for i in data:
        if i == '-':
            continue
        if not i.isdigit():
            return False
    return True


def test_generate_phonenumber():
    ''' Test generating a phone number '''
    phonenumber = gen_phonenumber()
    assert '(' in phonenumber
    assert ')' in phonenumber
    assert '-' in phonenumber
    assert ' ' in phonenumber
    assert len(phonenumber) == 14
    for i in phonenumber:
        if i in ('(',')','-',' '):
            continue
        assert i.isdigit()


def test_generate_applicant():
    ''' Test generating an applicant '''
    applicant = Applicant(firstName='John')
    assert applicant.firstName == 'John'
    assert hasattr(applicant,'middleName')
    assert hasattr(applicant,'dateOfBirth')
    assert hasattr(applicant,'gender')
    assert hasattr(applicant,'email')
    assert hasattr(applicant,'socialSecurity')
    assert hasattr(applicant,'driversLicense')
    assert hasattr(applicant,'income')
    assert hasattr(applicant,'address')
    assert hasattr(applicant,'city')
    assert hasattr(applicant,'state')
    assert hasattr(applicant,'zipcode')
    assert hasattr(applicant,'mailingAddress')
    assert hasattr(applicant,'mailingCity')
    assert hasattr(applicant,'mailingState')
    assert hasattr(applicant,'mailingZipcode')
    assert validate_date_and_ssn(applicant.dateOfBirth)
    assert applicant.gender in ('MALE', 'FEMALE')
    assert '@' in applicant.email and '.' in applicant.email
    assert validate_date_and_ssn(applicant.socialSecurity)


def test_generate_applicants():
    ''' Make sure when generating multiple applicants that there are correct amount of applicants'''
    assert len(generate_applicants(amount=5)) == 5
