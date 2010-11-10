#!/bin/sh

####################################################
# experiments with various number of collaborators #
####################################################

echo "Running Experiments..."
tmp="/tmp/collaborators/"
output="/home/ksulliv/vo/edatest/exp/collaborators/"
run="java -Xmx1g -classpath ../"
s=107
for i in 1 2 4 8 16 32
do 
	name=${tmp}leadingones_${i}_alpha_1
	if [ ! -e $name.dat ]
	then
		${run} -Dcmla.samples=${i} \
			-Dseed=${s} \
			-Dalpha=1 \
			-Dproblem=LeadingOnesProblem \
			Application cmla_collaborators.config 2>&1 1>${name}.dat | tee ${name}.log
	fi
	
	mv $name.* ${output}
	s=$(($s+1))
	
	name=${tmp}leadingones_${i}_alpha_0.05
	if [ ! -e $name.dat ]
	then
		${run} -Dcmla.samples=${i} \
			-Dseed=${s} \
			-Dcmla.alpha=0.05 \
			-Dproblem=LeadingOnesProblem \
			Application cmla_collaborators.config 2>&1 1>${name}.dat | tee ${name}.log
	fi
	
	mv $name.* ${output}
	s=$(($s+1))
done