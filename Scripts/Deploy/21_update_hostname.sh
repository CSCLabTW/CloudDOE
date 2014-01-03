#!/bin/bash

BACKUP_TIME=`date +%Y%m%d%H%m%S`
HOSTNAMEFILE=/etc/hostname

if [ $# -lt 1 ]; then
	echo "Using:" $0 "{hostname}"; exit
else
	echo "---- [2.1] Update hostname ----";
	sudo /bin/cp -f $HOSTNAMEFILE $HOSTNAMEFILE.$BACKUP_TIME
	echo "$1" | sudo tee $HOSTNAMEFILE
	sudo hostname -F $HOSTNAMEFILE
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
