from datetime import time
from time import sleep

import redis
import pprint
import json
from rest_client import RestClient

c = redis.ConnectionPool(host='localhost', port=6379, db=0, decode_responses=True)
r = redis.StrictRedis(connection_pool=c)


def get_all_keys(key):
    return r.hgetall(key)


def delete_everything():
    for key in r.scan_iter("*"):
        r.delete(key)


def delete_old_keys():
    for key in r.scan_iter("*"):
        idle = r.object("idletime", key)
        if idle > 7776000:  # idle time is in seconds, current value - 90 days, 86400 - 1 day
            r.delete(key)


def expire_all_keys():
    for key in r.scan_iter("*"):
        if r.ttl(key) == -1:
            r.expire(key, 60 * 60 * 24 * 7)  # This would clear them out in a week


class ConvertRequestLocal(object):
    def __init__(self, url):
        self.url = url

    def json(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)


print("cleaning database first")
delete_everything()
print(r.keys('*'))
client = RestClient()
request_no = 0
out_file_success = open('converted-success.json', 'w')
out_file_success.write('[\n')
out_file_failures = open('converted-failures.json', 'w')
out_file_failures.write('[\n')
success = 0
failure = 0
try:
    while request_no < 10:
        request_no = request_no+1
        url = "https://www.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogochuchaf.eu/" + str(request_no)
        request = ConvertRequestLocal(url)
        response = client.rest_call("POST", "/url-converter/add", request.json())

        content = response[0]
        payload = response[1]
        if content['status']=='200':
            out_file_success.write('{"content": ')
            out_file_success.write(json.dumps(content))
            out_file_success.write(', "shortUrl": ')
            out_file_success.write(json.dumps(str(payload)[1:].replace("'", "")))
            out_file_success.write('},\n')
            success = success + 1
        else:
            out_file_failures.write('{"content": ')
            out_file_failures.write(json.dumps(content))
            out_file_failures.write(', "error": ')
            out_file_failures.write(json.dumps(json.loads(str(payload)[1:].replace("'", ""))))
            out_file_failures.write('},\n')
            failure = failure + 1

        sleep(0.5)

except KeyboardInterrupt:
    print("*** test manually interrupted ***")

out_file_success.writelines(']')
out_file_failures.writelines(']')
out_file_success.close()
out_file_failures.close()
print("*** end of test ***")

print("*** number of successful calls: " + str(success))
print("*** number of failed calls: " + str(success))

entries = get_all_keys('url:')
print("Number of URLs save in DB: " + str(len(entries)))

# pp = pprint.PrettyPrinter(indent=4)
# pp.pprint(entries)

