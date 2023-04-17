''' Tests for everything that deals with cards '''
from generate.gen_card import get_posted_card, Card, get_active_card
from generate.gen_application import get_posted_application

def test_generate_debit():
    ''' test creating a valid debit card '''
    application = get_posted_application(applicationType="CHECKING").get('application')
    mem_id = application.createdMembers[0].get('membershipId')
    acc_num = application.createdAccounts[0].get('accountNumber')
    card = Card(membership_id=mem_id, account_number=acc_num)
    assert hasattr(card, 'accountNumber')
    assert hasattr(card, 'membershipId')
    assert hasattr(card, 'replacement')


def test_posted_card():
    ''' test getting a card posted to the microservice '''
    data = get_posted_card()
    card = data.get('card')
    response = data.get('response')
    assert card.accountNumber == response.get('accountNumber')
    assert card.membershipId == response.get('cardHolderId')
    assert card.cardNumber == response.get('cardNumber')
    assert card.securityCode == response.get('securityCode')
    assert card.expirationDate == response.get('expirationDate')


def test_activate_card():
    ''' test getting a valid activated debit card'''
    response = get_active_card()
    assert response.get('message') == 'Card Activated'
