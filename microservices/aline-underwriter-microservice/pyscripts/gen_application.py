''' Generating Applications '''
from random import randint

from microservice_urls import APPLICATION_URL

from generate import Aline_Object
from generate.gen_applicant import generate_applicants, Applicant

APPLICATION_TYPES = ('SAVINGS', 'CHECKING', 'CHECKING_AND_SAVINGS', 'CREDIT_CARD', 'LOAN')

class Application(Aline_Object):
    ''' Dataclass for Applications'''
    def __init__(self, applicants: list['Applicant'], **data):
        ''' Initialize a new Application based on applicants and random data unless specified '''
        if data.get('applicationType'):
            if data.get('applicationType') not in APPLICATION_TYPES:
                raise ValueError(f'Application must be one of {APPLICATION_TYPES}')
            self.applicationType = data.get('applicationType')
        else:
            self.applicationType = APPLICATION_TYPES[randint(0,3)]
        self.noNewApplicants = data.get('noNewApplicants') if data.get('noNewApplicants') else False
        self.applicantIds = data.get('applicantIds')
        self.applicants = [vars(app) for app in applicants]
        if data.get('applicationAmount'):
            self.applicationAmount = data.get('applicationAmount')
        else:
            self.applicationAmount = randint(100_000, 10_000_000)
        self.cardOfferId = data.get('cardOfferId') if data.get('cardOfferId') else randint(1, 3)
        if data.get('depositAccountNumber'):
            self.desositAccountNumber = data.get('depositAccountNumber')
        else:
            self.depositAccountNumber = str(hex(randint(0, 100_000)))
        self.id = None
        self.createdAccounts = None
        self.createdMembers = None
        self.status = None


    def post(self, url, token=''):
        response = super().post(url=url, token=token)
        self.createdAccounts = response.get('createdAccounts')
        self.createdMembers = response.get('createdMembers')
        self.status = response.get('status')
        return response


def get_posted_application(**data):
    ''' get a application that was posted to the microservice '''
    response = {'error': None}
    attempts = 0
    while ('error' in response or response.get('status')=='DENIED') and attempts < 10:
        application = Application(applicants=generate_applicants(randint(1,3)), **data)
        response = application.post(url=APPLICATION_URL)
        attempts += 1
    return {'response': response, 'application': application}
