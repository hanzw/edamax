#!/usr/bin/python

import sys, string, math

data = sys.stdin.readlines()

avgs = {}
counts = {}
stdevs = {}


# find highest iteration
maxiter = 0
for line in data:
  splitted = line.split(' ')
  if splitted[0]=="====":
    continue
  iteration = string.atoi(splitted[1])
  if iteration > maxiter:
    maxiter = iteration

# compute averages
last = 0;
for line in data:
  splitted = line.split(' ')
  if splitted[0]=="====":
    while last <= maxiter:
      if avgs.has_key(last):
        avgs[last]+=value
        counts[last]+=1
      else:
        avgs[last]=value
        counts[last]=1
      last+=1     
    last = 0
    continue
  iteration = string.atoi(splitted[1])
  value = string.atof(splitted[2])
  while last <= iteration:
    if avgs.has_key(last):
      avgs[last]+=value
      counts[last]+=1
    else:
      avgs[last]=value
      counts[last]=1
    last += 1

keys = avgs.keys()
keys.sort()

for i in keys:
  avgs[i] = avgs[i] / counts[i]

#compute variances
last = 0;
for line in data:
  splitted = line.split(' ')
  if splitted[0]=="====":
    while last <= maxiter:
      std = (value-avgs[last])*(value-avgs[last])
      if stdevs.has_key(last):
        stdevs[last]+=std
      else:
        stdevs[last]=std
      last+=1     
    last = 0
    continue
  iteration = string.atoi(splitted[1])
  value = string.atof(splitted[2])
  while last <= iteration:
    std = (value-avgs[last])*(value-avgs[last])
    if stdevs.has_key(last):
      stdevs[last]+=std
    else:
      stdevs[last]=std
    last+=1
    
for i in keys:
  stdevs[i] = math.sqrt(stdevs[i] / counts[i])
  
# print 

for i in keys:
  print i,avgs[i],stdevs[i]
