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
BACKUP_TIME=`date +%Y%m%d%H%m%S`

updateFile() {
	if [ ! -f $1/$3 ]; then
		echo "---- [ERROR 3.3] $1/$3 does not exist! ----"; exit
	else
		echo "---- [3.3] Updating $1/$3... ----"
		/bin/cp -f "$1/$3" "$1/$3.$BACKUP_TIME"
		if [ $4 = true ]; then
			/bin/cp -f "../config/$2/$3" "$1/$3"
		else
			cat "../config/$2/$3" >> "$1/$3"
		fi
	fi
}

if [ $# -lt 2 ]; then
	echo "Using:" $0 "{HadoopDir}" "{NodeType: NN/DN}"; exit
else
	if [ ! -d $1 ]; then
		echo  "---- [ERROR 3.3] $1 does not exist! ----"; exit
	elif [ ! -x $1 ]; then
		echo  "---- [ERROR 3.3] $1 permission error! ----"; exit
	fi

	if [ "$2" != "DN" ] && [ "$2" != "NN" ]; then
		echo  "---- [ERROR 3.3] Unknown Node type ----"; exit
	fi

	echo "---- [3.3] Config Hadoop ----";
	updateFile "$1/conf" "common" "hadoop-env.sh" false
	updateFile "$1/conf" "common" "masters" true
	updateFile "$1/conf" "common" "slaves" true
	updateFile "$1/conf" "$2" "core-site.xml" true
	updateFile "$1/conf" "$2" "hdfs-site.xml" true
	updateFile "$1/conf" "$2" "mapred-site.xml" true
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
