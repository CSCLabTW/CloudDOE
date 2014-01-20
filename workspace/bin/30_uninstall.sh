#!/bin/bash

if [ $# -lt 3 ]; then
	echo "Using:" $0 "{HadoopDir}" "{NodeType: NN/DN} {userPass}"; exit
else
	if [ "$2" != "DN" ] && [ "$2" != "NN" ]; then
		echo  "---- [ERROR 3.0] Unknown Node type ----"; exit
	fi

	echo "---- [3.0] Restore Stage 3 ----";
	./sudo_hack.sh "$3"
	# no need to config again, just delete it (33)
	./32_uninstall_hadoop.sh $1 $2
	# no need to uninstall java now (31)
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
