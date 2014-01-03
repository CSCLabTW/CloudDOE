#!/bin/bash

if [ $# -lt 1 ]; then
	echo "Using:" $0 "{packageName}"; exit
fi

if [ `dpkg -l $1 2> /dev/null | grep '^ii' | wc -l` -ne 0 ]; then
	echo "---- [0.0] $1 installed. Skip. ----"; exit
fi

echo "---- Install $1. This may take a long time... ----"
apt-get -qq update
apt-get -qqy install $1

# vim: ai ts=2 sw=2 et sts=2 ft=sh
