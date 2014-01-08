#!/bin/bash

PID_FILE=~/ociinstall.pid

if [ ! -f $PID_FILE ]; then 
	echo "end"
else
	echo "run"
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
