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
BACKUP_TIME=`date +%Y%m%d%H%m%S`
HOSTNAMEFILE=/etc/hostname

if [ $# -lt 1 ]; then
	echo "Using:" $0 "{hostname}"; exit
else
	echo "---- [2.1] Update hostname ----";
	sudo /bin/cp -f $HOSTNAMEFILE $HOSTNAMEFILE.$BACKUP_TIME
	echo "$1" | sudo tee $HOSTNAMEFILE
	sudo hostname -F $HOSTNAMEFILE
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
