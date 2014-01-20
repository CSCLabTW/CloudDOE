#!/bin/bash

if [ $# -lt 1 ]; then
	echo "Using:" $0 "{HadoopDir}"; exit
else
	if [ -x $1 ]; then
		echo "---- [3.2] Remove running environment ----";
		if [ -x /var/hadoop ]; then
			sudo rm -rf /var/hadoop
		fi

		echo "---- [3.2] Remove Hadoop from $1 ----";
		sudo rm -rf $1
	else
		echo "---- [ERROR 3.2] Hadoop directory not exist! ----"; exit
	fi
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
