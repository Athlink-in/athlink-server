import requests
import random
import randomname
from youtubesearchpython import VideosSearch


def generateData():
    emails = ["phanirajaenugula@gmail.com", "brandonburana2@gmail.com", "karvirishaan@gmail.com",
    "vaenugula@gmail.com"]
    tags = ["basketball", "soccer", "football", "hockey", "tennis", "chess", "golf"]

    for i in range(2):

        curr_user = emails[random.randint(0, len(emails)-1)]
        curr_tags = [tags[random.randint(0, len(tags)-1)]]
        videosSearch = VideosSearch(curr_tags[0], limit = 5)
        res = videosSearch.result()
        print(curr_user, curr_tags)
        for i in res['result']:
            video_id = i['id']
            embedded_link = f'https://www.youtube.com/embed/{video_id}'
            print(embedded_link)

            data = {
                "userEmail": curr_user,
                "postContent": "random thing",
                "linkUrl": embedded_link,
                "photoUrl": "/static/media/brandon.5c762569341bd800f8da.png",
                "tags": curr_tags,
                "timePosted":None,
                "likeCount":0,
                "likes":[],
                "postId": "test"
            }
            print(requests.post("https://athlink-server.herokuapp.com/post", json=data).status_code)


generateData()


