''' Generating Users '''

from faker import Faker

from microservice_urls import USERS_URL

from generate import Aline_Object, post_data
from generate.gen_application import get_posted_application, Application
from generate.gen_applicant import Applicant


f = Faker()

class User(Aline_Object):
    ''' Dataclass for Users'''
    def __init__(self, applicant: 'Applicant', membership_id: str, **data):
        ''' Initialize the user based on applicant and membership_id '''
        fake = Faker()
        self.username = data.get('username') if data.get('username') else fake.user_name()+'1234'
        self.password = data.get('password') if data.get('password') else '12341234qQ!'
        self.firstName = applicant.firstName
        self.lastName = applicant.lastName
        self.role = data.get('role') if data.get('role') else 'admin'
        self.email = applicant.email
        self.phone = applicant.phone
        self.lastFourOfSSN = applicant.socialSecurity[7:]
        self.membershipId = membership_id
        self.dateOfBirth = applicant.dateOfBirth
        self.id = data.get('id')


    def confirm(self, emailed_token):
        ''' Confirm the user in microservice if not admin '''
        if not self.id:
            raise ValueError('User has not been posted to the microservice')
        if self.role == 'admin':
            return 'Admin does not need to be confirmed'
        response = post_data(url=USERS_URL+'/confirmation', data={'token': emailed_token})
        if response.status_code == 200:
            return 'Confirmed User'
        return False


    def login(self):
        ''' Login user and return token '''
        if not self.id:
            raise ValueError('User has not been posted to the microservice')
        data = {
            'username': self.username,
            'password': self.password
        }
        response = post_data(url=USERS_URL.replace('users','')+'login', data=data)
        if 'Authorization' in response.headers:
            return response.headers.get('Authorization')
        return response.text


def generate_users(application:'Application'=None, **data):
    ''' Generate a list of users based on application '''
    if not application:
        application = get_posted_application().get('application')
    users = []
    for i, applicant in enumerate(application.applicants):
        membership_id = application.createdMembers[i].get('membershipId')
        users.append(User(applicant=Applicant(**applicant), membership_id=membership_id, **data))
    return users


def get_posted_user(application:'Application'=None, **data):
    ''' Get a random posted user '''
    response = {'error': None}
    attempts = 0
    while 'error' in response and attempts < 10:
        user = generate_users(application=application, **data)[0]
        response = user.post(url=USERS_URL+'/registration')
        attempts += 1
    return {'response': response, 'user': user}
