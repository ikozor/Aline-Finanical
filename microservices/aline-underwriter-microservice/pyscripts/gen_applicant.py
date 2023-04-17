''' Generating Applicants '''

from random import randint
from faker import Faker

from generate import Aline_Object, gen_phonenumber

class Applicant(Aline_Object):
    ''' Dataclass for Applicants'''
    def __init__(self, **data):
        ''' Initialize the data for an applicant, random unless specified '''
        fake = Faker()
        self.firstName = data.get('firstName') if data.get('firstName') else fake.first_name()
        self.middleName = data.get('middleName') if data.get('middleName') else fake.first_name()
        self.lastName = data.get('lastName') if data.get('lastName') else fake.last_name()
        self.dateOfBirth = data.get('dateOfBirth') if data.get('dateOfBirth') else fake.date()
        while int(self.dateOfBirth[:4]) > 2003:
            self.dateOfBirth = fake.date()
        if data.get('gender'):
            self.gender = data.get('gender')
        else:
            self. gender = 'MALE' if fake.profile()['sex'] == 'M' else 'FEMALE'
        self.email = data.get('email') if data.get('email') else fake.email()
        self.phone = data.get('phone') if data.get('phone') else gen_phonenumber()
        self.socialSecurity=data.get('socialSecurity') if data.get('socialSecurity') else fake.ssn()
        if data.get('driversLicense'):
            self.driversLicense=data.get('driversLicense')
        else:
            self.driversLicense = fake.isbn10()
        self.income = data.get('income') if data.get('income') else randint(100_000,10_000_000)
        self.address = data.get('address') if data.get('address') else fake.address().split('\n')[0]
        while not self.address[0].isdigit():
            self.address = fake.address().split('\n')[0]
        self.city = data.get('city') if data.get('city') else fake.city()
        self.state = data.get('state') if data.get('state') else fake.state()
        self.zipcode = data.get('zipcode') if data.get('zipcode') else fake.zipcode()
        self.mailingAddress = self.address
        self.mailingCity = self.city
        self.mailingState = self.state
        self.mailingZipcode = self.zipcode
        self.id = data.get('id') if data.get('id') else None


def generate_applicants(amount = 1) -> list:
    ''' Generate a certain number of applicants '''
    applicants = []
    for _ in range(amount):
        applicants.append(Applicant())
    return applicants
