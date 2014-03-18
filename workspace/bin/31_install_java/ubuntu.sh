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
echo "---- [3.1] Install Java environment  ----"

echo "---- [3.1.1] Update JAVA_HOME config ----"
sed -i -e 's/JAVA_HOME.*/JAVA_HOME=\"\/usr\/lib\/jvm\/java-6-oracle\"/' ../config/common/hadoop-env.sh
sed -i -e 's/JAVA_HOME.*/JAVA_HOME=\"\/usr\/lib\/jvm\/java-6-oracle\"/' ../config/common/yarn-env.sh

if [ ! -x /usr/bin/java ]; then
	echo "---- [3.1.2] Update system software settings. This may take a long time... ----"
	apt-get -qq update
	apt-get -qqy install python-software-properties
	add-apt-repository -y ppa:webupd8team/java
	cat << EOF | sudo /usr/bin/debconf-set-selections
oracle-java6-installer shared/accepted-oracle-license-v1-1 select true
EOF
	echo "---- [3.1.3] Fetch and install Java. This may take a long time... ----"
	apt-get -qq update
	apt-get -qqy install oracle-java6-installer
	echo "---- [3.1.4] Update Java environment settings... ----"
	update-java-alternatives -s java-6-oracle
else
	JVER=`java -version 2>&1 | awk 'NR==1{gsub(/"/,""); print $3}'`
	echo "---- [3.1.2] Java $JVER installed. Skip. ----"; exit
fi
