#!/bin/bash

#HADOOP_WWW=http://archive.apache.org/dist
HADOOP_WWW=http://ftp.twaren.net/Unix/Web/apache
HADOOP_VER=hadoop-0.22.0
TMP_DIR=/tmp

if [ $# -lt 1 ]; then
	echo "Using:" $0 "{HadoopDir}"; exit
else
	if [ ! -x $1 ]; then
		echo "---- [3.2] Install Hadoop $HADOOP_VER into $1 ----";
		rm -rf $TMP_DIR/$HADOOP_VER*
		echo "---- [3.2] Fetch Hadoop from apache ----";
		wget -q -P $TMP_DIR "$HADOOP_WWW/hadoop/core/$HADOOP_VER/$HADOOP_VER.tar.gz"
		echo "---- [3.2] Decompress $HADOOP_VER ----";
		tar zxpf $TMP_DIR/$HADOOP_VER.tar.gz -C $TMP_DIR
		echo "---- [3.2] Move $HADOOP_VER into $1 ----";
		sudo mv $TMP_DIR/$HADOOP_VER $1
		sudo chmod a+w -R $1
		echo "---- [3.2] Build running environment ----";
		if [ ! -x /var/hadoop ]; then
			sudo mkdir -p /var/hadoop
			sudo chmod a+w /var/hadoop
		fi
		rm -rf $TMP_DIR/$HADOOP_VER*
	else
		echo "---- [ERROR 3.2] Hadoop directory exists! ----"; exit
	fi
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
