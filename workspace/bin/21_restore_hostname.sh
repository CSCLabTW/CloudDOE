#!/bin/bash

HOSTNAMEFILE=/etc/hostname

ORIGFILE=`ls -1 $HOSTNAMEFILE.* | head -n 1`

if [ $# -lt 1 ]; then
	echo "Using:" $0 "{hostname}"; exit
else
	echo "---- [2.1] Restore hostname ----";
	sudo mv $ORIGFILE $HOSTNAMEFILE
	sudo rm -rf $HOSTNAMEFILE".20*"
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
