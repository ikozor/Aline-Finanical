''' All tests that have to do with applications '''
from generate.gen_application import Application, get_posted_application
from generate.gen_application import APPLICATION_TYPES
from generate.gen_applicant import generate_applicants


def test_generate_application():
    ''' Test generating applications '''
    application = Application(applicants=generate_applicants(), applicationAmount=1_000_000 )
    assert hasattr(application, 'applicationType')
    assert application.applicationType in APPLICATION_TYPES
    assert hasattr(application, 'noNewApplicants')
    assert hasattr(application, 'applicantIds')
    assert hasattr(application, 'applicants')
    assert application.applicantIds is None or isinstance(application.applicantIds, list)
    assert hasattr(application, 'applicationAmount')
    assert application.applicationAmount == 1_000_000
    assert hasattr(application, 'cardOfferId')
    assert hasattr(application, 'depositAccountNumber')


def test_post_application():
    ''' Test making a post request into the underwriter microservice '''
    data = get_posted_application()
    application = data.get('application')
    response = data.get('response')
    assert application.id == response.get('id')
    assert application.applicationType == response.get('applicationType')
    assert response.get('status') == 'APPROVED'
    assert isinstance(response.get('reasons'), list)
    assert isinstance(response.get('accountsCreated'), bool)
    assert application.createdAccounts == response.get('createdAccounts')
    assert isinstance(response.get('membersCreated'), bool)
    assert application.createdMembers == response.get('createdMembers')
