#!/bin/bash

if [ $# -lt 1 ]; then
	echo "Using:" $0 "{HadoopDir}"; exit
else
	if [ ! -d $1 ]; then
		echo  "---- [ERROR 4] $1 does not exist! ----"; exit
	elif [ ! -x $1 ]; then
		echo  "---- [ERROR 4] $1 permission error! ----"; exit
	fi

        echo "---- [4] Stop Hadoop Cluster ----";

        echo "---- [4.3] Stoping MapReduce Framework... ----"
        $1/bin/stop-mapred.sh
        echo "---- [4.2] Stoping DFS... ----"
        $1/bin/stop-dfs.sh

	# There is no 4.1 restore step
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
