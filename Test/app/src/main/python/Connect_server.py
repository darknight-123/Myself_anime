import json

from contextlib import closing


import websocket
import ssl
ws_opt = {
    'url': "wss://v.myself-bbs.com/ws",
    'header': {

        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36',
    },


}
#tid="44956"
#vid="004"
#video_id=""
def Get_video(tid,vid,video_id):

    with closing(websocket.create_connection(**ws_opt)) as ws:
        global m3u8_url
        ws.send(json.dumps({'tid': tid, 'vid': vid, 'id': video_id}))
        recv = ws.recv()
        res = json.loads(recv)
        m3u8_url = f'https:{res["video"]}'
        if video_id:
            video_url = m3u8_url.split('/index.m3u8')[0]
        else:
            video_url = m3u8_url[:m3u8_url.rfind('/')]
        print(video_url,"\n",m3u8_url)
        return m3u8_url
