import requests
import random

def generateData():
    emails = ["phanirajaenugula@gmail.com", "brandonburana2@gmail.com", "karvirishaan@gmail.com",
    "vaenugula@gmail.com"]
    tags = ["basketball", "soccer", "football", "hockey", "tennis", "chess", "golf"]

    for i in range(50):
        data = {
            "userEmail": emails[random.randint(0, len(emails)-1)],
            "postContent": "random thing",
            "linkUrl": "",
            "photoUrl": "/static/media/brandon.5c762569341bd800f8da.png",
            "tags": [tags[random.randint(0, len(tags)-1)]],
            "timePosted":None,
            "likes":0
        }
        print(requests.post("http://localhost:8080/post", json=data).text)


generateData()

