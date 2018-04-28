#!/usr/bin/python

import sys
import urllib.request
import re
import json

from bs4 import BeautifulSoup

import socket
socket.setdefaulttimeout(10)

cache = {}
fd = open(sys.argv[2], 'w')
for line in open(sys.argv[1]):
    fields = line.rstrip('\n').split('\t')
    sid = fields[0]
    uid = fields[1]

    #url = 'http://twitter.com/%s/status/%s' % (uid, sid)
    #print url
    tweet = None
    text = "Not Available"
    if sid in cache:
        text = cache[sid]
    else:
        try:
                f = urllib.request.urlopen("http://twitter.com/%s/status/%s" % (uid, sid))
                # print(type(f))

                # print(f.read().replace("</html>", ""))
                #Thanks to Arturo!
                # html = f.read(encoding = 'utf-8').replace("</html>", "") + "</html>"
                html = f.read()
                # print(html)
                soup = BeautifulSoup(html, "html5lib")

                jstt   = soup.find_all("p", "js-tweet-text")
                # print(jstt)
                tweets = list(set([x.get_text() for x in jstt]))

                #print len(tweets)
                # print(tweets)
                if(len(tweets)) > 1:
                    continue

                text = tweets[0]
                # print(text)
                cache[sid] = tweets[0]

                for j in soup.find_all("input", "json-data", id="init-data"):
                        js = json.loads(j['value'])
                        if("embedData" in js):
                                tweet = js["embedData"]["status"]
                                text  = js["embedData"]["status"]["text"]
                                cache[sid] = text
                                break
        except Exception:
            print('Error')
            continue

        if(tweet != None and tweet["id_str"] != sid):
                text = "Not Available"
                cache[sid] = "Not Available"
        text = text.replace('\n', ' ',)
        text = re.sub(r'\s+', ' ', text)
        #print json.dumps(tweet, indent=2)
        string = str("\t".join(fields + [text]).encode('utf-8'))+'\n'
        string = string.replace('b"','')
        string = string.replace("b'",'')
        string = string.replace("'\n",'\n')
        string = string.replace('"\n', '\n')
        string = string.replace('\t','  ')
        fd.write(string)
        print (string)
