#!/bin/bash
SPARK_VERSION="2.3.0"
IS_MASTER_NODE="f"
MASTER_IP=
SPARK_HOME=$HOME/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7
export SPARK_HOME=$SPARK_HOME
setup() {
	java -version || (sudo apt-get -y update && sudo apt-get -y install openjdk-8-jdk)

	ls ~/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7.tgz || (mkdir -p ~/SparkBusters && cd ~/SparkBusters && wget http://apache.claz.org/spark/spark-$SPARK_VERSION/spark-$SPARK_VERSION-bin-hadoop2.7.tgz)
	ls ~/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7 || (cd ~/SparkBusters && tar zxvf spark-$SPARK_VERSION-bin-hadoop2.7.tgz)

	cat ~/.bashrc | grep SPARK_HOME || ( echo export SPARK_HOME="$HOME/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7/" >> ~/.bashrc)
}

start_cluster() {
	if [ $IS_MASTER_NODE == "true" ] ; then
		echo "This is Master"
		$SPARK_HOME/sbin/start-master.sh -h $MASTER_IP &
	else 
		echo "This is slave"
		$SPARK_HOME/sbin/start-slave.sh spark://$MASTER_IP:7077 &
	fi
}
stop_cluster() {
	if [ $IS_MASTER_NODE == "true" ] ; then
		echo "This is Master"
		$SPARK_HOME/sbin/stop-master.sh
	else 
		echo "This is slave"
		$SPARK_HOME/sbin/stop-slave.sh
	fi
}
$1

