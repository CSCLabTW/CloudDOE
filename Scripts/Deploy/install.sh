#!/bin/bash

# Author: Wei-Chun Chung <wcchung@iis.sinica.edu.tw>
#
# v0.1: 2012-12-28 for CloudBrush Hadoop Installation
#
# modified from the installation script written by 
# Jazz Yao-Tsung Wang <jazzwang.tw@gmail.com>.
# Check the following site for detail:
# https://github.com/jazzwang/hicloud-hadoop

WORKSPACE=../../workspace
CONFIGDIR=../config/common
NNFILE=$CONFIGDIR/NN
DNFILE=$CONFIGDIR/DN
NPFILE=$CONFIGDIR/NP
KEYFILE=$CONFIGDIR/hadoop
HOSTFILE=$CONFIGDIR/hosts

PID_FILE=~/ociinstall.pid

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

### Check for configuration files ###
if [ ! -f $NNFILE ] || [ ! -f $DNFILE ]; then
  echo "---- [ERROR] Missing configuration files ----"; exit
fi

if [ ! -f $KEYFILE ]; then
  echo "---- [ERROR] Missing ssh key files ----"; exit
fi

### Store progress pid ###
echo $$ > $PID_FILE

### Cluster node list ###
NN_IP=$(cat $NNFILE | awk '{ print $1 }')
DN_IP=$(cat $DNFILE | awk '{ print $1 }')

### Configuration of public NN IP ###
NN_PUB=$NN_IP
if [ -f $NPFILE ]; then
	NN_PUB=$(cat $NPFILE | awk '{ print $1 }')
fi

### Check NN IP ###
VALIDIP=`/sbin/ifconfig | grep "inet addr" | sed -e 's/ .*inet addr://' -e 's/ .*//' | grep "$NN_IP"`
if [ ! $VALIDIP ]; then
	echo "---- [ERROR] We cannot verify the network address of your PCs. ----"
	echo "---- [ERROR] Your network environment may have special settings. ----"
	echo "---- [ERROR] Please contact to your MIS manager for this issue. ----"
	rm $PID_FILE; exit
fi

### Prepare for installation ###
chmod +x -R .
chmod 600 $KEYFILE
./sudo_hack.sh "`userPass $NN_IP $NNFILE`"
./os_speficy_install.sh "./00_perl_modules"

### Stage 0: SSH Key Exchange ###
echo "---- [Stage 0] ----"
cat $NNFILE | while read line; do ./ssh-cp-id $line "$KEYFILE.pub"; done
cat $DNFILE | while read line; do ./ssh-cp-id $line "$KEYFILE.pub"; done

### Stage 1: Prepare installation files ###
echo "---- [Stage 1] ----"
echo "---- [Stage 1 at $NN_IP] ----";
# NN no need copy install files
for dn_ip in $DN_IP; do
	echo "---- [Stage 1 at $dn_ip] ----";
	scp -i $KEYFILE $SSHOPTIONS -r $WORKSPACE `userName $dn_ip $DNFILE`@$dn_ip:~/
done

### Stage 2: Setup hostname and hosts ###
echo "---- [Stage 2] ----"
echo "---- [Stage 2 at $NN_IP] ----";
./20_install.sh `hostName $NN_IP` "`userPass $NN_IP $NNFILE`"

for dn_ip in $DN_IP; do
	echo "---- [Stage 2 at $dn_ip] ----";
	ssh -i $KEYFILE $SSHOPTIONS -t "`userName $dn_ip $DNFILE`@$dn_ip" "cd ~/workspace/bin; ./20_install.sh `hostName $dn_ip` \"`userPass $dn_ip $DNFILE`\""
done

### Stage 3: Install Hadoop ###
echo "---- [Stage 3] ----"
echo "---- [Stage 3 at $NN_IP] ----";
./30_install.sh "$HADOOP_DIR" "NN" "`userPass $NN_IP $NNFILE`"

for dn_ip in $DN_IP; do
	echo "---- [Stage 3 at $dn_ip] ----";
	ssh -i $KEYFILE $SSHOPTIONS -t "`userName $dn_ip $DNFILE`@$dn_ip" "cd ~/workspace/bin; ./30_install.sh $HADOOP_DIR DN \"`userPass $dn_ip $DNFILE`\""
done

### Stage 4: Start Hadoop cluster ###
echo "---- [Stage 4] ----"
./40_start_hadoop.sh "$HADOOP_DIR"
echo "---- [Congrats] Please check http://$NN_PUB:50070 and http://$NN_PUB:50030 for cluster status ----"

### Finish Installation ###
rm $PID_FILE; exit

# vim: ai ts=2 sw=2 et sts=2 ft=sh
