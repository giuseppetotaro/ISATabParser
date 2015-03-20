#!/bin/bash
#
# Script     : run.sh
# Usage      : ./run.sh /path/to/output_dir
# Author     : Giuseppe Totaro
# Date       : 03/19/2015 [MM-DD-YYYY]
# Last Edited: 
# Description: This scripts runs the ISATab parsers over sample ISA-Tab files. 
#              It generates a ISATab.log, file in the given directory, that 
#              includes the parsed data.
# Notes      : Run this script from its folder by typing ./run.sh
#

if [ $# -lt 1 ] || [ ! -d $1 ]
then
	echo "Usage: $0 /path/to/output_dir (output_dir must be a directory!)"
	printf "\n\tExample: $0 /tmp/output\n"
	exit 1
fi

DATA_DIR="BII-I-1"
OUT_DIR=$1

if [ ! -d $DATA_DIR ]
then
	echo "Error: input data not found!"
	exit 1
fi

java -cp ./:./lib/tika-app-1.8-SNAPSHOT.jar::./lib/commons-csv-1.1.jar:./bin TestISATabInvestigation $DATA_DIR/i_investigation.txt >> $OUT_DIR/ISATab.log 
java -cp ./:./lib/tika-app-1.8-SNAPSHOT.jar::./lib/commons-csv-1.1.jar:./bin TestISATabStudy $DATA_DIR/s_BII-S-1.txt >> $OUT_DIR/ISATab.log 
java -cp ./:./lib/tika-app-1.8-SNAPSHOT.jar::./lib/commons-csv-1.1.jar:./bin TestISATabAssay $DATA_DIR/a_metabolome.txt >> $OUT_DIR/ISATab.log 
