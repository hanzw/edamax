# CMLA varied sample sizes on MaxOnes, Generations, alpha = 1
set yrange [50:100]
set xrange [0:70]
set key bottom right
set xlabel "Generations"
set ylabel "Fitness (Best-So-Far)"
set term postscript enhanced eps monochrome "Times" 20
set output "finalplots/maxones_cmla_collaborators_alpha_1_gens.eps"

plot 'collaborators/maxones_1_alpha_1.dat.gens' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_4_alpha_1.dat.gens' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_16_alpha_1.dat.gens' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_1_alpha_1.dat.gens' every 2 using 1:2:(1.95*$3) with yerrorbars lt 1 pt 4 title "N=1", \
'collaborators/maxones_4_alpha_1.dat.gens' every 2 using 1:2:(1.95*$3) with points lt 1 pt 2 title "N=4", \
'collaborators/maxones_16_alpha_1.dat.gens' every 2 using 1:2:(1.95*$3) with yerrorbars lt 1 pt 6 title "N=16"


# CMLA varied sample sizes on MaxOnes, Evaluations, alpha = 1
reset
set parametric
set yrange [50:100]
set xrange [0:224000]
set trange [60:100]
set key bottom right
set xlabel "Evaluations"
set ylabel "Fitness (Best-So-Far)"
set term postscript enhanced eps monochrome "Times" 20
set output "finalplots/maxones_cmla_collaborators_alpha_1_evals.eps"

plot 'collaborators/maxones_1_alpha_1.dat.evals' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_4_alpha_1.dat.evals' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_16_alpha_1.dat.evals' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_1_alpha_1.dat.evals' every 5000 using 1:2:(1.95*$3) with points lt 1 pt 4 title "N=1", \
'collaborators/maxones_4_alpha_1.dat.evals' every 5000 using 1:2:(1.95*$3) with points lt 1 pt 2 title "N=4", \
'collaborators/maxones_16_alpha_1.dat.evals' every 10000 using 1:2:(1.95*$3) with points lt 1 pt 6 title "N=16", \
20000 lt 2 notitle, t lt 2 title "N=1 cutoff", \
80000 lt 5 notitle, t lt 5 title "N=4 cutoff"

# CMLA varied sample sizes on MaxOnes, Generations, alpha = 0.05
reset
set yrange [50:100]
set xrange [0:400]
set key bottom right
set xlabel "Generations"
set ylabel "Fitness (Best-So-Far)"
set term postscript enhanced eps monochrome "Times" 20
set output "finalplots/maxones_cmla_collaborators_alpha_0.05_gens.eps"

plot 'collaborators/maxones_1_alpha_0.05.dat.gens' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_4_alpha_0.05.dat.gens' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_16_alpha_0.05.dat.gens' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_1_alpha_0.05.dat.gens' every 20 using 1:2:(1.95*$3) with yerrorbars lt 1 pt 4 title "N=1", \
'collaborators/maxones_4_alpha_0.05.dat.gens' every 20 using 1:2:(1.95*$3) with yerrorbars lt 1 pt 2 title "N=4", \
'collaborators/maxones_16_alpha_0.05.dat.gens' every 20 using 1:2:(1.95*$3) with yerrorbars lt 1 pt 6 title "N=16"


# CMLA varied sample sizes on MaxOnes, Evaluations, alpha = 0.05
reset
set yrange [50:100]
set xrange [0:600000]
set key bottom right
set xlabel "Evaluations"
set ylabel "Fitness (Best-So-Far)"
set term postscript enhanced eps monochrome "Times" 20
set output "finalplots/maxones_cmla_collaborators_alpha_0.05_evals.eps"

plot 'collaborators/maxones_1_alpha_0.05.dat.evals' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_4_alpha_0.05.dat.evals' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_16_alpha_0.05.dat.evals' using 1:2 with lines lt 1 notitle, \
'collaborators/maxones_1_alpha_0.05.dat.evals' every 20000 using 1:2:(1.95*$3) with yerrorbars lt 1 pt 4 title "N=1", \
'collaborators/maxones_4_alpha_0.05.dat.evals' every 20000 using 1:2:(1.95*$3) with yerrorbars lt 1 pt 2 title "N=4", \
'collaborators/maxones_16_alpha_0.05.dat.evals' every 20000 using 1:2:(1.95*$3) with yerrorbars lt 1 pt 6 title "N=16"

# PBIL vs CMLA 16-1: MaxOnesProblem, Gens
reset
set yrange [50:100]
set key bottom right
set xlabel "Generations"
set ylabel "Fitness (Best-So-Far)"
set term postscript enhanced eps monochrome "Times" 20
set output "finalplots/maxones_pbil_vs_cmla_16_alpha_1_gens.eps"

plot 'comparisons/maxones_pbil.dat.gens' using 1:2 with lines lt 1 notitle, \
'comparisons/maxones_cmla_16_alpha_1.dat.gens' using 1:2 with lines lt 1 notitle, \
'comparisons/maxones_pbil.dat.gens' every 2 using 1:2:($3*1.96) with yerrorbars lt 1 pt 4 title "PBIL: 20 samples, selection=2", \
'comparisons/maxones_cmla_16_alpha_1.dat.gens' every 2 using 1:2:($3*1.96) with yerrorbars lt 1 pt 6 title "CMLA: N=16"

