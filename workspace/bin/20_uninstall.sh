#!/bin/bash

if [ $# -lt 2 ]; then
	echo "Using:" $0 "{hostname} {userPass}"; exit
else
	echo "---- [2.0] Restore Stage 2 ----";
	./sudo_hack.sh "$2"
	./23_restore_keypair.sh
	./22_restore_hosts.sh
	./21_restore_hostname.sh $1
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
