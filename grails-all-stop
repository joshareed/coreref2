#!/bin/sh

for app in `ps | grep -i grails | grep -v grep | awk '{print $1}'`
do
	kill $app
done
