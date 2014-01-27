#!/bin/bash

# Author: Wei-Chun Chung <wcchung@iis.sinica.edu.tw>
#
# v0.1: 2012-12-28 for CloudBrush Hadoop Installation
# v1.0: 2014-01-20 for CloudDOE Deploy

CONFIGDIR=../config/common
NNFILE=$CONFIGDIR/NN
DNFILE=$CONFIGDIR/DN
NPFILE=$CONFIGDIR/NP
KEYFILE=$CONFIGDIR/hadoop
HOSTFILE=$CONFIGDIR/hosts

PID_FILE=~/ociuninstall.pid

HADOOP_DIR=/opt/hadoop

SSHOPTIONS="-o ConnectTimeout=10 -o BatchMode=yes -o StrictHostKeyChecking=no"

hostName() {
	echo `grep "$1" $HOSTFILE | awk -F '\t' '{ print $2 }'`
}

userName() {
	echo `grep "$1" $2 | awk -F '\t' '{ print $2 }'`
}

userPass() {
	echo `grep "$1" $2 | awk -F '\t' '{ print $3 }'`
}

### Start Installation ###
echo $$ > $PID_FILE
sleep 5

### Check for configuration files ###
if [ ! -f $NNFILE ] || [ ! -f $DNFILE ]; then
  echo "---- [ERROR] Missing configuration files ----"
  rm $PID_FILE; exit
fi

if [ ! -f $KEYFILE ]; then
  echo "---- [ERROR] Missing ssh key files ----"
  rm $PID_FILE; exit
fi

### Cluster node list ###
NN_IP=$(cat $NNFILE | awk '{ print $1 }')
DN_IP=$(cat $DNFILE | awk '{ print $1 }')

### Prepare for installation ###
chmod +x -R .

### Stage 4: Stop Hadoop cluster ###
echo "---- [Stage 4] ----"
./40_stop_hadoop.sh "$HADOOP_DIR"

### Stage 3: Uninstall Hadoop ###
echo "---- [Stage 3] ----"

for dn_ip in $DN_IP; do
	echo "---- [Stage 3 at $dn_ip] ----";
	ssh -i $KEYFILE $SSHOPTIONS -t "`userName $dn_ip $DNFILE`@$dn_ip" "cd ~/workspace/bin; ./30_uninstall.sh $HADOOP_DIR DN \"`userPass $dn_ip $DNFILE`\""
done

echo "---- [Stage 3 at $NN_IP] ----";
./30_uninstall.sh "$HADOOP_DIR" "NN" "`userPass $NN_IP $NNFILE`"

### Stage 2: Restore hostname and hosts ###
echo "---- [Stage 2] ----"

for dn_ip in $DN_IP; do
	echo "---- [Stage 2 at $dn_ip] ----";
	ssh -i $KEYFILE $SSHOPTIONS -t "`userName $dn_ip $DNFILE`@$dn_ip" "cd ~/workspace/bin; ./20_uninstall.sh `hostName $dn_ip` \"`userPass $dn_ip $DNFILE`\""
done

echo "---- [Stage 2 at $NN_IP] ----";
./20_uninstall.sh `hostName $NN_IP` "`userPass $NN_IP $NNFILE`"

### Stage 1: Remove installation files ###
echo "---- [Stage 1] ----"
for dn_ip in $DN_IP; do
	echo "---- [Stage 1 at $dn_ip] ----";
	ssh -i $KEYFILE $SSHOPTIONS -t "`userName $dn_ip $DNFILE`@$dn_ip" "rm -rf ~/workspace/"
done

echo "---- [Stage 1 at $NN_IP] ----";
# Data in NN will be removed from GUI

### Finish Installation ###
echo "---- [Congrats] Your Hadoop cluster is successfully removed ----"
sleep 5
rm $PID_FILE; exit

# vim: ai ts=2 sw=2 et sts=2 ft=sh
