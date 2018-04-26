#!/usr/bin/env python
# from nltk.corpus import stopwords

# from textblob import TextBlob
from nltk.corpus import wordnet as wn
from nltk.parse.stanford import StanfordDependencyParser
import codecs
import re
import cat_dict
path_to_jar = '/Users/payoj/Downloads/stanford-corenlp-full-2018-02-27/stanford-corenlp-3.9.1.jar'
path_to_models_jar = '/Users/payoj/Downloads/stanford-corenlp-full-2018-02-27/stanford-corenlp-3.9.1-models.jar'
dependency_parser = StanfordDependencyParser(path_to_jar=path_to_jar, path_to_models_jar=path_to_models_jar)


category_dictionary = cat_dict.category_dict()

def process_tweet(tweet):
	# for tweet in tweets:
	new_tweet = re.sub(r'^https?:\/\/.*[\r\n]*', '', tweet)
	new_tweet = new_tweet.replace('!','.').replace('?','.')
	new_tweet = re.sub(r'#(\w+)', '', tweet)
	new_tweet = re.sub(r'\.\.+','.', new_tweet)
	# processed_tweet.append(new_tweet)
	return new_tweet

def parsing(tweet):
	print tweet
	result = dependency_parser.raw_parse(tweet)
	dep = result.next()
	depend_parser = list(dep.triples())
	# print depend_parser
	return depend_parser

def get_term(parsed_list):
	aspect_term = {'NN':[], 'JJ':[]}
	# print parsed_list
# (tags[1]=='VB' or tags[1]=='VBD' or tags[1]=='VBG' or tags[1]=='VBN' or tags[1]=='VBP' or tags[1]=='VBZ'):
	for relation in parsed_list:
		
		if relation[1] == 'amod' or relation[1] == 'advmod': # Adj/ adv as aspect aspect_term

			if relation[2][1] == 'JJ' and relation[0][1] != 'RB':
				aspect_term['NN'].append((relation[0][0],relation[2][0]))
				aspect_term['JJ'].append(relation[2][0])
			else:
				aspect_term['NN'].append((relation[2][0],relation[0][0]))
				aspect_term['JJ'].append(relation[0][0])
		
		if relation[1] == 'dobj' or relation[1] == 'nsubj': 	# Make noun as aspect term. // check for auxiliary verb
			# print 'Hello'
			for i, term in enumerate(relation):
				if term[1] == 'NN':
					aspect_term['NN'].append((term[0], relation[2-i][0]))
				if term[1] == 'JJ':
					aspect_term['JJ'].append(term[0])
		if relation[1] == 'advcl':
			# print 'Jain'
			aspect_term['JJ'].append(relation[2][0])
		if relation[1] == 'xcomp':
			# print 'hELLO'
			aspect_term['JJ'].append(relation[0][0])
			aspect_term['JJ'].append(relation[2][0])
		if 'compound' in relation [1] and relation[0][1] != 'FW' and relation[2][1] != 'FW':

				# opinion += ' '+t[2][0]+'-'+t[0][0]
				if relation[2][1] != 'NN':
					aspect_term['JJ'].append(relation[0][0]+'_'+relation[2][0])
				else:
					aspect_term['NN'].append(relation[2][0]+' '+relation[0][0])
	# aspect_term['NN'] = list(set(aspect_term['NN']))
	aspect_term['JJ'] = list(set(aspect_term['JJ']))

	for relation in parsed_list:
		if relation[1]=='conj' and relation[0][0] in aspect_term:
			if relation[0][1] == 'JJ':
				aspect_term.append(relation[2][0])

	# aspect_term['NN'] = list(set(aspect_term['NN']))
	aspect_term['JJ'] = list(set(aspect_term['JJ']))

	# print aspect_term
	return aspect_term

def get_aspect_category(aspect_term):
	tweet_aspect_categories = {}
	for adj in aspect_term['JJ']:
		aspect_category_list = []
		
		for nn in aspect_term['NN']:
			aspect_category_flag = -100
			overall_aspect_category = ''
			if len(nn)> 1 and nn[1] == adj:
				aspect = nn[0]
			
				print 'Aspect, word', aspect, nn[1]
				for aspect_category in category_dictionary:
					if adj in category_dictionary[aspect_category]:
						# if aspect_category == '':
						# 	aspect_category = 'appearance'
						aspect_category_list.append(aspect_category)
				
					
						# aspect_category_flag = max(aspect_category_flag,detect_similarity(aspect, aspect_category))
						score = detect_similarity(aspect, aspect_category)
						if aspect_category_flag < score:
							overall_aspect_category = aspect_category
							aspect_category_flag = score
							print aspect_category_flag
						# if aspect_category_flag != 0:
			if overall_aspect_category != '':
				if overall_aspect_category not in tweet_aspect_categories:
					tweet_aspect_categories[overall_aspect_category] = []
				tweet_aspect_categories[overall_aspect_category].append(nn[0])
	print tweet_aspect_categories
	return tweet_aspect_categories

def detect_similarity(aspect_term, aspect_category):
	
	#print
	#print value, wn.synsets(value)[0]
	print aspect_term, aspect_category
	try:
		
		aspect_vector = wn.synsets(aspect_term)[0]

	except:
		return 0

	aspect_category_vector = wn.synsets(aspect_category)[0]
	# try:
	# 	wn_term = wn.synsets(term)[0]
	# except:
	# 	#print term
	# 	return

	# max_score = -100
	# max_sim = ''
	# for cat, value in synset_categories.iteritems():
	sim_score = wn.wup_similarity(aspect_category_vector, aspect_vector)
	print sim_score
		#print cat, term, sim_score
	if sim_score >= 0 and sim_score != None:
		return sim_score
	return 0
	# 		max_score = sim_score
	# 		max_sim = cat

	# temp = []
	# temp.append(max_sim)
	# temp.append(max_score)

	# if temp[1] >= 0.5:
	# 	return temp
	# else:
		# return

def get_aspect_sentiment(adj):
	sentiment_score = TextBlob(adj).sentiment.polarity
	return sentiment_score


def get_category_sentiment(aspect_dict):
	aspect_category_sentiment = {}
	for cat in aspect_dict:
		score = 0
		for word in aspect_dict[cat]:
			score += get_aspect_sentiment(word)
		if score > 0:
			aspect_category_sentiment[cat] = 'Positive: '+ str(score)
		if score < 0:
			aspect_category_sentiment[cat] = 'Negative: '+ str(score)
		else:
			aspect_category_sentiment[cat] = 'Neutral: '+ str(score)

	return aspect_category_sentiment

tweets = []
f = codecs.open('example.txt', 'r', encoding = 'utf-8')
count = 0
for line in f:
	line = line.strip()
	tweets.append(line)
	count += 1
	if count >= 4:
		break
f.close()

for tweet in tweets:
	processed_tweet = process_tweet(tweet)
	relation = parsing(processed_tweet)
	aspects = get_term(relation)
	if aspects != {}:
		category = get_aspect_category(aspects)
	# tweet_category_sentiment = get_category_sentiment(category)
	# print(tweet_category_sentiment)
	# print(category)
