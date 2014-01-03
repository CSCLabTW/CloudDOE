#!/bin/bash

INSTALL_DIR="/opt"
INSTALL_TGT="cloudgene"

PACKAGE_PATH="http://cloudgene.uibk.ac.at/downloads"
PACKAGE_NAME="cloudgene-0.3.0.zip"

TMP_DIR="/tmp"

PID_FILE=~/cloudgene_install.pid

if [ $# -lt 3 ]; then
	echo "Using:" $0 "{userIP} {userName} {userPass}"; exit
fi

### Store progress pid ###
echo $$ > $PID_FILE

../os_specify_install.sh "../os_specify" "unzip"

### Stage 1: Prepare installation packages ###
echo "---- [Stage 1] Fetch and check environment for cloudgene ----"
if [ ! -x $INSTALL_DIR/$INSTALL_TGT ]; then
	echo "---- [1.1] Install $INSTALL_TGT into $INSTALL_DIR ----";
	rm -rf $TMP_DIR/$PACKAGE_NAME
	echo "---- [1.2] Fetching $INSTALL_TGT packages... ----";
	wget -q -P $TMP_DIR "$PACKAGE_PATH/$PACKAGE_NAME"
	echo "---- [1.3] Decompress $PACKAGE_NAME... ----";
	unzip -q "$TMP_DIR/$PACKAGE_NAME" -d $TMP_DIR/$INSTALL_TGT
	echo "---- [1.4] Move $INSTALL_TGT into $INSTALL_DIR ----";
	sudo mv $TMP_DIR/$INSTALL_TGT $INSTALL_DIR
	sudo chmod a+w -R $INSTALL_DIR/$INSTALL_TGT
	echo "---- [1.5] Construct $PACKAGE_NAME... ----";
	sed -ri 's/^(hadoopPath: ).*/\1\/opt\/hadoop/g' $INSTALL_DIR/$INSTALL_TGT/config/settings.yaml
	rm -rf $TMP_DIR/$PACKAGE_NAME
else
	echo "---- [ERROR 1] Cloudgene installation exists! ----";
fi

### Stage 2: Start cloudgene ###
echo "---- [Stage 2] ----"
echo "---- [2.1] Prepare for running ----";
cd $INSTALL_DIR/$INSTALL_TGT/
chmod +x cloudgene
export PATH=$PATH:/opt/hadoop/bin
echo "---- [2.2] Generate administrator account ----";
./cloudgene --mode private --add-user "$2" "$3" --admin
echo "---- [2.3] Start cloudgene service ----";
./cloudgene --mode private &
echo "---- [Congrats] Please check http://"$1":8082 for $INSTALL_TGT ----"

### Finish Installation ###
rm $PID_FILE; exit

# vim: ai ts=2 sw=2 et sts=2 ft=sh
