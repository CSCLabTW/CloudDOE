#!/bin/bash

# Author: Wei-Chun Chung <wcchung@iis.sinica.edu.tw>
#
# A shell interface aims to install plugins for CloudBrushPack

if [ $# -lt 4 ]; then
	echo "Using:" $0 "{userIP} {userName} {userPass} {pluginName}"; exit
fi

### Prepare for installation ###
chmod +x -R .
./sudo_hack.sh "$3"

### Run installation ###
cd plugins
./$4.sh "$1" "$2" "$3"

# vim: ai ts=2 sw=2 et sts=2 ft=sh
