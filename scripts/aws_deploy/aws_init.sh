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
	set_master_ip="sed \"s/MASTER_IP=/MASTER_IP=$MASTER_IP/\" -i ~/node_exec.sh"
	while IFS='' read -r line || [[ -n "$line" ]]; do
	   echo "checking $line"
	   ROLE=`echo $line | cut -d'=' -f1`
	   NODE_IP=`echo $line | cut -d'=' -f2`
	   scp $pemfile $params node_exec.sh ubuntu@$NODE_IP:~/
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
   	ssh $pemfile $params -n ubuntu@$NODE_IP $cmd
	done < ./node_details
}	

start_cluster()
{
	exec_cmd " ./node_exec.sh start_cluster < /dev/null &"
}

stop_cluster()
{
	exec_cmd " -f ./node_exec.sh stop_cluster"
}

cleanup() {
	exec_cmd "rm -rf ~/SparkBusters && rm ~/node_exec.sh"
	exec_cmd "sed -i '$ d' ~/.bashrc"
}
$1
#config_cluster
#start_cluster
#stop_cluster
#cleanup
#exec_cmd "rm ~/cluster_exec.sh"
