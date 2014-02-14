#!/bin/bash
#
# (C) Copyright 2013 The CloudDOE Project and others.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# 
# Contributors:
#      Wei-Chun Chung (wcchung@iis.sinica.edu.tw)
#      Yu-Chun Wang (zxaustin@iis.sinica.edu.tw)
# 
# CloudDOE Project:
#      http://clouddoe.iis.sinica.edu.tw/
#
if [ $# -lt 2 ]; then
	echo "Using:" $0 "{directory of install script} {package name}"; exit
fi

if [ -x /usr/bin/lsb_release ]; then
	DID=`lsb_release -i | awk -F '\t' '{print $2}' | tr '[:upper:]' '[:lower:]'`

	if [ "$DID" = "ubuntu" ] || [ -x /usr/bin/apt ] ; then
		sudo "$1/ubuntu.sh" "$2"
	else
		echo "---- [ERROR] We does not support $DID now. ----"; exit
	fi
else
	FILTER=`find /etc/*-release -type f | wc -l`

	if [ ! -z $FILTER ]; then
		VER=`cat < \`find /etc/*-release -type f | awk 'NR==1{print $1}'\` | awk -F 'release' '{print $1}' | tr '[:upper:]' '[:lower:]'`
		echo "---- [ERROR] $VER is under construction. ----"; exit
	else
		echo "---- [ERROR] Cannot determine OS distribution. Discard installation. ----"; exit
	fi
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
