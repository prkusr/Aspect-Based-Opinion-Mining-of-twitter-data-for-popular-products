from search.api import Query

import json
from kafka.errors import KafkaError
from kafka import KafkaConsumer, KafkaProducer

# Kafka Conf
# TODO: Externalise configs
kafka_server = ['localhost:9092']
receiving_topic = 'search_string'
sending_topic = 'tweets'
consumer_group_name = 'TwitterSearchString'

search_string_consumer = KafkaConsumer(receiving_topic, group_id=consumer_group_name, bootstrap_servers=kafka_server)
tweet_producer = KafkaProducer(bootstrap_servers=kafka_server, value_serializer=lambda m: json.dumps(m).encode('ascii'),
                               retries=3)

# Twitter/Gnip API conf
language = 'en'
filter_param = 'geo'
max_results = 10

for message in search_string_consumer:
    search_string = message.value.decode('utf-8')
    print('Received string: ' + search_string)
    g = Query("n*******u", "#!******!#",
              "https://***/****/**.json")
    g.execute("{} lang:{} has:{}".format(search_string, language, filter_param), max_results)

    futures = []
    for x in g.get_activity_set():
        tweet_json_str = json.dumps(x)
        future = tweet_producer.send(sending_topic, tweet_json_str)
        futures.append(future)

    for i, future in enumerate(futures):
        try:
            record_metadata = future.get(timeout=10)
        except KafkaError:
            # Error occurred Tweet Json not sent!
            # TODO: Figure out what to do in this case.
            print('Tweet number %i was not sent to Kafka' % i)
            pass
