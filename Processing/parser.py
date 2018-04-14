from nltk.corpus import stopwords
from nltk.parse.stanford import StanfordDependencyParser
import codecs
path_to_jar = '/Users/payoj/Downloads/stanford-corenlp-full-2018-02-27/stanford-corenlp-3.9.1.jar'
path_to_models_jar = '/Users/payoj/Downloads/stanford-corenlp-full-2018-02-27/stanford-corenlp-3.9.1-models.jar'
dependency_parser = StanfordDependencyParser(path_to_jar=path_to_jar, path_to_models_jar=path_to_models_jar)


stop_words = set(stopwords.words('english'))
words = ['in','on','at','a','it','is','and','or','my','i','myself']
for word in words:
    stop_words.add(word)
f = codecs.open('example.txt', 'r', encoding = 'utf-8')
tweets = []
for line in f:
	if line != '\n':
	    tokens = line.strip().split()
	    new_tweet= ''
	    for tok in tokens:
	        if tok not in stop_words or tok[0] != '@' or tok[0] != '#' or tok[:4] != 'http':
	            new_tweet += ' '+tok
	    new_tweet.strip()
	    tweets.append(new_tweet)
f.close()
fd = codecs.open('cleaned_example.txt', 'w', encoding = 'utf-8')
for tweet in tweets:
    fd.write(tweet+'\n')
fd.close()



f = codecs.open('cleaned_example.txt','r', encoding = 'utf-8')
parser = []
for line in f:
	content = line.strip()
	print(content)
	result = dependency_parser.raw_parse(content)
	dep = result.next()
	depend_parser = list(dep.triples())
	opinion = ''
	for t in depend_parser:
		if t[1] == 'amod':
			if t[2][1] == 'JJ':
				opinion += ' '+t[2][0]+'-'+t[0][0]
			else:
				opinion += ' '+t[0][0]+'-'+t[2][0]
		if t[1] == 'compound':
				opinion += ' '+t[2][0]+'-'+t[0][0]
		if t[1] == 'number':
				 opinion += ' '+t[2][0]+'-'+t[0][0]
	opinion = opinion.strip()
	parser.append(opinion)
f.close()

fd = codecs.open('opinion.txt','w',encoding='utf-8')
for p in parser:
	fd.write(p+'\n')
