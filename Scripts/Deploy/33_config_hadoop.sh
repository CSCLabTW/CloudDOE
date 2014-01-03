#!/bin/bash

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
	updateFile "$1/conf" "$2" "core-site.xml" true
	updateFile "$1/conf" "$2" "hdfs-site.xml" true
	updateFile "$1/conf" "$2" "mapred-site.xml" true
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
