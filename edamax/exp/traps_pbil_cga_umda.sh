#!/bin/sh

####################################################
# experiments with various number of collaborators #
####################################################

echo "Running Experiments..."
tmp="/tmp/comparisons/"
output="/home/ksulliv/vo/edatest/exp/comparisons/"
run="java -Xmx1g -classpath ../"
s=67867

name=${tmp}traps_pbil
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=PBIL \
		-Dproblem=SimpleTrapProblem \
		Application traps.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))

name=${tmp}traps_cga
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=CGA \
		-Dproblem=SimpleTrapProblem \
		Application traps.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))

name=${tmp}traps_umda
if [ ! -e $name.dat ]
then
	${run} -Dseed=${s} \
		-Dalgorithm=UMDA \
		-Dproblem=SimpleTrapProblem \
		Application traps.config 2>&1 1>${name}.dat | tee ${name}.log
fi
mv $name.* ${output}
s=$(($s+1))