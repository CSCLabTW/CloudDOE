#!/bin/bash

SSHLOC=$HOME/.ssh
SSHFILE=id_rsa

ORIGFILE=`ls -1 $SSHLOC/$SSHFILE.* | head -n 1`

echo "---- [2.3] Restore SSH keypair ----";
mv $ORIGFILE "$SSHLOC/$SSHFILE"
rm -rf "$SSHLOC/$SSHFILE"".20*"

# vim: ai ts=2 sw=2 et sts=2 ft=sh
