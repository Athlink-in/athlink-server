import requests
import random

def generateData():
    emails = ["phanirajaenugula@gmail.com", "brandonburana2@gmail.com", "karvirishaan@gmail.com",
    "vaenugula@gmail.com"]

    for i in range(50):
        data = {
            "email": emails[random.randint(0, len(emails)-1)],
            "postContent": "random thing",
            "linkUrl": "",
            "photoUrl": "/static/media/brandon.5c762569341bd800f8da.png"
        }
        requests.post("http://localhost:8080/post", json=data)


generateData()

