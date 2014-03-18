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
        echo "Using:" $0 "{HadoopDir} {NN Public IP}"; exit
else
        if [ ! -d $1 ]; then
                echo  "---- [ERROR 5] $1 does not exist! ----";
                echo  "---- [ERROR] Your Hadoop Cloud is NOT successfully deployed ----";
                exit
        fi

        echo "---- [5] Check Hadoop Cloud Status ----";

        NN=`jps | grep "\bNameNode" | wc -l`
        if [ $NN ]; then
            echo "---- [5.1] DFS starts successfully. Please check http://$2:50070 for status ----";
        else
            echo "---- [ERROR 5.1] DFS does not start ----";
        fi

        if [ -e $1/bin/yarn ]; then
            MR=`jps | grep "\bResourceManager" | wc -l`
            if [ $MR ]; then
                echo "---- [5.2] YARN starts successfully. Please check http://$2:8088 for status ----";
            else
                echo "---- [ERROR 5.2] DFS does not start ----";
            fi
        else
            MR=`jps | grep "\bJobTracker" | wc -l`
            if [ $MR ]; then
                echo "---- [5.2] MapReduce starts successfully. Please check http://$2:50030 for status ----";
            else
                echo "---- [ERROR 5.2] DFS does not start ----";
            fi
        fi
fi

# vim: ai ts=2 sw=2 et sts=2 ft=sh
