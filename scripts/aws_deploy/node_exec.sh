#!/bin/bash
SPARK_VERSION="2.3.0"
IS_MASTER_NODE="f"
java -version || (sudo apt-get -y update && sudo apt-get -y install openjdk-8-jdk)

ls ~/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7.tgz || (mkdir -p ~/SparkBusters && cd ~/SparkBusters && wget http://apache.claz.org/spark/spark-$SPARK_VERSION/spark-$SPARK_VERSION-bin-hadoop2.7.tgz)
ls ~/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7 || (cd ~/SparkBusters && tar zxvf spark-$SPARK_VERSION-bin-hadoop2.7.tgz)

cat ~/.bashrc | grep SPARK_HOME || ( echo export SPARK_HOME="~/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7/" >> ~/.bashrc)
export SPARK_HOME="~/SparkBusters/spark-$SPARK_VERSION-bin-hadoop2.7/"

if [ $IS_MASTER_NODE == "true" ] ; then
	echo "This is Master"	
fi

