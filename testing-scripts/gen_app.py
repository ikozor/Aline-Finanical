''' Provides command line arguments for generating data'''
import os
import sys
import random
import json
from typing import Optional, List
import requests
import typer
from loguru import logger
from dotenv import load_dotenv

import microservice_urls as urls
from generate.gen_applicant import Applicant, generate_applicants
from generate.gen_application import get_posted_application, APPLICATION_TYPES, Application
from generate.gen_bank_branch import Bank, Branch
from generate.gen_card import Card
from generate.gen_transaction import Transaction, TRANSACTION_TYPES, TRANSACTION_METHODS
from generate.gen_user import User, get_posted_user

app = typer.Typer()

load_dotenv('./env')

logger.remove(0)
logger.add(sys.stderr, format='<level>| {level} |</level> {message}')


def get_data(url, filter_bank=False):
    ''' Get the dat from a microservice based on url '''
    response = requests.get(
        url,
        headers= { 'Accept': '*/*', 'Authorization': os.environ['TOKEN']},
        timeout=10
    )
    try:
        if filter_bank:
            response_text = response.text.split(',"branch')[0]+'}'
            data = json.loads(response_text)
        else:
            data = json.loads(response.text)
    except json.JSONDecodeError:
        return response.text, response.status_code
    return data, response.status_code


@app.command()
def applicant(number: Optional[int] = typer.Option(1,
        '--number', '-n',
        help = 'number of applicants to create')
    ):
    ''' Get a specified number or random applicants '''
    try:
        get_data(urls.APPLICANT_URL)
    except requests.exceptions.ConnectionError as connect_error:
        logger.critical('Applicants microservice is offline')
        raise SystemExit(connect_error) from connect_error

    applicants = generate_applicants(number)
    for cur in applicants:
        response = cur.post(urls.APPLICANT_URL, token=os.environ['TOKEN'])
        if 'error' in response:
            logger.error(response.get('error'))
        else:
            logger.success(f'Applicant {cur.id} created \nMicroservice response:\n{response}')


@app.command()
def application(
        applicant_ids: Optional[List[int]] = typer.Option(None,
            '--applicant', '-a',
            help="Ids of applicants to be added to application (max 3)"
        ),
        application_type: Optional[str] = typer.Option(
            random.choice(APPLICATION_TYPES),
            '--type', '-t',
            help = f'Options: {APPLICATION_TYPES}'
        ),
        application_amount: Optional[int] = typer.Option(None,
            '--amount',
            help='Amount for the application'
        ),
        deposit_account_number: Optional[str] = typer.Option(None),
        number: Optional[int] = typer.Option(1,
            '--number', '-n',
            help="Amount of applications to create")
    ):
    ''' Create a new Application '''
    try:
        get_data(urls.APPLICATION_URL)
    except requests.exceptions.ConnectionError as connect_error:
        logger.critical('Application microservice is offline')
        raise SystemExit(connect_error) from connect_error

    created_applicants=[]
    if application_type and application_type not in APPLICATION_TYPES:
        raise typer.Exit(f'type must be in {APPLICATION_TYPES}')
    if applicant_ids:
        if len(applicant_ids) > 3:
            raise typer.Exit('Cannot have more than 3 applicants')
        for applicant_id in applicant_ids:
            found_applicant = get_data(urls.APPLICANT_URL+f'/{applicant_id}')
            if found_applicant[1] != 200:
                logger.error(found_applicant[0])
            else:
                created_applicants.append(Applicant(**found_applicant[0]))

    if len(created_applicants) == 0 and typer.confirm(
            'No applicants found. \nAutomatically create new random applicants?'):
        for _ in range(number):
            new_application = get_posted_application(
                    applicationAmount = application_amount,
                    applicationType= application_type,
                    depositAccountNumber = deposit_account_number
                    )
            logger.success(f'Application {new_application.get("application").id} created'
                + f'\nmicroservice Response:\n{new_application.get("response")}\n')
        raise typer.Exit()
    for _ in range(number):
        new_application = Application(
            applicants = created_applicants,
            noNewApplicants = True,
            applicationAmount = application_amount,
            applicantIds = applicant_ids,
            applicationType = application_type,
            depositAccountNumber = deposit_account_number
        )
        response = new_application.post(urls.APPLICATION_URL, token=os.environ['TOKEN'])
        if 'error' in response:
            logger.error(response.get('error'))
        else:
            logger.success(f'Application {new_application.id} created'
                + f'\nmicroservice Response:\n{response}\n')


