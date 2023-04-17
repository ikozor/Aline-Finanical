''' Tests for everything with banks and branches'''
from generate.gen_bank_branch import Bank, get_posted_bank
from generate.gen_bank_branch import Branch, get_posted_branch

def test_generate_bank():
    ''' Test generating a bank '''
    bank = Bank()
    assert hasattr(bank,'routingNumber')
    assert hasattr(bank,'address')
    assert hasattr(bank,'city')
    assert hasattr(bank,'state')
    assert hasattr(bank,'zipcode')


def test_post_bank():
    ''' Test posting a bank to the microservice '''
    data = get_posted_bank()
    bank = data.get('bank')
    response = data.get('response')
    assert response.get('id') == bank.id
    assert response.get('routingNumber') == bank.routingNumber
    assert response.get('address') == bank.address
    assert response.get('city') == bank.city
    assert response.get('state') == bank.state
    assert response.get('zipcode') == bank.zipcode


def test_generate_branch():
    ''' Test generating a branch '''
    bank = get_posted_bank().get('bank')
    branch = Branch(bank)
    assert hasattr(branch, 'name')
    assert hasattr(branch, 'phone')
    assert branch.address == bank.address
    assert branch.city == bank.city
    assert branch.state == bank.state
    assert branch.zipcode == bank.zipcode
    assert branch.bankID == bank.id


def test_post_branch():
    ''' Test posting a branch to the microservice '''
    data = get_posted_branch()
    branch = data.get('branch')
    response = data.get('response')
    assert response.get('id') == branch.id
    assert response.get('name') == branch.name
    assert response.get('address') == branch.address
    assert response.get('city') == branch.city
    assert response.get('state') == branch.state
    assert response.get('zipcode') == branch.zipcode
    assert response.get('phone') == branch.phone
