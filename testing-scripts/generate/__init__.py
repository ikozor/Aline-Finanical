''' Contains functions that will be used throughout the module'''
import json
import requests
from random import randint

def post_data(url, data, token=''):
    ''' Post the application to the mircoservice '''
    json_data = json.dumps(data)
    response = requests.post(url,
        data=json_data,
        headers={
            'accept': '*/*',
            'content-type': 'application/json',
            'Authorization': token
        },
        timeout=10
    )
    return response


class Aline_Object():
    def post(self, url, token=''):
        if self.id:
            return {'error': 'Object has already been posted'}
        response = post_data(url=url, data = self.__dict__, token=token)
        if response.status_code not in (201, 200):
            return {'error': response.text}
        data = json.loads(response.text)
        self.id = data.get('id')
        return data


def gen_phonenumber() -> str:
    ''' Generate a phone number in the correct format '''
    nums = [str(randint(0,9)) for x in range(10)]
    nums.insert(0,'(')
    nums.insert(4,')')
    nums.insert(5,' ')
    nums.insert(9,'-')
    return ''.join(nums)
