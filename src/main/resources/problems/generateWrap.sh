#!/bin/sh

set -e
set -x

echo "Generating for storecredit"
CMD="python src/main/resources/problems/storecredit/generate.py"
TRG="target/classes/problems/storecredit/"

#	If one file is present, consider them all generated.
#
[ -f $TRG/10000.txt ] && exit 0
$CMD 1 10000 10000 	> $TRG/10000.txt
$CMD 1 50000 100000 	> $TRG/100000.txt
$CMD 1 100000 1000000 	> $TRG/1000000.txt
