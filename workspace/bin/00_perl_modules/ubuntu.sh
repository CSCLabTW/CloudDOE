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
PACKAGE=libexpect-perl

if [ `dpkg -l $PACKAGE 2> /dev/null | grep '^ii' | wc -l` -ne 0 ]; then
	echo "---- [0.0] libexpect-perl installed. Skip. ----"; exit
fi

echo "---- [0.0] Install perl expect modules. This may take a long time... ----"
apt-get -qq update
apt-get -qqy install $PACKAGE

# vim: ai ts=2 sw=2 et sts=2 ft=sh
