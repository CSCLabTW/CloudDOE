#!/bin/bash

if [ $# -lt 1 ]; then
	echo "Using:" $0 "{pidFilePath}"; exit
fi

if [ ! -f $1 ]; then 
	echo "end"
else
	echo "run"
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
