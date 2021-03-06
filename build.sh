#!/bin/bash
#
# Script     : build.sh
# Usage      : ./build.sh
# Author     : Giuseppe Totaro
# Date       : 03/19/2015 [MM-DD-YYYY]
# Last Edited: 
# Description: This scripts compiles all .java files for the ISA-Tab parsers.
# Notes      : Run this script from its folder by typing ./build.sh
#

if [ ! -e lib/tika-app-1.7.jar ]
then
	echo "Error: this program requires Apache Tika 1.7 library!"
	echo "Please provide \"tika-app-1.7.jar\" file in the \"lib\" folder and try again."
	exit 1
fi

mkdir -p bin

for file in $(find . -name "*.java" -print)
do
	javac -cp ./:./lib/tika-app-1.7.jar:./lib/commons-csv-1.1.jar:./lib/junit-4.12.jar:./src -d ./bin ${file}
done
