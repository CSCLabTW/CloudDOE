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
if [ $# -lt 3 ]; then
	echo "Using: " $0 "<host> <username> <pubkeyfile>"; exit
fi

if [ ! -r $3 ]; then
	echo "---- [ERROR] PublicKey file($3) not found. ----"; exit
fi

if [ -x /usr/bin/ssh-copy-id ]; then
	/usr/bin/ssh-copy-id -i $3 $1\@$2
else
	/bin/cat $3 | ssh -l $2 $1 "mkdir ~/.ssh; /bin/cat >> ~/.ssh/authorized_keys"
fi
