#!/bin/bash

if [ $# -lt 2 ]; then
	echo "Using:" $0 "{HadoopDir}" "{NodeType: NN/DN}"; exit
else
	if [ ! -d $1 ]; then
		echo  "---- [ERROR 3.4] $1 does not exist! ----"; exit
	elif [ ! -x $1 ]; then
		echo  "---- [ERROR 3.4] $1 permission error! ----"; exit
	fi

	if [ "$2" != "DN" ] && [ "$2" != "NN" ]; then
		echo  "---- [ERROR 3.4] Unknown Node type ----"; exit
	fi

	echo "---- [3.4] Start Hadoop ----";
	if [ ! -d /var/hadoop/hadoop-root/dfs/name ]; then
		echo "---- [3.4] Formating and start Hadoop Node ($2)... ----"
		$1/bin/hadoop namenode -format
		if [ "$2" = "NN" ]; then
			$1/bin/hadoop-daemon.sh start namenode
			$1/bin/hadoop-daemon.sh start jobtracker
		elif [ "$2" = "DN" ]; then
			$1/bin/hadoop-daemon.sh start datanode
			$1/bin/hadoop-daemon.sh start tasktracker
		fi
	fi
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
