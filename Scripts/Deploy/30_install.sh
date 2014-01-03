#!/bin/bash

if [ $# -lt 3 ]; then
	echo "Using:" $0 "{HadoopDir}" "{NodeType: NN/DN} {userPass}"; exit
else
	if [ "$2" != "DN" ] && [ "$2" != "NN" ]; then
		echo  "---- [ERROR 3.0] Unknown Node type ----"; exit
	fi

	echo "---- [3.0] Start Stage 3 ----";
	./sudo_hack.sh "$3"
	./os_specify_install.sh "./31_install_java"
	./32_install_hadoop.sh $1 $2
	./33_config_hadoop.sh $1 $2
	./34_start_hadoop.sh $1 $2
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
