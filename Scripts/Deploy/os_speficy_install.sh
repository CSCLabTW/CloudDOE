#!/bin/bash

if [ $# -lt 1 ]; then
	echo "Using:" $0 "{directory of install package}"; exit
fi

if [ -x /usr/bin/lsb_release ]; then
	DID=`lsb_release -i | awk -F '\t' '{print $2}' | tr '[:upper:]' '[:lower:]'`

	if [ "$DID" = "ubuntu" ] || [ -x /usr/bin/apt ] ; then
		sudo "$1/ubuntu.sh"
	else
		echo "---- [ERROR] We does not support $DID now. ----"; exit
	fi
else
	FILTER=`find /etc/*-release -type f | wc -l`

	if [ ! -z $FILTER ]; then
		VER=`cat < \`find /etc/*-release -type f | awk 'NR==1{print $1}'\` | awk -F 'release' '{print $1}' | tr '[:upper:]' '[:lower:]'`
		echo "---- [ERROR] $VER is under construction. ----"; exit
	else
		echo "---- [ERROR] Cannot determine OS distribution. Discard installation. ----"; exit
	fi
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
