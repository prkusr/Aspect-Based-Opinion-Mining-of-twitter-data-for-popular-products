# from pyspark.ml.clustering import LDA
# # $example off$
# from pyspark.sql import SparkSession

# if __name__ == "__main__":
#     spark = SparkSession \
#         .builder \
#         .appName("LDAExample") \
#         .getOrCreate()

#     # $example on$
#     # Loads data.
#     dataset = spark.read.format("libsvm").load("example.csv")

#     # Trains a LDA model.
#     lda = LDA(k=10, maxIter=10)
#     model = lda.fit(dataset)

#     ll = model.logLikelihood(dataset)
#     lp = model.logPerplexity(dataset)
#     print("The lower bound on the log likelihood of the entire corpus: " + str(ll))
#     print("The upper bound on perplexity: " + str(lp))

#     # Describe topics.
#     topics = model.describeTopics(3)
#     print("The topics described by their top-weighted terms:")
#     topics.show(truncate=False)

#     # Shows the result
#     transformed = model.transform(dataset)
#     transformed.show(truncate=False)
#     # $example off$

#     spark.stop()
import codecs
from nltk.corpus import stopwords
from pyspark.sql import SparkSession
from pyspark.sql import SQLContext, Row
from pyspark.ml.feature import CountVectorizer
from pyspark.mllib.clustering import LDA, LDAModel
from pyspark.mllib.linalg import Vector, Vectors
from pyspark import SparkContext, SparkConf
conf = SparkConf().setAppName("pyspark")
sc = SparkContext(conf=conf)
path = "example.txt"
if __name__ == "__main__":
    spark = SparkSession \
        .builder \
        .appName("LDAExample") \
        .getOrCreate()
    # stop_words = set(stopwords.words('english'))
    # words = ['in','on','at','a','it','is','and','or','my','i','myself']
    # for word in words:
    #     stop_words.add(word)
    # f = codecs.open(path, 'r', encoding = 'utf-8')
    # tweets = []
    # for line in f:
    #     tokens = line.strip().split()
    #     new_tweet= ''
    #     for tok in tokens:
    #         if tok not in stop_words or tok[0] != '@' or tok[0] != '#' or tok[:4] != 'http':
    #             new_tweet += ' '+tok
    #     new_tweet.strip()
    #     tweets.append(new_tweet)
    # f.close()
    # fd = codecs.open('cleaned_example.txt', 'w', encoding = 'utf-8')
    # for tweet in tweets:
    #     fd.write(tweet+'\n')

    rdd = sc.textFile('opinion.txt').zipWithIndex().map(lambda (words,idd): Row(idd= idd, words = words.split(" ")))
    docDF = spark.createDataFrame(rdd)
    Vector = CountVectorizer(inputCol="words", outputCol="vectors")
    model = Vector.fit(docDF)
    result = model.transform(docDF)

    corpus = result.select("idd", "vectors").rdd.map(lambda (x,y): [x,Vectors.fromML(y)]).cache()

    # Cluster the documents into three topics using LDA
    ldaModel = LDA.train(corpus, k=5,maxIterations=100,optimizer='online')
    topics = ldaModel.topicsMatrix()
    vocabArray = model.vocabulary

    wordNumbers = 4  # number of words per topic
    topicIndices = sc.parallelize(ldaModel.describeTopics(maxTermsPerTopic = wordNumbers))

    def topic_render(topic):  # specify vector id of words to actual words
        terms = topic[0]
        result = []
        for i in range(wordNumbers):
            term = vocabArray[terms[i]]
            result.append(term)
        return result

    topics_final = topicIndices.map(lambda topic: topic_render(topic)).collect()

    for topic in range(len(topics_final)):
        print ("Topic" + str(topic) + ":")
        for term in topics_final[topic]:
            print (term)
        print ('\n')
    spark.stop()