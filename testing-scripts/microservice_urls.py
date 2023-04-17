''' Get all the microservice urls'''
import os
from pathlib import Path
from dotenv import load_dotenv

load_dotenv(Path('../.env'))
load_dotenv(Path('.env'))


APPLICATION_URL = os.environ['APPLICATION_URL']
BANK_BRANCH_URL = os.environ['BANK_BRANCH_URL']
CARDS_URL = os.environ['CARDS_URL']
TRANSACTION_URL = os.environ['TRANSACTION_URL']
USERS_URL = os.environ['USERS_URL']
APPLICANT_URL = os.environ['APPLICANT_URL']
