#!/bin/sh

####################################################
# experiments with various number of collaborators #
####################################################

echo "Running Experiments..."
tmp="/tmp/comparisons/"
output="/home/ksulliv/vo/edatest/exp/comparisons/"
run="java -Xmx1g -classpath ../"
s=3471051
alpha=0.05

for i in 1 2 4 8 16
do
	name=${tmp}lobproblem_cmla_${i}_alpha_${alpha}
	if [ ! -e $name.dat ]
	then
		${run} -Dseed=${s} \
			-Dcmla.samples=${i} \
			-Dcmla.alpha=${alpha} \
			-Dalgorithm=CMLA \
			-Dproblem=LOBProblem \
			Application lobproblem.config 2>&1 1>${name}.dat | tee ${name}.log
	fi
	mv $name.* ${output}
	s=$(($s+1))
done

