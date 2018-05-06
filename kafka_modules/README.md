#Kafka Modules

###Tweets Producer
This module is responsible for making the gnip api calls to twitter's Enterprise API. This module consumes what has been input into the kafka topic "search_string" and this query string is used as query string to the api. The recieved tweets are then input to the kafka queue "tweets".

