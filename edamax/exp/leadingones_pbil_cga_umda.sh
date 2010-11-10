#!/bin/sh

####################################################
# experiments with various number of collaborators #
####################################################

echo "Running Experiments..."
tmp="/tmp/comparisons/"
output="/home/ksulliv/vo/edatest/exp/comparisons/"
run="java -Xmx1g -classpath ../"
s=55464

name=${tmp}leadingones_pbil
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=PBIL \
		-Dproblem=LeadingOnesProblem \
		Application leadingones.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))

name=${tmp}leadingones_cga
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=CGA \
		-Dproblem=LeadingOnesProblem \
		Application leadingones.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))

name=${tmp}leadingones_umda
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=UMDA \
		-Dproblem=LeadingOnesProblem \
		Application leadingones.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))