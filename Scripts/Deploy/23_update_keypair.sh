#!/bin/bash

BACKUP_TIME=`date +%Y%m%d%H%m%S`
SSHKEY=../config/common/hadoop
SSHLOC=$HOME/.ssh
SSHFILE=id_rsa

echo "---- [2.3] Update SSH keypair ----";
mkdir -p $SSHLOC
/bin/cp -f "$SSHLOC/$SSHFILE" "$SSHLOC/$SSHFILE.$BACKUP_TIME"
/bin/cp -f $SSHKEY "$SSHLOC/$SSHFILE"

# vim: ai ts=2 sw=2 et sts=2 ft=sh
