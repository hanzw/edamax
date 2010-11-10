#!/bin/sh

####################################################
# experiments with various number of collaborators #
####################################################

echo "Running Experiments..."
tmp="/tmp/comparisons/"
output="/home/ksulliv/vo/edatest/exp/comparisons/"
run="java -Xmx1g -classpath ../"
s=23051

name=${tmp}lobproblem_pbil
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=PBIL \
		-Dproblem=LOBProblem \
		Application lobproblem.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))

name=${tmp}lobproblem_cga
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=CGA \
		-Dproblem=LOBProblem \
		Application lobproblem.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))

name=${tmp}lobproblem_umda
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=UMDA \
		-Dproblem=LOBProblem \
		Application lobproblem.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))