# PBIL vs CMLA 16-1: MaxOnesProblem, Evals
reset
set xrange [0:200000]
set yrange [50:100]
set key bottom right
set xlabel "Evaluations"
set ylabel "Fitness (Best-So-Far)"
set term postscript enhanced eps monochrome "Times" 20
set output "finalplots/maxones_pbil_vs_cmla_16_alpha_1_evals.eps"

plot 'comparisons/maxones_pbil.dat.evals' using 1:2 with lines lt 1 notitle, \
'comparisons/maxones_cmla_16_alpha_1.dat.evals' using 1:2 with lines lt 1 notitle, \
'comparisons/maxones_pbil.dat.evals' every 5000 using 1:2:($3*1.96) with yerrorbars lt 1 pt 4 title "PBIL: 20 samples, selection size=2", \
'comparisons/maxones_cmla_16_alpha_1.dat.evals' every 5000 using 1:2:($3*1.96) with yerrorbars lt 1 pt 6 title "CMLA: N=16"

# PBIL vs CMLA 8-1: LOBProblem, Gens
reset
set yrange [0:20]
set xrange [0:1300]
set key bottom right
set xlabel "Generations"
set ylabel "Fitness (Best-So-Far)"
set term postscript enhanced eps monochrome "Times" 20
set output "finalplots/lobproblem_pbil_vs_cmla_8_alpha_1_gens.eps"

plot 'comparisons/lobproblem_pbil.dat.gens' using 1:2 with lines lt 1 notitle, \
'comparisons/lobproblem_cmla_8_alpha_1.dat.gens' using 1:2 with lines lt 1 notitle, \
'comparisons/lobproblem_pbil.dat.gens' every 40 using 1:2:($3*1.96) with yerrorbars lt 1 pt 4 title "PBIL: 100 samples, selection size=10", \
'comparisons/lobproblem_cmla_8_alpha_1.dat.gens' every 40 using 1:2:($3*1.96) with yerrorbars lt 1 pt 6 title "CMLA: N=8, alpha=1"

# PBIL vs CMLA 8-1: LOBProblem, Evals
reset
set yrange [0:20]
set xrange [0:200000]
set key bottom right
set xlabel "Evaluations"
set ylabel "Fitness (Best-So-Far)"
set term postscript enhanced eps monochrome "Times" 20
set output "finalplots/lobproblem_pbil_vs_cmla_8_alpha_1_evals.eps"

plot 'comparisons/lobproblem_pbil.dat.evals' every 10 using 1:2 with lines lt 1 notitle, \
'comparisons/lobproblem_cmla_8_alpha_1.dat.evals' every 1000 using 1:2 with lines lt 1 notitle, \
'comparisons/lobproblem_pbil.dat.evals' every 7000 using 1:2:($3*1.96) with yerrorbars lt 1 pt 4 title "PBIL: 100 samples, selection size=10", \
'comparisons/lobproblem_cmla_8_alpha_1.dat.evals' every 7000 using 1:2:($3*1.96) with yerrorbars lt 1 pt 6 title "CMLA: N=8, alpha=1"

# PBIL vs CMLA 8-0.05: LOBProblem, Gens
reset
set yrange [0:20]
set xrange [0:1300]
set key bottom right
set xlabel "Generations"
set ylabel "Fitness (Best-So-Far)"
set term postscript enhanced eps monochrome "Times" 20
set output "finalplots/lobproblem_pbil_vs_cmla_8_alpha_0p05_gens.eps"

plot 'comparisons/lobproblem_pbil.dat.gens' using 1:2 with lines lt 1 notitle, \
'comparisons/lobproblem_cmla_8_alpha_0.05.dat.gens' using 1:2 with lines lt 1 notitle, \
'comparisons/lobproblem_pbil.dat.gens' every 40 using 1:2:($3*1.96) with yerrorbars lt 1 pt 4 title "PBIL: 100 samples, selection size=10", \
'comparisons/lobproblem_cmla_8_alpha_0.05.dat.gens' every 40 using 1:2:($3*1.96) with yerrorbars lt 1 pt 6 title "CMLA: N=8, alpha=0.05"

# PBIL vs CMLA 8-0.05: LOBProblem, Evals
reset
set yrange [0:20]
set xrange [0:1800000]
set xtics ("200 K" 200000, "400 K" 400000, "600 K" 600000, \
"800 K" 800000, "1 M" 1000000, "1.2 M" 1200000, \
"1.4 M" 1400000, "1.6 M" 1600000, "1.8 M" 1800000, "0" 0)
set key bottom right
set xlabel "Evaluations"
set ylabel "Fitness (Best-So-Far)"
set term postscript enhanced eps monochrome "Times" 20
set output "finalplots/lobproblem_pbil_vs_cmla_8_alpha_0p05_evals.eps"

plot 'comparisons/lobproblem_pbil.dat.evals' every 10 using 1:2 with lines lt 1 notitle, \
'comparisons/lobproblem_cmla_8_alpha_0.05.dat.evals' every 1000 using 1:2 with lines lt 1 notitle, \
'comparisons/lobproblem_pbil.dat.evals' every 20000 using 1:2:($3*1.96) with points lt 1 pt 4 title "PBIL: 100 samples, selection size=10", \
'comparisons/lobproblem_cmla_8_alpha_0.05.dat.evals' every 50000 using 1:2:($3*1.96) with yerrorbars lt 1 pt 6 title "CMLA: N=8, alpha=0.05"