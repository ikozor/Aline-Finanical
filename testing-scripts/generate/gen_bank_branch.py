''' Generating Banks and Branches '''

import json
from random import randint
from faker import Faker

from microservice_urls import BANK_BRANCH_URL

from generate import Aline_Object, post_data, gen_phonenumber
from generate.gen_user import get_posted_user

class Bank(Aline_Object):
    ''' Dataclass for Banks '''
    def __init__(self, **data):
        ''' Initialize data for bank, random unless specified '''
        fake = Faker()
        if 'routingNumber' in data:
            self.routingNumber = data.get('routingNumber')
        else:
            self.routingNumber = str(randint(100000000,999999999))
        self.address = data.get('address') if data.get('address') else fake.address().split('\n')[0]
        self.city = data.get('city') if data.get('city') else fake.city()
        self.state = data.get('state') if data.get('state') else fake.state()
        self.zipcode = data.get('zipcode') if data.get('zipcode') else fake.zipcode()
        self.id = data.get('id')
        while not self.address[0].isdigit():
            self.address = fake.address().split('\n')[0]


def get_posted_bank():
    ''' Get a random posted bank '''
    bank = Bank()
    user = get_posted_user().get('user')
    response = bank.post(url=BANK_BRANCH_URL+'/banks' ,token=user.login())
    if 'error' in response:
        return get_posted_bank()
    return {'response': response, 'bank': bank}


class Branch(Aline_Object):
    ''' Dataclass for Branches '''
    def __init__(self, bank:'Bank', **data):
        ''' Initialize data for branch, random unless specified '''
        if not hasattr(bank, 'id'):
            raise ValueError('Bank has not been posted')
        fake = Faker()
        self.name = data.get('name') if 'name' in data else fake.user_name()
        self.address = bank.address
        self.city = bank.city
        self.state = bank.state
        self.zipcode = bank.zipcode
        self.phone = data.get('phone') if 'phone' in data else gen_phonenumber()
        self.bankID = bank.id
        self.id = None


    def post(self, url, token=''):
        ''' The reason I overrode this is because it loops forever and I removed it '''
        if self.id:
            return {'error': 'Object has already been posted'}
        response = post_data(url=url, data = self.__dict__, token=token)
        if response.status_code != 201:
            return {'error': response.text}
        response_text = response.text.split(',"bank":')[0] + '}'
        data = json.loads(response_text)
        self.id = data.get('id')
        return data


def get_posted_branch(bank=None):
    ''' Get a random posted branch '''
    bank_data = bank if bank else get_posted_bank().get('bank')
    branch = Branch(bank_data)
    user = get_posted_user().get('user')
    response = branch.post(url=BANK_BRANCH_URL+'/branches', token=user.login())
    if 'error' in response:
        return get_posted_branch()
    return {'response': response, 'branch': branch}
