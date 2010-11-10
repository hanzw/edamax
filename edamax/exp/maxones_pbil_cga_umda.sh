#!/bin/sh

####################################################
# experiments with various number of collaborators #
####################################################

echo "Running Experiments..."
tmp="/tmp/comparisons/"
output="/home/ksulliv/vo/edatest/exp/comparisons/"
run="java -Xmx1g -classpath ../"
s=1051

name=${tmp}maxones_pbil
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=PBIL \
		-Dproblem=MaxOnesProblem \
		Application maxones.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))

name=${tmp}maxones_cga
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=CGA \
		-Dproblem=MaxOnesProblem \
		Application maxones.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))

name=${tmp}maxones_umda
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=UMDA \
		-Dproblem=MaxOnesProblem \
		Application maxones.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))