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
if [ $# -lt 1 ]; then
	echo "Using:" $0 "{HadoopDir}"; exit
else
	if [ ! -d $1 ]; then
		echo  "---- [ERROR 4] $1 does not exist! ----"; exit
	elif [ ! -x $1 ]; then
		echo  "---- [ERROR 4] $1 permission error! ----"; exit
	fi

        echo "---- [4] Start Hadoop Cluster ----";
        if [ ! -d /var/hadoop/hadoop-$(id -u -n)/dfs/name ]; then
                echo "---- [4.1] Formating Namenode... ----"
                if [ -e $1/bin/yarn ]; then
                    $1/bin/hdfs namenode -format
                else
                    $1/bin/hadoop namenode -format
                fi
        else
                echo "---- [4.1] Namenode is formatted. Skip... ----"
        fi

        echo "---- [4.2] Starting DFS... ----"
        if [ -e $1/bin/yarn ]; then
            $1/sbin/start-dfs.sh
        else
            $1/bin/start-dfs.sh
        fi
        echo "---- [4.3] Starting MapReduce Framework... ----"
        if [ -e $1/bin/yarn ]; then
            $1/sbin/start-yarn.sh
        else
            $1/bin/start-mapred.sh
        fi
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
