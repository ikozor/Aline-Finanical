''' Generating Transactions '''

from datetime import datetime
from random import randint

from microservice_urls import TRANSACTION_URL

from generate import Aline_Object
from generate.gen_application import Application, get_posted_application
from generate.gen_card import Card, get_posted_card

TRANSACTION_METHODS = ['APP', 'ATM', 'DEBIT_CARD', 'ACH', 'CREDIT_CARD']
TRANSACTION_TYPES = ['PURCHASE',
                    'VOID',
                    'WITHDRAWAL',
                    'PAYMENT',
                    'REFUND',
                    'TRANSFER_IN',
                    'TRANSFER_OUT',
                    'DEPOSIT'
                    ]

class Transaction(Aline_Object):
    ''' Dataclass for Transactions '''
    def __init__(self, account_number: str, card: 'Card' = None, **data):
        ''' Initialize transaction based on card and application, random data unless specified'''
        if data.get('method'):
            if data.get('method') not in TRANSACTION_METHODS:
                raise ValueError(f'Method must be in {TRANSACTION_METHODS}')
            self.method = data.get('method')
        else:
            self.method = TRANSACTION_METHODS[randint(0,4)]
        if data.get('type'):
            if data.get('type') not in TRANSACTION_TYPES:
                raise ValueError(f'Type must be in {TRANSACTION_TYPES}')
            self.type = data.get('type')
        else:
            self.type = TRANSACTION_TYPES[randint(0,6)]
        self.date = data.get('date') if data.get('date') else datetime.now().isoformat()
        self.amount = data.get('amount') if data.get('amount') else randint(1,10_000)
        if data.get('merchantCode'):
            self.merchantCode = data.get('merchantCode')
        else:
            self.merchantCode = str(randint(10000,9999999))
        self.merchantName = data.get('merchantName') if data.get('merchantName') else 'Tester'
        self.description = data.get('description') if data.get('description') else 'test'
        if card:
            self.cardRequest = {
                'cardNumber': card.cardNumber,
                'securityCode': card.securityCode,
                'expirationDate': card.expirationDate,
            }
        self.accountNumber = account_number
        self.hold = data.get('hold') if data.get('hold') else True
        self.id = data.get('id')


def get_posted_transaction(application:'Application'=None, card:'Card'=None, **data):
    ''' Get a transaction that is posted to the microservice '''
    response = {'error': None}
    attempts = 0
    if not application:
        application = get_posted_application().get('application')
    if not card and application.applicationType =='CHECKING':
        card = get_posted_card(application=application).get('card')
    while 'error' in response and attempts < 10:
        acc = application.createdAccounts[0].get('accountNumber')
        transaction = Transaction(account_number=acc, card=card, **data)
        response = transaction.post(url=TRANSACTION_URL)
        attempts += 1
    return {'response': response, 'transaction': transaction}
