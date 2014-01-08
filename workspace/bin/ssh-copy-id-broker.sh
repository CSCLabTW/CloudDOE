#!/bin/bash

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
