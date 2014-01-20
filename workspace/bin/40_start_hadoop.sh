#!/bin/bash

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
                $1/bin/hadoop namenode -format
        else
                echo "---- [4.1] Namenode is formatted. Skip... ----"
        fi

        echo "---- [4.2] Starting DFS... ----"
        $1/bin/start-dfs.sh
        echo "---- [4.3] Starting MapReduce Framework... ----"
        $1/bin/start-mapred.sh	
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
