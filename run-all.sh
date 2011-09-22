#!/bin/sh

for app in admin coreref services
do
	cd $app
	grails run-app &
	cd ..
done
