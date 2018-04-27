import sys
import os
import re
import subprocess
import shlex
from collections import defaultdict

regex = 'https?://(?:[-\w.]|(?:%[\da-fA-F]{2}))'
keywords = ['iphone', 'blackberry', 'nokia', 'palmpre', 'sony', 'motorola', 'canon', 'nikon', 'dell', 'lenovo',
            'toshiba', 'acer', 'asus', 'macbook', 'hp', 'alienware', 'camera', 'laptop', 'tablet', 'netbook',
            'ipad', 'ipod', 'xbox', 'playstation', 'wii', 'phone', 'nintendo', 'printer', 'panasonic', 'epson',
            'samsung', 'kyocera', 'ibm', 'sony', 'microsoft', 'lg', 'hitachi', 'scanner', 'computer', 'fujitsu',
            'kodak', 'gameboy', 'sega', 'squareenix', 'android', 'ios', 'windows', 'operating system', 'apple']
def read_product_tweets():

    f = open('senti140.csv', 'r')
    cache = defaultdict(list)
    for line in f:
        line = line.strip('\n')
        tweet = line.split(',')[5]
        tweet = tweet.lower()

        urls = re.findall("http[s]?://(?:[a-zA-Z]|[0-9]|[~$-_@.&+]|[!*\(\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+", tweet)
        for url in urls:
            tweet.replace(url, '')
        if tweet != '':
            sentiment = line.split(',')[0]
            user = line.split(',')[4]
            for keyword in keywords:
                if keyword in tweet:
                    cache[keyword].append((tweet,sentiment))

    fd = open('product_tweets.csv','w')

    for key in cache:
        print(key, len(cache[key]))
        for tuple in cache[key]:
            fd.write(key+'\t'+tuple[0]+'\t'+tuple[1]+'\n')

def runTagger():
    # subprocess.call('twitterNLP')
    # subprocess.call('ls')
    file = open('tweettags.txt', 'w')
    subprocess.call(shlex.split('/twitterNLP/./runTagger.sh --output-format conll examples/example_tweets.txt'), stdout=file)

runTagger()