#!/bin/bash
#
# (C) Copyright 2013 The CloudDOE Project and others.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# 
# Contributors:
#      Wei-Chun Chung (wcchung@iis.sinica.edu.tw)
#      Yu-Chun Wang (zxaustin@iis.sinica.edu.tw)
# 
# CloudDOE Project:
#      http://clouddoe.iis.sinica.edu.tw/
#
HADOOP_WWW="http://archive.apache.org/dist"
HADOOP_VER="hadoop-0.20.203.0"
HADOOP_TAR="rc1.tar.gz"
TMP_DIR="/tmp"

if [ $# -lt 1 ]; then
	echo "Using:" $0 "{HadoopDir}"; exit
else
	if [ ! -x $1 ]; then
		echo "---- [3.2] Install Hadoop $HADOOP_VER into $1 ----";
		rm -rf $TMP_DIR/$HADOOP_VER*
		echo "---- [3.2] Fetching Hadoop packages... ----";
		wget -q -P $TMP_DIR "$HADOOP_WWW/hadoop/core/$HADOOP_VER/$HADOOP_VER$HADOOP_TAR"
		echo "---- [3.2] Decompress $HADOOP_VER... ----";
		tar zxpf $TMP_DIR/$HADOOP_VER$HADOOP_TAR -C $TMP_DIR
		echo "---- [3.2] Move $HADOOP_VER into $1 ----";
		sudo mv $TMP_DIR/$HADOOP_VER $1
		sudo chmod a+w -R $1
		echo "---- [3.2] Construct running environment ----";
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
