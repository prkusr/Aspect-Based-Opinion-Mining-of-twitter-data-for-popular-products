import json

from kafka import KafkaConsumer, KafkaProducer
from kafka.errors import KafkaError
from search.api import Query
from constants import Constants


def json_deserializer(received_message):
    try:
        return json.loads(received_message.decode('utf-8', 'ignore'))
    except ValueError:
        return json.dumps({Constants.JSONKeys.exception: True})


if __name__ == "__main__":
    search_string_consumer = KafkaConsumer(Constants.Topics.receiving, group_id=Constants.Topics.consumer_group_name,
                                           bootstrap_servers=Constants.EnvConfig.kafka_server,
                                           value_deserializer=json_deserializer)
    tweet_producer = KafkaProducer(bootstrap_servers=Constants.EnvConfig.kafka_server,
                                   retries=Constants.EnvConfig.retries)

    for message in search_string_consumer:
        received_json = message.value
        if Constants.JSONKeys.exception in received_json:
            print('Invalid message received')
            continue

        print('Received string: ' + str(received_json))

        api_url = "{} lang:{} has:{}".format(received_json[Constants.JSONKeys.search_string], Constants.GNIP.language,
                                             Constants.GNIP.filter_param)

        if Constants.JSONKeys.optional_location in received_json:
            api_url += " place: %s" % received_json[Constants.JSONKeys.optional_location]

        print(api_url)

        g = Query(Constants.EnvConfig.gnip_api_username, Constants.EnvConfig.gnip_api_password,
                  Constants.EnvConfig.gnip_api_auth_url, paged=True, hard_max=Constants.GNIP.hard_max)
        g.execute(api_url)
        futures = []

        tweets = list(g.get_activity_set())
        total_tweets = len(tweets)
        for x in tweets:
            filtered_tweet = {k: v for (k, v) in x.items() if k in Constants.JSONKeys.requiredKeys}

            filtered_tweet[Constants.JSONKeys.search_string] = received_json[Constants.JSONKeys.search_string]
            filtered_tweet[Constants.JSONKeys.total_tweets] = total_tweets
            tweet_json_str = json.dumps(filtered_tweet)
            future = tweet_producer.send(Constants.Topics.sending, tweet_json_str.encode('ascii', 'ignore'))
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
