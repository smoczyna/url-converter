import json
import asyncio
from aiohttp import ClientSession

converter_base_url = "http://localhost:8080/url-converter"

class ConvertRequestLocal(object):
    def __init__(self, url):
        self.url = url

    def json(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)


async def get_call(url: str, session: ClientSession, **kwargs) -> str:
    resp = await session.request(method="GET", url=url, **kwargs)
    resp.raise_for_status()
    return await resp.text()


async def post_call(url: str, session: ClientSession, payload, **kwargs) -> str:
    resp = await session.request(method="POST", url=url, data=payload, **kwargs)
    resp.raise_for_status()
    return await resp.text()


def generate_payload(url: str, count: int):
    url = url + str(count)
    request = ConvertRequestLocal(url)
    return request.json()


async def make_get_requests(b62_id: str, session: ClientSession, **kwargs) -> None:
    url = converter_base_url + "/get/" + b62_id
    resp = await session.request(method="GET", url=url, **kwargs)
    resp.raise_for_status()
    return await resp.text()


async def make_post_requests(url: str, **kwargs) -> None:
    async with ClientSession() as session:
        tasks = []
        for i in range(1, 10):
            base_url = "https://www.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogochuchaf.eu/"
            payload = generate_payload(base_url, i)
            tasks.append(
                post_call(url=url, session=session, payload=payload, **kwargs)
            )
        results = await asyncio.gather(*tasks)
        for j in range(1, len(results)):
            short_url = results[j]
            base62_id = short_url.rsplit('/', 1)[1]
            try:
                if base62_id is not None:
                    get_result = await make_get_requests(base62_id, session, **kwargs)
                    print(short_url + ' : ' + get_result)
            except Exception as ex:
                print(ex)


converter_post_url = converter_base_url + "/add-plain-text"
asyncio.run(make_post_requests(converter_post_url))
