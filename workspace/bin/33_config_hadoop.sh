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
	if [ ! -f $1/$3 ] && [ ! -f $1/$3.template ]; then
		echo "---- [ERROR 3.3] $1/$3 does not exist! ----"; exit
	else
		echo "---- [3.3] Updating $1/$3... ----"
		/bin/cp -f "$1/$3" "$1/$3.$BACKUP_TIME"
		if [ $4 = true ]; then
			if [ $5 = true ]; then
				/bin/cp -f "../config/$2/$3.yarn" "$1/$3"
			else
				/bin/cp -f "../config/$2/$3" "$1/$3"
			fi
		else
			if [ $5 = true ]; then
				cat "$1/$3" >> "../config/$2/$3.yarn"
				/bin/cp -f "../config/$2/$3.yarn" "$1/$3"
			else
				cat "$1/$3" >> "../config/$2/$3"
				/bin/cp -f "../config/$2/$3" "$1/$3"
			fi
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
	if [ -e $1/bin/yarn ]; then
		updateFile "$1/etc/hadoop" "common" "hadoop-env.sh" false false
		updateFile "$1/etc/hadoop" "common" "yarn-env.sh" false false
		updateFile "$1/etc/hadoop" "common" "slaves" true false
		updateFile "$1/etc/hadoop" "$2" "core-site.xml" true true
		updateFile "$1/etc/hadoop" "$2" "hdfs-site.xml" true true
		updateFile "$1/etc/hadoop" "$2" "yarn-site.xml" true false
		updateFile "$1/etc/hadoop" "$2" "mapred-site.xml" true true
	else
		updateFile "$1/conf" "common" "hadoop-env.sh" false false
		updateFile "$1/conf" "common" "masters" true false
		updateFile "$1/conf" "common" "slaves" true false
		updateFile "$1/conf" "$2" "core-site.xml" true false
		updateFile "$1/conf" "$2" "hdfs-site.xml" true false
		updateFile "$1/conf" "$2" "mapred-site.xml" true false
	fi
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
