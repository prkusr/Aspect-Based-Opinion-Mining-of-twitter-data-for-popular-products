import os


class Constants:
    class EnvConfig:
        @staticmethod
        def gnip_api_username():
            return os.environ['TWITTER_UNAME']

        @staticmethod
        def gnip_api_password():
            return os.environ['TWITTER_PASS']

        @staticmethod
        def gnip_api_auth_url():
            return os.environ['TWITTER_URL']

        @staticmethod
        def kafka_server():
            return [str(os.environ['KAFKA_IP']) + ":" + os.environ["KAFKA_PORT"]]

        @staticmethod
        def retries():
            return [os.environ['RETRIES']]

    class JSONKeys:
        @staticmethod
        def optional_location():
            return 'location'

        @staticmethod
        def search_string():
            return 'searchString'

        @staticmethod
        def total_tweets():
            return 'totalTweets'

        @staticmethod
        def exception():
            return 'exception'

    class Topics:
        @staticmethod
        def receiving():
            return 'search_string'

        @staticmethod
        def sending():
            return 'tweets'

        @staticmethod
        def consumer_group_name():
            return 'TwitterSearchString'
