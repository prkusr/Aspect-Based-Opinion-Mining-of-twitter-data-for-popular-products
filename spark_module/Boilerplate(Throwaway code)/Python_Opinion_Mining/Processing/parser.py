from nltk.corpus import stopwords
from nltk.parse.stanford import StanfordDependencyParser
import codecs
import re
path_to_jar = '/Users/payoj/Downloads/stanford-corenlp-full-2018-02-27/stanford-corenlp-3.9.1.jar'
path_to_models_jar = '/Users/payoj/Downloads/stanford-corenlp-full-2018-02-27/stanford-corenlp-3.9.1-models.jar'
dependency_parser = StanfordDependencyParser(path_to_jar=path_to_jar, path_to_models_jar=path_to_models_jar)


stop_words = set(stopwords.words('english'))
words = ['in','on','at','a','it','is','and','or','my','i','myself']
# urls = re.findall('https?://(?:[-\w.]|(?:%[\da-fA-F]{2}))+', url)
# stop_words = set(stopwords.words('english'))

for word in words:
    stop_words.add(word)
f = codecs.open('example.txt', 'r', encoding = 'utf-8')
tweets = []
for line in f:
	if line != '\n':
	    tokens = line.strip()
	    new_tweet= ''
	    # for tok in tokens:
	    #     if tok not in stop_words or tok[0] != '@' or tok[0] != '#' or tok[:4] != 'http':
	    #         new_tweet += ' '+tok
	    new_tweet = re.sub(r'^https?:\/\/.*[\r\n]*', '', new_tweet)
	    new_tweet = tokens.replace('!','.').replace('?','.')
	    new_tweet = re.sub('\.\.+','.', new_tweet)
	    '''
	    for line in file.split('\n'):
    # Replace multiple dots with space
    line = re.sub('\.\.+', ' ', line) 
    # Remove single dots
    		line = re.sub('\.', '', line)
    print line
	    '''

	    tweets.append((tokens,new_tweet))
f.close()
fd = codecs.open('cleaned_example.txt', 'w', encoding = 'utf-8')
for tweet in tweets:
    fd.write(tweet[0]+'\t'+tweet[1]+'\n')
fd.close()



f = codecs.open('cleaned_example.txt','r', encoding = 'utf-8')
parser = []
tweets = []
for line in f:
	if line != '\n':
		content = line.strip().split('\t')
		old_tweet = content[0]
		new_tweet = content[1]
		print(content)
		result = dependency_parser.raw_parse(new_tweet)
		dep = result.next()
		depend_parser = list(dep.triples())
		opinion = ''
		for t in depend_parser:
			if t[1] == 'amod':
				if t[2][1] == 'JJ':
					opinion += ' '+t[2][0]+'-'+t[0][0]
				else:
					opinion += ' '+t[0][0]+'-'+t[2][0]
			if t[1] == 'compound' and t[0][1] != 'FW' and t[2][1] != 'FW':
					opinion += ' '+t[2][0]+'-'+t[0][0]
			if t[1] == 'number':
					 opinion += ' '+t[2][0]+'-'+t[0][0]
		opinion = opinion.strip()
		if opinion != '':
			parser.append(opinion)
			tweets.append((old_tweet,opinion))
f.close()

fw = codecs.open('opinion.txt','w',encoding='utf-8')
for p in parser:
	fw.write(p+'\n')
fw.close()

fo = codecs.open('documents.txt', 'w', encoding='utf-8')
for t in tweets:
	fo.write(t[0]+'\t'+t[1]+'\n')
fo.close()