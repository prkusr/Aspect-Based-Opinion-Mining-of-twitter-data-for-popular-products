from nltk.corpus import wordnet as wn
from nltk.parse.stanford import StanfordDependencyParser
import codecs
import re
import cat_dict
path_to_jar = '/Users/payoj/Downloads/stanford-corenlp-full-2018-02-27/stanford-corenlp-3.9.1.jar'
path_to_models_jar = '/Users/payoj/Downloads/stanford-corenlp-full-2018-02-27/stanford-corenlp-3.9.1-models.jar'
dependency_parser = StanfordDependencyParser(path_to_jar=path_to_jar, path_to_models_jar=path_to_models_jar)


def process_tweet(tweet):
    new_tweet = re.sub(r'^https?:\/\/.*[\r\n]*', '', tweet)
    new_tweet = new_tweet.replace('!', '.').replace('?', '.')
    new_tweet = re.sub(r'#(\w+)', '', tweet)
    new_tweet = re.sub(r'\.\.+', '.', new_tweet)
    return new_tweet


def parsing(tweet):
    print(tweet)
    result = dependency_parser.raw_parse(tweet)
    dep = list(result)[0]
    depend_parser = list(dep.triples())
    # print depend_parser
    return depend_parser

def get_term(parsed_list):
    aspect_term = {'NN': [], 'JJ': []}
    for relation in parsed_list:
    	# if relation[0][0] == 'lens' or relation[2][0] =='lens' or relation[0][0] == 'camera' or relation[2][0] == 'camera' or relation[0][0] == 'pic' or relation[2][0] == 'pic' or relation[0][0] == 'photograph' or relation[2][0] == 'photograph':
	        if relation[1] == 'amod' or relation[1] == 'advmod':  # Adj/ adv as aspect aspect_term
	            if relation[2][1] == 'RB' or relation[0][1] == 'RB':
	                # aspect_term['NN'].append((relation[0][0], relation[2][0]))
	                aspect_term['JJ'].append(relation[0][0])
	            else:
	                aspect_term['NN'].append((relation[2][0], relation[0][0]))
	                aspect_term['JJ'].append(relation[0][0])
	        if relation[1] == 'nmod':
	        	if relation[2][1] == 'NN' and relation[0][1] == 'NN':
	        		aspect_term['NN'].append((relation[0][0], relation[2][0]))
	        		# aspect_term['JJ'].append(relation[2][0])
	        if relation[1] == 'nsubj':  # Make noun as aspect term. // check for auxiliary verb
	            # for i, term in enumerate(relation):
	            #     if term[1] == 'NN':
	            #         aspect_term['NN'].append((term[0], relation[2 - i][0]))
	            #     if term[1] == 'JJ' or term[1] == 'VBP':
	            #         aspect_term['JJ'].append(term[0])
	           	if (relation[2][1] == 'NN' and relation[0][1] == 'JJ'):
	           		aspect_term['NN'].append((relation[2][0],relation[0][0]))
	           		aspect_term['JJ'].append(relation[0][0])
	           	if (relation[2][1] == 'JJ' and relation[0][1] == 'NN') :
	           		aspect_term['NN'].append((relation[0][0]),relation[2][0])
	           		aspect_term['JJ'].append(relation[2][0])

	        if relation[1] == 'dobj':
	        	if relation[0][1] == 'VBP':
	        		aspect_term['JJ'].append(relation[0][0])
	        		aspect_term['NN'].append((relation[2][0],relation[0][0]))

	           	# if relation[0][1] == 'NN' and relation[2][1] == 'NN':
	           	# 	aspect_term['NN'].append()
	        if relation[1] == 'advcl':
	            aspect_term['JJ'].append(relation[2][0])
	        if relation[1] == 'xcomp':
	        	aspect_term['JJ'].append(relation[0][0])
	        	aspect_term['JJ'].append(relation[2][0])
	        	aspect_term['NN'].append((relation[2][0], relation[0][0]))
	        	aspect_term['NN'].append((relation[0][0], relation[2][0]))
    aspect_term['JJ'] = list(set(aspect_term['JJ']))
    for rel in parsed_list:
       	if rel[1] == 'conj' and rel[0][0] in aspect_term['JJ']:
            aspect_term['JJ'].append(rel[2][0])
    aspect_term['JJ'] = list(set(aspect_term['JJ']))
    print aspect_term['JJ']
    return aspect_term['JJ']

tweets = []
f = codecs.open('example.txt', 'r', encoding='utf-8')
for line in f:
    line = line.strip()
    tweets.append(line)

f.close()
dictionary = []
for tweet in tweets:
	processed_tweet = process_tweet(tweet)
	relation = parsing(processed_tweet)
	aspects = get_term(relation)
	dictionary += aspects

dictionary = list(set(dictionary))
print(dictionary)