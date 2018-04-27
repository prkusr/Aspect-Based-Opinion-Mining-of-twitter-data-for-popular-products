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
	cmd="chmod +x node_exec.sh && ./node_exec.sh"
	while IFS='' read -r line || [[ -n "$line" ]]; do
	   echo "checking $line"
	   ROLE=`echo $line | cut -d'=' -f1`
	   NODE_IP=`echo $line | cut -d'=' -f2`
	   scp $pemfile $params node_exec.sh ubuntu@$NODE_IP:~/
	   if [ $ROLE == "MASTER" ] ; 
	   then
		  ssh $pemfile $params -n ubuntu@$NODE_IP "sed 's/IS_MASTER_NODE=\"f\"/IS_MASTER_NODE=\"true\"/' -i ~/node_exec.sh"
	   fi
   	ssh $pemfile $params -n ubuntu@$NODE_IP $cmd
	done < ./node_details
}	

cleanup() {
	exec_cmd "rm -rf ~/SparkBusters && rm ~/node_exec.sh"
	exec_cmd "sed -i '$ d' ~/.bashrc"
}

config_cluster
#cleanup
#exec_cmd "rm ~/cluster_exec.sh"
