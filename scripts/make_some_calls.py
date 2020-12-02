import json


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
        baseUri = 'http://localhost:8080'
        target = urlparse(baseUri + rest_path)
        h = httplib2.Http()
        if no_headers:
            return h.request(target.geturl(), method, body, no_headers)
        else:
            return h.request(target.geturl(), method, body, headers)


client = RestClient()

url1 = '{"url": "https://www.google.com/search?client=ubuntu&sxsrf=ALeKk03Jiqd5d5Xh4YmERQfm2qjFtXAZ-Q%3A1606505037246&ei=TVLBX-67DsOV1fAPjbGV2A8&q=spring+boot+influxdb+example&oq=spring+boot+influx+&gs_lcp=CgZwc3ktYWIQAxgCMgcIABDJAxAKMgQIABAKMgQIABAKMgQIABAKMgQIABAKMgYIABAWEB4yBggAEBYQHjIGCAAQFhAeOgQIABBHOgQIIxAnOgUIABCRAjoECAAQQzoICAAQsQMQgwE6DgguELEDEIMBEMcBEKMCOgcIIxDqAhAnOggIABDJAxCRAjoHCAAQyQMQQzoHCAAQsQMQQzoCCAA6BQgAEMkDOgIILjoHCAAQyQMQDToECAAQDToHCCEQChCgAToGCCEQDRAVUN0eWKZsYJSEAWgDcAJ4AIABlAGIAfQNkgEEMTcuNZgBAKABAaoBB2d3cy13aXqwAQrIAQjAAQE&sclient=psy-ab"}'
url2 = '{"url": "https://www.google.com/search?client=ubuntu&sxsrf=ALeKk03Jiqd5d5Xh4YmERQfm2qjFtXAZ-Q%3A"}'
url3 = '{"url": "https://www.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogochuchaf.eu"}'

for i in range(1, 10):
    response1 = client.rest_call("POST", "/shortener/add-plain-text", url1, True)
    print(response1)
    response2 = client.rest_call("POST", "/shortener/add-plain-text", url2, True)
    print(response2)
    response3 = client.rest_call("POST", "/shortener/add-plain-text", url3, True)
    print(response3)


class ConvertRequestLocal(object):
    def __init__(self, url):
        self.url = url

    def json(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)


url4 = "https://www.google.com/search?client=ubuntu&sxsrf=ALeKk03Jiqd5d5Xh4YmERQfm2qjFtXAZ-Q%3A1606505037246&ei=TVLBX-67DsOV1fAPjbGV2A8&q=spring+boot+influxdb+example&oq=spring+boot+influx+&gs_lcp=CgZwc3ktYWIQAxgCMgcIABDJAxAKMgQIABAKMgQIABAKMgQIABAKMgQIABAKMgYIABAWEB4yBggAEBYQHjIGCAAQFhAeOgQIABBHOgQIIxAnOgUIABCRAjoECAAQQzoICAAQsQMQgwE6DgguELEDEIMBEMcBEKMCOgcIIxDqAhAnOggIABDJAxCRAjoHCAAQyQMQQzoHCAAQsQMQQzoCCAA6BQgAEMkDOgIILjoHCAAQyQMQDToECAAQDToHCCEQChCgAToGCCEQDRAVUN0eWKZsYJSEAWgDcAJ4AIABlAGIAfQNkgEEMTcuNZgBAKABAaoBB2d3cy13aXqwAQrIAQjAAQE&sclient=psy-ab"
request = ConvertRequestLocal(url4)
response4 = client.rest_call("POST", "/url-converter/add", request.json())
content = response4[0]
shortUrl = response4[1]
print(json.dumps(content))
print(str(shortUrl)[1:].replace("'", ""))