@app.command()
def user(
        applicant_id: int = typer.Option(None,
            '--applicant', '-a',
            help = 'Id of the applicant you with to make an id for '
        ),
        membership_id: str = typer.Option(None,
            '--membership', '-m',
            help = 'Membership id of the applicant'
        ),
        role: Optional[str] = typer.Option('admin',
            '--role', '-r',
            help = 'Role of the user, must be either admin or member'
        ),
        randomize: bool = typer.Option(False,
            help = 'Make a random user with new applicant and membership'
        )
    ):
    ''' Create a new user '''
    try:
        get_data(urls.USERS_URL)
    except requests.exceptions.ConnectionError as connect_error:
        logger.critical('Users microservice is offline')
        raise SystemExit(connect_error) from connect_error

    if role not in ('member', 'admin'):
        raise typer.Exit('role must be admin or member')
    if randomize:
        user_data = get_posted_user(role=role)
        response = user_data.get('response')
        card_user = user_data.get('user')
    elif membership_id and applicant_id:
        try:
            applicant_data = get_data(url=urls.APPLICANT_URL+f'/{applicant_id}')
            if applicant_data[1] != 200:
                logger.error(applicant_data[0])
                raise typer.Exit()
        except requests.exceptions.ConnectionError:
            data = {}
            logger.warning('Could not find data for member, please enter data manually')
            data['firstName'] = typer.prompt('First Name')
            data['lastName'] = typer.prompt('Last Name')
            data['email'] = typer.prompt('Email')
            data['phone'] = typer.prompt('Phone (XXX) XXX-XXXX format')
            data['socialSecurity'] = typer.prompt('Social Security')
            applicant_data = [data]
        card_applicant = Applicant(**applicant_data[0])
        card_user = User(applicant=card_applicant, membership_id=membership_id, role=role)
        response = card_user.post(url=urls.USERS_URL+'/registration')
    elif role == 'admin':
        card_user = User(applicant=Applicant(), membership_id=None)
        response = card_user.post(url=urls.USERS_URL+'/registration')
    else:
        logger.critical('If not admin, must provide membership id and applicant id')
        raise typer.Exit()

    if 'error' in response:
        logger.error(response.get('error'))
        raise typer.Exit()
    logger.success(response)
    if role == 'admin':
        raise typer.Exit()
    confirm = typer.confirm('User is not confirmed\nWould you like to confirm this user?')
    if confirm:
        if card_user.confirm(emailed_token=response.get('email')) == 'Confirmed User':
            logger.success('Conirmed User')
            raise typer.Exit()
        logger.error('Could not confirm user')


@app.command()
def card(
        account_number: str = typer.Option(...,
            '--acount', '-a',
            help = 'Account id of the account for which the card will be created for'
        ),
        membership_id: str = typer.Option(...,
            '--member', '-m',
            help = 'Membership id for the member which the card will be created for'
        ),
        replace: bool = typer.Option(False,
            '--replace', '-r',
            help = 'If new card for account or replacement'
        )
    ):
    ''' Create a new card '''
    try:
        get_data(urls.CARDS_URL)
    except requests.exceptions.ConnectionError as connect_error:
        logger.critical('Card microservice is offline')
        raise SystemExit(connect_error) from connect_error

    new_card = Card(membership_id=membership_id, account_number=account_number, replacement=replace)
    response = new_card.post(url=urls.CARDS_URL+'/debit', token=os.environ['TOKEN'])
    if 'error' in response:
        logger.error(response.get('error'))
        raise typer.Exit()
    logger.success(response)
    activate = typer.confirm('Would you like to activate the card?')
    if activate:
        try:
            membership = get_data(
                url=urls.BANK_BRANCH_URL+f'/members/{membership_id}',
                filter_bank=True)[0]
            membership_applicant = Applicant(**membership.get('applicant'))
            card_user = User(applicant=membership_applicant, membership_id=membership_id)
            card_user.id = 1
        except requests.exceptions.ConnectionError:
            user_data = {}
            logger.warning('Cannot get date from membership, pleas enter manually:')
            user_data['dateOfBirth'] = typer.prompt('Date of birth (YYYY-MM-DD)')
            user_data['socialSecurity'] = '123-23-' + typer.prompt('Last 4 of SSN')
            card_user = User(applicant=Applicant(**user_data), membership_id=membership_id)
            card_user.id = 1

        card_activation = new_card.activate(user=card_user, token = os.environ['TOKEN'])
        if 'error' in card_activation:
            logger.error(card_activation+'\n(Does the account have a registered user?)')
            raise typer.Exit()
        logger.success(card_activation)


