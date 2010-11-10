#!/bin/sh

####################################################
# experiments with various number of collaborators #
####################################################

echo "Running Experiments..."
tmp="/tmp/comparisons/"
output="/home/ksulliv/vo/edatest/exp/comparisons/"
run="java -Xmx1g -classpath ../"
s=45645
alpha=0.05

for i in 1 2 4 8 16
do
	name=${tmp}leadingones_cmla_${i}_alpha_${alpha}
	if [ ! -e $name.dat ]
	then
		${run} -Dseed=${s} \
			-Dcmla.samples=${i} \
			-Dcmla.alpha=${alpha} \
			-Dalgorithm=CMLA \
			-Dproblem=LeadingOnesProblem \
			Application leadingones.config 2>&1 1>${name}.dat | tee ${name}.log
	fi
	mv $name.* ${output}
	s=$(($s+1))
done

