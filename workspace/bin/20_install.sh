#!/bin/bash

if [ $# -lt 2 ]; then
	echo "Using:" $0 "{hostname} {userPass}"; exit
else
	echo "---- [2.0] Start Stage 2 ----";
	./sudo_hack.sh "$2"
	./21_update_hostname.sh $1
	./22_update_hosts.sh
	./23_update_keypair.sh
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