@app.command()
def transaction(
        transaction_type: Optional[str] = typer.Option(None,
            '--type', '-t',
            help = f'Type of transaction, must be one if {TRANSACTION_TYPES}'
        ),
        transaction_method: Optional[str] = typer.Option(None,
            '--method', '-m',
            help = f'method of transaction, must be on of {TRANSACTION_METHODS}'
        ),
        amount: Optional[int] = typer.Option(None,
            '--amount', '-a',
            help = 'Amount of money to be sent in the transaction'
        ),
        account_number: str = typer.Option(...,
            '--account', '-acc',
            help = 'Account number for the transaction'
        )
    ):
    ''' Create a new transaction '''
    try:
        get_data(urls.TRANSACTION_URL)
    except requests.exceptions.ConnectionError as connect_error:
        logger.critical('Transaction microservice is offline')
        raise SystemExit(connect_error) from connect_error

    if transaction_type and transaction_type not in TRANSACTION_TYPES:
        logger.error(f'type must be in {TRANSACTION_TYPES}')
        raise typer.Exit()
    if transaction_method and transaction_method not in TRANSACTION_METHODS:
        logger.error(f'method must be in {TRANSACTION_METHODS}')
        raise typer.Exit()
    transaction_card = None
    if transaction_method == 'DEBIT_CARD':
        transaction_card_data = {}
        transaction_card_data['cardNumber'] = typer.prompt('Card Number')
        transaction_card_data['securityCode'] = typer.prompt('Security Code')
        transaction_card_data['expirationDate'] = typer.prompt('Expiration Date (YYYY-MM-DD)')
        transaction_card = Card(
            account_number=account_number,
            membership_id=None,
            **transaction_card_data
        )

    new_transaction = Transaction(
        type = transaction_type,
        method = transaction_method,
        amount = amount,
        account_number = account_number,
        card = transaction_card
    )
    response = new_transaction.post(url=urls.TRANSACTION_URL, token=os.environ['TOKEN'])
    if 'error' in response:
        logger.error(response.get('error'))
        raise typer.Exit()
    logger.success(response)


@app.command()
def bank(
        number: Optional[int] = typer.Option(1,
            '--number', '-n',
            help='Number of banks to create'
        )
    ):
    ''' Create a new bank '''
    try:
        get_data(urls.BANK_BRANCH_URL)
    except requests.exceptions.ConnectionError as connect_error:
        logger.critical('Bank and Branch microservice is offline')
        raise SystemExit(connect_error) from connect_error

    for _ in range(number):
        new_bank = Bank()
        response = new_bank.post(url=urls.BANK_BRANCH_URL+'/banks', token=os.environ['TOKEN'])
        if 'error' in response:
            logger.error(response.get('error'))
        else:
            logger.success(response)


@app.command()
def branch(
    bank_id : int = typer.Option(...,
        '--bank', '-b',
        help = 'Id of the bank for which to create a branch'
    )
    ):
    ''' Create a new branch '''
    try:
        get_data(urls.BANK_BRANCH_URL)
    except requests.exceptions.ConnectionError as connect_error:
        logger.critical('Bank and Branch microservice is offline')
        raise SystemExit(connect_error) from connect_error

    data = get_data(urls.BANK_BRANCH_URL+f'/banks/id/{bank_id}', filter_bank=True)[0]
    get_bank = Bank(data=data)
    get_bank.id = bank_id
    new_branch = Branch(bank=get_bank)
    response = new_branch.post(urls.BANK_BRANCH_URL+'/branches', token=os.environ['TOKEN'])
    if 'error' in response:
        logger.error(response.get('error'))
    else:
        logger.success(response)


if __name__ == '__main__':
    app()
