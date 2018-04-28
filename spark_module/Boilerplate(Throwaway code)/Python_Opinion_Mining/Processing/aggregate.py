import codecs
from nltk.corpus import stopwords


topic_dict = dict()
for i in range(5):
	f =  codecs.open('Topic'+str(i)+'.txt','r',encoding = 'utf-8')
	for line in f:
		topic = line.strip()
		topic_dict[topic] = (i,[])


f =  codecs.open('documents.txt','r',encoding = 'utf-8')

for line in f:
	content = line.strip().split('\t')
	tweet = content[0]
	opinion = content[1]
	opinion_tokens = opinion.split()
	for op in opinion_tokens:
		if op in topic_dict:
			topic_dict[op][1].append(tweet)
			


for topic in topic_dict:
	f = codecs.open('Topic'+str(topic_dict[topic][0])+'_tweets.txt', 'a', encoding = 'utf-8')
	f.write('\n\n\n'+topic.upper()+'\n\n')
	tweets = topic_dict[topic][1]
	for tweet in tweets:
		f.write(tweet+'\n')
	f.close()
		
