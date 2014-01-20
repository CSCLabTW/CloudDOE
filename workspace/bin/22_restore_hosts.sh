#!/bin/bash

HOSTSFILE=/etc/hosts

ORIGFILE=`ls -1 $HOSTSFILE.20* | head -n 1`

echo "---- [2.2] Restore hosts ----";
sudo mv $ORIGFILE $HOSTSFILE
sudo rm -rf $HOSTSFILE".20*"

# vim: ai ts=2 sw=2 et sts=2 ft=sh
