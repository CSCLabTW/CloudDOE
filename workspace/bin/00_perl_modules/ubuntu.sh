#!/bin/bash

PACKAGE=libexpect-perl

if [ `dpkg -l $PACKAGE 2> /dev/null | grep '^ii' | wc -l` -ne 0 ]; then
	echo "---- [0.0] libexpect-perl installed. Skip. ----"; exit
fi

echo "---- [0.0] Install perl expect modules. This may take a long time... ----"
apt-get -qq update
apt-get -qqy install $PACKAGE

# vim: ai ts=2 sw=2 et sts=2 ft=sh
