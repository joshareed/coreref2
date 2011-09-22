#!/bin/sh

for app in admin common coreref services
do
	cd $app
	grails test-app
	cd ..
done
