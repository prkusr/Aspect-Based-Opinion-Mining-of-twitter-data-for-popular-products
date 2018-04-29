#!/bin/bash
params=" -o StrictHostKeyChecking=no "
pemfile=" -i SparkBusters.pem "
exec_cmd() {
	cmd=$1
	while IFS='' read -r line || [[ -n "$line" ]]; do
	   echo "checking $line"
	   ROLE=`echo $line | cut -d'=' -f1`
	   NODE_IP=`echo $line | cut -d'=' -f2`
   	   ssh $pemfile $params -n ubuntu@$NODE_IP $cmd
	done < ./node_details
}
config_cluster() {
	cmd="chmod +x node_exec.sh && ./node_exec.sh setup"
	MASTER_IP=`cat ./node_details | grep MASTER | cut -d'=' -f2`
	KAFKA_IP=`cat node_details | grep KAFKA | cut -d"=" -f2 | cut -d"-" -f2-5 | cut -d"." -f1 | sed 's/-/./g'`
	set_master_ip="sed \"s/MASTER_IP=/MASTER_IP=$MASTER_IP/\" -i ~/node_exec.sh"
	set_kafka_ip="sed \"s/KAFKA_IP=/KAFKA_IP=$KAFKA_IP/\" -i ~/node_exec.sh"
	sed "s/KAFKA_IP=/KAFKA_IP=$KAFKA_IP/" -i spark_settings.sh
	while IFS='' read -r line || [[ -n "$line" ]]; do
	   echo "checking $line"
	   ROLE=`echo $line | cut -d'=' -f1`
	   NODE_IP=`echo $line | cut -d'=' -f2`
	   scp $pemfile $params node_exec.sh ubuntu@$NODE_IP:~/
	   scp $pemfile $params spark_settings.sh ubuntu@$NODE_IP:~/
	   ssh $pemfile $params -n ubuntu@$NODE_IP "sudo mv ~/spark_settings.sh /etc/profile.d/" 
	   if [ $ROLE == "MASTER" ] ; 
	   then
		  ssh $pemfile $params -n ubuntu@$NODE_IP "sed 's/ROLE=\"f\"/ROLE=\"master\"/' -i ~/node_exec.sh"
	   elif [ $ROLE == "SLAVE" ] ;
           then
		  ssh $pemfile $params -n ubuntu@$NODE_IP "sed 's/ROLE=\"f\"/ROLE=\"slave\"/' -i ~/node_exec.sh"
	   elif [ $ROLE == "KAFKA" ] ;
	   then
		   scp $pemfile $params -r ../Kafka ubuntu@$NODE_IP:~/
		  ssh $pemfile $params -n ubuntu@$NODE_IP "sed 's/ROLE=\"f\"/ROLE=\"kafka\"/' -i ~/node_exec.sh"

	   fi
   	ssh $pemfile $params -n ubuntu@$NODE_IP $set_master_ip
   	ssh $pemfile $params -n ubuntu@$NODE_IP $set_kafka_ip
   	ssh $pemfile $params -n ubuntu@$NODE_IP $cmd
	done < ./node_details
}	

start_cluster()
{
	exec_cmd " ./node_exec.sh start_cluster < /dev/null &"
}

git_pull()
{

	exec_cmd "cd ~/SparkBusters/SparkBusters && git pull git@github.com:CUBigDataClass/SparkBusters.git"
}
gradle_build()
{
	exec_cmd " ./node_exec.sh gradle_build"
}
gradle_run()
{
	exec_cmd "-f ./node_exec.sh gradle_run "
}
stop_cluster()
{
	exec_cmd " -f ./node_exec.sh stop_cluster"
}

cleanup() {
	exec_cmd "rm -rf ~/SparkBusters && rm ~/node_exec.sh && rm ~/Kafka ~/kafka"
	exec_cmd "sed -i '$ d' ~/.bashrc"
}
$1
#config_cluster
#start_cluster
#stop_cluster
#cleanup
#exec_cmd "rm  ~/gradle-4.4.1-bin.zip"
#exec_cmd "ssh-keygen -f id_rsa -t rsa -N '' && mv ~/id_rsa* ~/.ssh/"
#exec_cmd "cat ~/.ssh/id_rsa.pub"
#exec_cmd "eval \"\$(ssh-agent -s)\" && ssh-add ~/.ssh/id_rsa"
#exec_cmd "cd ~/SparkBusters/SparkBusters && git pull git@github.com:CUBigDataClass/SparkBusters.git"
#exec_cmd "cd /home/ubuntu/SparkBusters/SparkBusters/spark_module/Spark_Opinion_Mining/lib && ln -s stanford-english-corenlp-2018-02-27-models.jar stanford-corenlp-3.9.1-models.jar" 
