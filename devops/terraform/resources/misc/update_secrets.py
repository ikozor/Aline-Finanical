import sys
import yaml
from base64 import b64encode

def update_secret(db_password):
    with open("misc/secrets.yaml") as yaml_in_stream:
        values_yaml = yaml.safe_load(yaml_in_stream)
        encoded_password = b64encode(db_password.encode("ascii")).decode("ascii")
        values_yaml['data']['DB_PASSWORD'] = encoded_password

    with open("misc/secrets.yaml", "w") as yaml_out_stream:
        yaml.dump(values_yaml, yaml_out_stream, default_flow_style=False, sort_keys=False)

if __name__ == "__main__" :
    update_secret(sys.argv[1])
