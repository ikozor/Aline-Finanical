''' All tests for cards and transactions '''
from generate.gen_application import get_posted_application
from generate.gen_card import get_active_card
from generate.gen_transaction import Transaction, get_posted_transaction
from generate.gen_transaction import TRANSACTION_METHODS, TRANSACTION_TYPES

def test_generate_transactions():
    ''' test gernerating a transaction '''
    application = get_posted_application().get('application')
    acc = application.createdAccounts[0].get('accountNumber')
    transaction = Transaction(account_number=acc)
    for i in range(2):
        if i == 1:
            application = get_posted_application(applicationType='CHECKING').get('application')
            card = get_active_card(application=application).get('card')
            acc = application.createdAccounts[0].get('accountNumber')
            transaction = Transaction(account_number=acc, method='DEBIT_CARD', card=card)
            assert hasattr(transaction, 'cardRequest')
            assert isinstance(transaction.cardRequest.get('cardNumber'), str)
            assert isinstance(transaction.cardRequest.get('securityCode'), str)
            assert isinstance(transaction.cardRequest.get('expirationDate'), str)
        assert hasattr(transaction, 'type')
        assert transaction.type in TRANSACTION_TYPES
        assert hasattr(transaction, 'method')
        assert transaction.method in TRANSACTION_METHODS
        assert hasattr(transaction, 'date')
        assert hasattr(transaction, 'amount')
        assert hasattr(transaction, 'merchantCode')
        assert hasattr(transaction, 'merchantName')
        assert hasattr(transaction, 'description')
        assert hasattr(transaction, 'accountNumber')
        assert hasattr(transaction, 'hold')


def test_posted_transaction():
    ''' Test posted transaction to to the microservice '''
    data = get_posted_transaction()
    transaction = data.get('transaction')
    response = data.get('response')
    assert transaction.id == response.get('id')
    assert transaction.type == response.get('type')
    assert transaction.method == response.get('method')
    assert transaction.amount == response.get('amount')
    assert response.get('status') in ('APPROVED', 'DENIED')
