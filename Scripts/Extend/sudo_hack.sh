#!/bin/bash

if [ $# -lt 1 ]; then
	echo "Using:" $0 "{userPass}"; exit
else
	echo "$1" | sudo -S -p '' echo -n
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
