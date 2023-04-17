''' Generating Cards '''

from microservice_urls import CARDS_URL

from generate import Aline_Object, post_data
from generate.gen_application import Application, get_posted_application
from generate.gen_user import User, get_posted_user

class Card(Aline_Object):
    ''' Dataclass for Cards '''
    def __init__(self, account_number, membership_id, **data):
        ''' Initialize card based on application, random otherwise unless specified '''
        self.accountNumber = account_number
        self.membershipId = membership_id
        if data.get('replacement'):
            self.replacement = data.get('replacement')
        else:
            self.replacement = False
        self.cardNumber = data.get('cardNumber')
        self.securityCode = data.get('securityCode')
        self.expirationDate = data.get('expirationDate')
        self.id = None


    def post(self, url, token= ''):
        ''' Overriden to add more data'''
        response = super().post(url=url, token=token)
        self.cardNumber = response.get('cardNumber')
        self.securityCode = response.get('securityCode')
        self.expirationDate = response.get('expirationDate')
        return response


    def activate(self, user: 'User', token=None):
        ''' Activate card based on user'''
        if not token:
            token = user.login()
        if not self.cardNumber:
            raise ValueError('Card has not been posted')
        data = {
            'cardNumber': self.cardNumber,
            'securityCode': self.securityCode,
            'expirationDate': self.expirationDate,
            'dateOfBirth': user.dateOfBirth,
            'lastFourOfSSN': user.lastFourOfSSN
        }
        response = post_data(url=CARDS_URL+'/activation', data=data, token=token)
        if response.status_code != 200:
            return 'error: ' + response.text
        return 'Card Activated'


def get_posted_card(application: 'Application'= None, replacement = False):
    ''' Get a card posted to the mircoservice'''
    if not application:
        application = get_posted_application(applicationType='CHECKING').get('application')
    response = {'error': ''}
    attempts = 0
    while 'error' in response and attempts < 10:
        if 'Active card already exists' in response.get('error'):
            replacement = True
        mem_id = application.createdMembers[0].get('membershipId')
        acc_num = application.createdAccounts[0].get('accountNumber')
        card = Card(membership_id=mem_id, account_number=acc_num, replacement=replacement)
        response = card.post(url=CARDS_URL+'/debit')
        attempts += 1
    return {'card': card, 'response': response}

def get_active_card(application: 'Application'= None, user:'User'=None):
    ''' Get a card that has been activated '''
    data = 'error'
    attempts = 0
    if not application:
        application = get_posted_application(applicationType='CHECKING').get('application')
    if not user:
        user = get_posted_user(application=application).get('user')
    while data != 'Card Activated' and attempts < 10:
        card = get_posted_card(application=application).get('card')
        data = card.activate(user=user)
        attempts += 1

    return {'card': card, 'message': data}
