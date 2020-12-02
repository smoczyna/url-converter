from time import sleep
import httplib2
import json

host = "http://192.168.0.15"


class RestClient(object):

    @staticmethod
    def rest_call(method, rest_path, body, no_headers=False):
        try:
            from urlparse import urlparse
        except ImportError:
            from urllib.parse import urlparse

        no_headers = {}
        headers = {
            'Content-Type': 'application/json; charset=UTF-8'
        }
        baseUri = host + ':8080'
        target = urlparse(baseUri + rest_path)
        h = httplib2.Http()
        if no_headers:
            return h.request(target.geturl(), method, body, no_headers)
        else:
            return h.request(target.geturl(), method, body, headers)


class ConvertRequestLocal(object):
    def __init__(self, url):
        self.url = url

    def json(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)


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
