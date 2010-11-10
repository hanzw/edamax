#!/bin/sh

####################################################
# experiments with various number of collaborators #
####################################################

echo "Running Experiments..."
tmp="/tmp/comparisons/"
output="/home/ksulliv/vo/edatest/exp/comparisons/"
run="java -Xmx1g -classpath ../"
s=1051
alpha=1

for i in 1 2 4 8 16
do
	name=${tmp}maxones_cmla_${i}_alpha_${alpha}
	if [ ! -e $name.dat ]
	then
		${run} -Dseed=${s} \
			-Dcmla.samples=${i} \
			-Dcmla.alpha=${alpha} \
			-Dalgorithm=CMLA \
			-Dproblem=MaxOnesProblem \
			Application maxones.config 2>&1 1>${name}.dat | tee ${name}.log
	fi
	mv $name.* ${output}
	s=$(($s+1))
done

