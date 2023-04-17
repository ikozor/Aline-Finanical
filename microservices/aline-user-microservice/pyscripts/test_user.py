''' All test that have to do with user '''
from generate.gen_user import generate_users, get_posted_user

def validate_password(password):
    ''' Validate password '''
    lower = upper = special = digit = 0
    for i in password:
        if i.islower():
            lower += 1
        if i.isupper():
            upper += 1
        if i.isdigit():
            digit += 1
        if i in '@#$%&!&':
            special += 1
    return lower > 0 and upper > 0 and digit > 0 and special > 0 and len(password) >= 8


def test_generate_user():
    ''' Test generating a user'''
    user = generate_users(password='09870987zZ@')[0]
    assert hasattr(user, 'username')
    assert hasattr(user, 'password')
    assert user.password == '09870987zZ@'
    assert validate_password(user.password)
    assert hasattr(user, 'firstName')
    assert hasattr(user, 'lastName')
    assert hasattr(user, 'email')
    assert '@' in user.email and '.' in user.email
    assert hasattr(user, 'phone')
    assert '(' in user.phone and ')' in user.phone
    assert '-' in user.phone
    assert len(user.phone) == 14
    assert hasattr(user, 'lastFourOfSSN')
    assert len(user.lastFourOfSSN) == 4
    assert hasattr(user, 'membershipId')


def test_post_user():
    ''' Test posting a user to the user microservice '''
    data =  get_posted_user()
    user = data.get('user')
    response = data.get('response')
    assert user.id == response.get('id')
    assert user.firstName == response.get('firstName')
    assert user.lastName == response.get('lastName')
    assert hasattr(user, 'email')
    assert response.get('role') in ['ADMINISTRATOR', 'MEMBER', 'EMPLOYEE']
    assert isinstance(response.get('enabled'), bool)


def test_confirming_user():
    ''' Test confirming a user that is a member'''
    data = get_posted_user(role='member')
    user = data.get('user')
    response = data.get('response')
    confirmation = user.confirm(emailed_token = response.get('email'))
    assert confirmation == 'Confirmed User'


def test_login_user():
    ''' Test login into the microservice '''
    user = get_posted_user().get('user')
    token = user.login()
    assert 'Bearer ' in token
