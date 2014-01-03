#!/bin/bash

BACKUP_TIME=`date +%Y%m%d%H%m%S`
HOSTSFILE=/etc/hosts
HOSTCONFIG=../config/common/hosts

echo "---- [2.2] Update hosts ----";
sudo /bin/cp -f $HOSTSFILE $HOSTSFILE.$BACKUP_TIME
sudo /bin/cp -f $HOSTCONFIG $HOSTSFILE

# vim: ai ts=2 sw=2 et sts=2 ft=sh
