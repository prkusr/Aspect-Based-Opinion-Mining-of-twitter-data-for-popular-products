import json

from kafka import KafkaConsumer, KafkaProducer
from kafka.errors import KafkaError
from search.api import Query

# Kafka Conf
# TODO: Externalise configs
from constants import Constants


def json_deserializer(received_message):
    try:
        return json.loads(received_message.decode('utf-8', 'ignore'))
    except ValueError:
        return json.dumps({Constants.JSONKeys.exception(): True})


search_string_consumer = KafkaConsumer(Constants.Topics.receiving(), group_id=Constants.Topics.consumer_group_name(),
                                       bootstrap_servers=Constants.EnvConfig.kafka_server(),
                                       value_deserializer=json_deserializer)
tweet_producer = KafkaProducer(bootstrap_servers=Constants.EnvConfig.kafka_server(),
                               retries=Constants.EnvConfig.retries())

# Twitter/Gnip API conf
language = 'en'
filter_param = 'geo'
# TODO: Discuss later!
max_results = 10

for message in search_string_consumer:
    received_json = message.value
    if Constants.JSONKeys.exception() in received_json:
        print('Invalid message received')
        continue

    print('Received string: ' + str(received_json))

    api_url = "{} lang:{} has:{}".format(received_json[Constants.JSONKeys.search_string()], language, filter_param)

    if Constants.JSONKeys.optional_location() in received_json:
        api_url += " place: %s" % received_json[Constants.JSONKeys.optional_location()]

    print(api_url)

    g = Query(Constants.EnvConfig.gnip_api_username(), Constants.EnvConfig.gnip_api_password(),
              Constants.EnvConfig.gnip_api_auth_url())
    g.execute(api_url, max_results)
    futures = []

    tweets = list(g.get_activity_set())
    total_tweets = len(tweets)
    for x in tweets:
        x[Constants.JSONKeys.search_string()] = received_json
        x[Constants.JSONKeys.total_tweets()] = total_tweets
        tweet_json_str = json.dumps(x)
        future = tweet_producer.send(Constants.Topics.sending(), tweet_json_str.encode('ascii', 'ignore'))
        futures.append(future)

    for i, future in enumerate(futures):
        try:
            record_metadata = future.get(timeout=10)
        except KafkaError:
            # Error occurred Tweet Json not sent!
            # TODO: Figure out what to do in this case.
            print('Tweet number %i was not sent to Kafka' % i)
            pass
    print("Python Module: Tweet send to Spark Module")
