#!/bin/bash
SPARK_VERSION="2.3.0"
KAFKA_VERION="1.1.0"
ROLE="f"
MASTER_IP=
SPARK_HOME=$HOME/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7
KAFKA_HOME=$HOME/kafka/kafka_2.11-$KAFKA_VERION
KAFKA_SCRIPTS=$HOME/Kafka
export SPARK_HOME=$SPARK_HOME
setup() {
	java -version || (sudo apt-get -y update && sudo apt-get -y install openjdk-8-jdk)
        if [ $ROLE == "master" ] || [ $ROLE == "slave" ] ; then	
		ls ~/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7.tgz || (mkdir -p ~/SparkBusters && cd ~/SparkBusters && wget http://apache.claz.org/spark/spark-$SPARK_VERSION/spark-$SPARK_VERSION-bin-hadoop2.7.tgz)
		ls ~/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7 || (cd ~/SparkBusters && tar zxvf spark-$SPARK_VERSION-bin-hadoop2.7.tgz)

		cat ~/.bashrc | grep SPARK_HOME || ( echo export SPARK_HOME="$HOME/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7/" >> ~/.bashrc)
	else
		(netstat -ant | grep :2181) || (sudo apt-get -y install zookeeperd) 
		ls ~/kafka/kafka_2.11-$KAFKA_VERION.tgz || (mkdir -p ~/kafka && cd ~/kafka &&  wget http://apache.claz.org/kafka/$KAFKA_VERION/kafka_2.11-$KAFKA_VERION.tgz)
		ls ~/kafka/kafka_2.11-$KAFKA_VERION || (cd ~/kafka && tar zxvf kafka_2.11-$KAFKA_VERION.tgz )
		cat ~/.bashrc | grep KAFKA_HOME || ( echo "export KAFKA_HOME=$KAFKA_HOME" >> ~/.bashrc)
	fi

}

start_cluster() {
	if [ $ROLE == "master" ] ; then
		echo "This is Master"
		$SPARK_HOME/sbin/start-master.sh -h $MASTER_IP &
	elif [ $ROLE == "slave" ] ; then
		echo "This is slave"
		$SPARK_HOME/sbin/start-slave.sh spark://$MASTER_IP:7077 &
	elif [ $ROLE == "kafka" ] ; then
		echo "This is kafka"
		(netstat -ant | grep 9092 | grep ESTABLISHED) || (nohup $KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties  > /dev/null &)
		($KAFKA_SCRIPTS/list-topics | grep tweets | grep -v "deletion") || ($KAFKA_SCRIPTS/create-topic tweets 1)	
		($KAFKA_SCRIPTS/list-topics | grep search_string | grep -v "deletion") || ($KAFKA_SCRIPTS/create-topic search_string 1)	
		($KAFKA_SCRIPTS/list-topics | grep opinions | grep -v "deletion") || ($KAFKA_SCRIPTS/create-topic opinions 15)	
	else
		echo "This is defualt"
	fi
}
stop_cluster() {
	if [ $ROLE == "master" ] ; then
		echo "This is Master"
		$SPARK_HOME/sbin/stop-master.sh
	elif [ $ROLE == "slave" ] ; then
		echo "This is slave"
		$SPARK_HOME/sbin/stop-slave.sh
	elif [ $ROLE == "kafka" ] ; then
		echo "This is kafka"
		$KAFKA_HOME/bin/kafka-server-stop.sh
	else
		echo "This is default"
	fi
}
$1

