==================================================================
Authors's Name		:A. Sprecher / A. Drexl
Authors's Email		:Drexl@bwl.uni-kiel.de
Instance Set		:J10
Type			:MM
Date			    : 2/15/96
=======================================================================
Research Report: Solving Multi-Mode Resource-Constrained Project
		 Scheduling Problems by a Simple, General and Powerful
		 Sequencing Algorithm. Part I: Theory and Part II
		 Computation.
		 Research Reports 385 and 386, Institut fuer Betriebs-
		 wirtschaftslehre, Christian-Albrechts-Univeritaet zu
		 Kiel.
=======================================================================
Computer	 : IBM compatible Personal Computer
Processor	 : 80486 dx
Clockpulse	 : 66 MHz
Operating System : OS/2
Memory Code	 : 100 KB
Memory Data	 :   8 MB
Language	 : GNU C
Average CPU-Time : 0.14 sec.
=======================================================================
   Paramter Instance  Makespan	CPU-Time[sec.]
-----------------------------------------------------------------------
       1,      1,	16384,	   0.00
       1,      2,	16384,	   0.00
       1,      3,	16384,	   0.00
       1,      4,	16384,	   0.00
       1,      5,	16384,	   0.00
       1,      6,	16384,	   0.00
       1,      7,	16384,	   0.00
       1,      8,	16384,	   0.00
       1,      9,	16384,	   0.00
       1,     10,	16384,	   0.00
       2,      1,	16384,	   0.00
       2,      2,	   20,	   0.12
       2,      3,	16384,	   0.00
       2,      4,	   18,	   0.13
       2,      5,	   16,	   0.88
       2,      6,	   16,	   0.06
       2,      7,	   25,	   0.09
       2,      8,	16384,	   0.00
       2,      9,	   17,	   0.10
       2,     10,	   33,	   0.28
       3,      1,	16384,	   0.00
       3,      2,	   13,	   0.09
       3,      3,	   19,	   0.10
       3,      4,	   23,	   0.06
       3,      5,	   21,	   0.22
       3,      6,	   29,	   0.13
       3,      7,	   14,	   0.10
       3,      8,	   24,	   0.34
       3,      9,	   20,	   0.07
       3,     10,	   16,	   0.06
       4,      1,	   27,	   0.06
       4,      2,	   20,	   0.03
       4,      3,	   23,	   0.03
       4,      4,	   15,	   0.06
       4,      5,	   31,	   0.03
       4,      6,	   21,	   0.03
       4,      7,	16384,	   0.00
       4,      8,	   19,	   0.06
       4,      9,	   21,	   0.03
       4,     10,	   15,	   0.07
       5,      1,	   42,	   0.06
       5,      2,	16384,	   0.00
       5,      3,	   32,	   0.18
       5,      4,	   23,	   0.22
       5,      5,	16384,	   0.00
       5,      6,	   34,	   0.41
       5,      7,	16384,	   0.00
       5,      8,	   31,	   0.10
       5,      9,	16384,	   0.00
       5,     10,	   25,	   0.09
       6,      1,	   18,	   0.09
       6,      2,	16384,	   0.00
       6,      3,	   29,	   0.31
       6,      4,	   23,	   0.56
       6,      5,	   37,	   0.12
       6,      6,	16384,	   0.00
       6,      7,	   23,	   0.16
       6,      8,	   23,	   0.07
       6,      9,	16384,	   0.00
       6,     10,	16384,	   0.00
       7,      1,	   14,	   0.10
       7,      2,	   24,	   0.12
       7,      3,	   23,	   0.09
       7,      4,	   19,	   0.10
       7,      5,	   24,	   0.09
       7,      6,	   36,	   0.37
       7,      7,	16384,	   0.00
       7,      8,	   31,	   0.10
       7,      9,	   28,	   0.22
       7,     10,	16384,	   0.00
       8,      1,	16384,	   0.00
       8,      2,	16384,	   0.00
       8,      3,	   17,	   0.04
       8,      4,	16384,	   0.00
       8,      5,	   25,	   0.03
       8,      6,	   23,	   0.07
       8,      7,	   20,	   0.06
       8,      8,	   14,	   0.04
       8,      9,	16384,	   0.00
       8,     10,	   23,	   0.06
       9,      1,	16384,	   0.00
       9,      2,	16384,	   0.00
       9,      3,	16384,	   0.00
       9,      4,	16384,	   0.00
       9,      5,	16384,	   0.00
       9,      6,	16384,	   0.00
       9,      7,	16384,	   0.00
       9,      8,	16384,	   0.00
       9,      9,	16384,	   0.00
       9,     10,	16384,	   0.00
      10,      1,	   17,	   0.04
      10,      2,	   24,	   0.06
      10,      3,	   21,	   0.04
      10,      4,	   15,	   0.03
      10,      5,	   24,	   0.16
      10,      6,	   18,	   0.19
      10,      7,	   15,	   0.06
      10,      8,	   15,	   0.15
      10,      9,	   10,	   0.06
      10,     10,	   17,	   0.28
      11,      1,	   20,	   0.03
      11,      2,	   11,	   0.00
      11,      3,	   12,	   0.03
      11,      4,	   23,	   0.04
      11,      5,	   15,	   0.06
      11,      6,	   15,	   0.07
      11,      7,	   11,	   0.04
      11,      8,	   14,	   0.03
      11,      9,	   16,	   0.03
      11,     10,	   21,	   0.03
      12,      1,	   15,	   0.03
      12,      2,	   15,	   0.03
      12,      3,	   16,	   0.03
      12,      4,	   17,	   0.04
      12,      5,	   13,	   0.03
      12,      6,	   22,	   0.03
      12,      7,	   12,	   0.03
      12,      8,	   19,	   0.00
      12,      9,	   21,	   0.03
      12,     10,	    9,	   0.03
      13,      1,	   24,	   0.16
      13,      2,	   21,	   0.16
      13,      3,	   27,	   0.19
      13,      4,	   27,	   0.12
      13,      5,	   24,	   0.25
      13,      6,	   23,	   0.15
      13,      7,	   24,	   0.18
      13,      8,	   19,	   0.22
      13,      9,	   24,	   0.09
      13,     10,	   33,	   0.22
      14,      1,	   16,	   0.18
      14,      2,	   19,	   0.21
      14,      3,	   22,	   0.15
      14,      4,	   19,	   0.21
      14,      5,	   19,	   0.03
      14,      6,	   16,	   0.22
      14,      7,	   21,	   0.25
      14,      8,	   24,	   0.09
      14,      9,	   22,	   0.07
      14,     10,	   15,	   0.37
      15,      1,	   13,	   0.06
      15,      2,	   25,	   0.06
      15,      3,	   15,	   0.03
      15,      4,	   15,	   0.16
      15,      5,	   11,	   0.34
      15,      6,	   19,	   0.06
      15,      7,	   10,	   0.13
      15,      8,	   11,	   0.06
      15,      9,	   14,	   0.06
      15,     10,	   18,	   0.03
      16,      1,	   13,	   0.03
      16,      2,	   17,	   0.03
      16,      3,	   12,	   0.03
      16,      4,	   21,	   0.00
      16,      5,	   25,	   0.03
      16,      6,	   21,	   0.07
      16,      7,	   12,	   0.03
      16,      8,	    9,	   0.03
      16,      9,	   16,	   0.03
      16,     10,	   26,	   0.03
      17,      1,	16384,	   0.00
      17,      2,	16384,	   0.00
      17,      3,	16384,	   0.00
      17,      4,	16384,	   0.00
      17,      5,	16384,	   0.00
      17,      6,	16384,	   0.00
      17,      7,	16384,	   0.00
      17,      8,	16384,	   0.00
      17,      9,	16384,	   0.00
      17,     10,	16384,	   0.00
      18,      1,	   15,	   0.25
      18,      2,	   15,	   0.09
      18,      3,	   17,	   0.10
      18,      4,	   19,	   0.06
      18,      5,	   14,	   0.07
      18,      6,	   15,	   0.09
      18,      7,	   12,	   0.03
      18,      8,	   16,	   0.06
      18,      9,	   14,	   0.07
      18,     10,	   16,	   0.06
      19,      1,	   13,	   0.04
      19,      2,	   15,	   0.03
      19,      3,	   16,	   0.03
      19,      4,	   15,	   0.03
      19,      5,	   13,	   0.03
      19,      6,	   17,	   0.03
      19,      7,	   14,	   0.03
      19,      8,	   15,	   0.03
      19,      9,	   12,	   0.06
      19,     10,	   11,	   0.03
      20,      1,	   12,	   0.00
      20,      2,	   18,	   0.03
      20,      3,	   21,	   0.03
      20,      4,	   17,	   0.03
      20,      5,	   12,	   0.00
      20,      6,	   20,	   0.03
      20,      7,	16384,	   0.00
      20,      8,	   12,	   0.03
      20,      9,	    9,	   0.03
      20,     10,	   10,	   0.03
      21,      1,	   27,	   0.22
      21,      2,	   23,	   0.13
      21,      3,	   37,	   0.09
      21,      4,	   23,	   0.13
      21,      5,	   35,	   0.13
      21,      6,	   29,	   0.13
      21,      7,	   24,	   0.25
      21,      8,	   23,	   0.16
      21,      9,	   25,	   0.06
      21,     10,	   26,	   0.34
      22,      1,	   21,	   0.16
      22,      2,	   15,	   0.25
      22,      3,	   23,	   0.10
      22,      4,	   16,	   0.25
      22,      5,	   15,	   0.12
      22,      6,	   16,	   0.07
      22,      7,	   12,	   0.06
      22,      8,	   19,	   0.60
      22,      9,	   19,	   0.10
      22,     10,	   19,	   0.44
      23,      1,	   15,	   0.65
      23,      2,	   15,	   0.06
      23,      3,	   16,	   0.16
      23,      4,	   21,	   0.16
      23,      5,	   14,	   0.09
      23,      6,	   19,	   0.06
      23,      7,	16384,	   0.00
      23,      8,	   18,	   0.06
      23,      9,	   13,	   0.15
      23,     10,	   16,	   0.06
      24,      1,	    8,	   0.03
      24,      2,	   14,	   0.03
      24,      3,	   10,	   0.00
      24,      4,	   15,	   0.06
      24,      5,	   14,	   0.00
      24,      6,	   16,	   0.03
      24,      7,	   17,	   0.03
      24,      8,	   25,	   0.03
      24,      9,	   10,	   0.03
      24,     10,	   15,	   0.03
      25,      1,	16384,	   0.00
      25,      2,	16384,	   0.00
      25,      3,	16384,	   0.00
      25,      4,	16384,	   0.00
      25,      5,	16384,	   0.00
      25,      6,	16384,	   0.00
      25,      7,	16384,	   0.00
      25,      8,	16384,	   0.00
      25,      9,	16384,	   0.00
      25,     10,	16384,	   0.00
      26,      1,	   14,	   0.09
      26,      2,	   12,	   0.07
      26,      3,	   16,	   0.03
      26,      4,	   16,	   0.07
      26,      5,	   18,	   0.16
      26,      6,	   18,	   0.06
      26,      7,	   14,	   0.07
      26,      8,	   15,	   0.03
      26,      9,	   11,	   0.07
      26,     10,	   16,	   0.06
      27,      1,	   15,	   0.03
      27,      2,	   12,	   0.12
      27,      3,	   15,	   0.04
      27,      4,	   15,	   0.03
      27,      5,	   18,	   0.16
      27,      6,	   14,	   0.19
      27,      7,	   10,	   0.06
      27,      8,	   12,	   0.03
      27,      9,	   15,	   0.07
      27,     10,	   14,	   0.03
      28,      1,	   19,	   0.03
      28,      2,	   24,	   0.03
      28,      3,	   20,	   0.00
      28,      4,	   16,	   0.03
      28,      5,	   18,	   0.00
      28,      6,	    9,	   0.00
      28,      7,	   15,	   0.04
      28,      8,	   16,	   0.03
      28,      9,	   12,	   0.00
      28,     10,	   18,	   0.04
      29,      1,	   20,	   0.12
      29,      2,	   19,	   0.09
      29,      3,	   24,	   0.22
      29,      4,	   23,	   0.09
      29,      5,	   25,	   0.10
      29,      6,	   27,	   0.12
      29,      7,	   28,	   0.15
      29,      8,	   23,	   0.19
      29,      9,	   23,	   0.16
      29,     10,	   28,	   0.10
      30,      1,	   16,	   0.09
      30,      2,	   17,	   0.15
      30,      3,	   17,	   0.12
      30,      4,	   12,	   0.06
      30,      5,	   28,	   0.19
      30,      6,	   22,	   0.13
      30,      7,	   15,	   0.06
      30,      8,	   14,	   0.19
      30,      9,	   15,	   0.25
      30,     10,	   17,	   0.06
      31,      1,	   17,	   0.06
      31,      2,	   15,	   0.03
      31,      3,	   15,	   0.06
      31,      4,	   18,	   0.10
      31,      5,	   15,	   0.06
      31,      6,	   20,	   0.03
      31,      7,	   14,	   0.09
      31,      8,	   16,	   0.06
      31,      9,	   15,	   0.07
      31,     10,	   13,	   0.12
      32,      1,	   17,	   0.03
      32,      2,	   12,	   0.00
      32,      3,	   19,	   0.04
      32,      4,	   12,	   0.03
      32,      5,	   11,	   0.00
      32,      6,	   17,	   0.04
      32,      7,	   16,	   0.03
      32,      8,	   12,	   0.00
      32,      9,	   15,	   0.03
      32,     10,	   13,	   0.03
      33,      1,	16384,	   0.00
      33,      2,	16384,	   0.00
      33,      3,	16384,	   0.00
      33,      4,	16384,	   0.00
      33,      5,	16384,	   0.00
      33,      6,	16384,	   0.00
      33,      7,	16384,	   0.00
      33,      8,	16384,	   0.00
      33,      9,	16384,	   0.00
      33,     10,	16384,	   0.00
      34,      1,	   23,	   0.25
      34,      2,	   12,	   0.13
      34,      3,	   25,	   0.60
      34,      4,	   23,	   1.31
      34,      5,	   22,	   0.28
      34,      6,	   30,	   2.31
      34,      7,	   26,	   0.22
      34,      8,	   19,	   0.29
      34,      9,	   26,	   0.22
      34,     10,	   18,	   0.53
      35,      1,	   28,	   0.40
      35,      2,	   32,	   0.34
      35,      3,	   34,	   0.53
      35,      4,	   23,	   0.18
      35,      5,	   24,	   0.40
      35,      6,	   19,	   0.09
      35,      7,	   22,	   0.22
      35,      8,	   28,	   0.50
      35,      9,	   18,	   0.12
      35,     10,	   20,	   0.90
      36,      1,	   32,	   0.06
      36,      2,	   26,	   0.06
      36,      3,	   17,	   0.09
      36,      4,	   22,	   0.06
      36,      5,	   23,	   0.12
      36,      6,	   21,	   0.10
      36,      7,	   25,	   0.09
      36,      8,	   23,	   0.12
      36,      9,	16384,	   0.00
      36,     10,	   33,	   0.06
      37,      1,	   36,	   0.10
      37,      2,	   27,	   0.31
      37,      3,	   29,	   0.25
      37,      4,	   33,	   0.44
      37,      5,	   38,	   0.47
      37,      6,	   28,	   0.25
      37,      7,	   30,	   0.85
      37,      8,	   42,	   0.22
      37,      9,	   31,	   0.60
      37,     10,	   40,	   0.40
      38,      1,	   28,	   0.12
      38,      2,	   25,	   1.57
      38,      3,	   25,	   0.66
      38,      4,	   27,	   0.56
      38,      5,	   22,	   0.65
      38,      6,	   24,	   0.78
      38,      7,	   32,	   0.59
      38,      8,	   31,	   0.22
      38,      9,	   31,	   0.47
      38,     10,	   22,	   0.31
      39,      1,	   21,	   1.34
      39,      2,	   18,	   0.53
      39,      3,	   28,	   0.31
      39,      4,	   17,	   0.16
      39,      5,	   30,	   0.41
      39,      6,	   24,	   0.31
      39,      7,	   17,	   0.16
      39,      8,	   23,	   0.28
      39,      9,	   24,	   1.21
      39,     10,	   31,	   0.22
      40,      1,	   19,	   0.06
      40,      2,	   32,	   0.12
      40,      3,	   25,	   0.03
      40,      4,	16384,	   0.00
      40,      5,	   21,	   0.06
      40,      6,	   18,	   0.07
      40,      7,	   28,	   0.03
      40,      8,	   29,	   0.10
      40,      9,	   19,	   0.06
      40,     10,	   16,	   0.10
      41,      1,	16384,	   0.00
      41,      2,	16384,	   0.00
      41,      3,	16384,	   0.00
      41,      4,	16384,	   0.00
      41,      5,	16384,	   0.00
      41,      6,	16384,	   0.00
      41,      7,	16384,	   0.00
      41,      8,	16384,	   0.00
      41,      9,	16384,	   0.00
      41,     10,	16384,	   0.00
      42,      1,	   17,	   0.16
      42,      2,	   15,	   0.09
      42,      3,	   25,	   0.06
      42,      4,	   14,	   0.13
      42,      5,	   20,	   0.06
      42,      6,	   12,	   0.15
      42,      7,	   26,	   0.09
      42,      8,	   18,	   0.06
      42,      9,	   12,	   0.06
      42,     10,	   12,	   0.13
      43,      1,	   18,	   0.06
      43,      2,	   15,	   0.06
      43,      3,	   17,	   0.13
      43,      4,	   15,	   0.06
      43,      5,	   19,	   0.13
      43,      6,	   16,	   0.10
      43,      7,	   12,	   0.09
      43,      8,	   21,	   0.07
      43,      9,	   11,	   0.03
      43,     10,	   16,	   0.28
      44,      1,	   18,	   0.03
      44,      2,	   16,	   0.03
      44,      3,	   20,	   0.03
      44,      4,	   24,	   0.06
      44,      5,	   24,	   0.06
      44,      6,	16384,	   0.00
      44,      7,	   15,	   0.03
      44,      8,	   16,	   0.06
      44,      9,	   17,	   0.06
      44,     10,	   13,	   0.00
      45,      1,	   27,	   0.19
      45,      2,	   20,	   0.06
      45,      3,	   18,	   0.25
      45,      4,	   19,	   0.28
      45,      5,	   28,	   0.19
      45,      6,	   23,	   0.25
      45,      7,	   31,	   0.29
      45,      8,	   23,	   0.28
      45,      9,	   24,	   0.28
      45,     10,	   25,	   0.15
      46,      1,	   24,	   0.47
      46,      2,	   14,	   0.25
      46,      3,	   15,	   0.19
      46,      4,	   18,	   0.22
      46,      5,	   18,	   0.28
      46,      6,	   23,	   1.32
      46,      7,	   21,	   1.03
      46,      8,	   21,	   0.35
      46,      9,	   16,	   0.22
      46,     10,	   20,	   0.06
      47,      1,	   20,	   0.15
      47,      2,	   15,	   0.06
      47,      3,	   19,	   0.22
      47,      4,	   25,	   0.18
      47,      5,	   16,	   0.12
      47,      6,	   19,	   0.10
      47,      7,	   18,	   0.19
      47,      8,	   21,	   0.47
      47,      9,	   16,	   0.38
      47,     10,	   29,	   0.72
      48,      1,	   16,	   0.03
      48,      2,	   16,	   0.07
      48,      3,	   13,	   0.03
      48,      4,	   10,	   0.00
      48,      5,	   14,	   0.03
      48,      6,	   14,	   0.03
      48,      7,	   14,	   0.07
      48,      8,	   24,	   0.06
      48,      9,	   16,	   0.04
      48,     10,	   16,	   0.06
      49,      1,	16384,	   0.00
      49,      2,	16384,	   0.00
      49,      3,	16384,	   0.00
      49,      4,	16384,	   0.00
      49,      5,	16384,	   0.00
      49,      6,	16384,	   0.00
      49,      7,	16384,	   0.00
      49,      8,	16384,	   0.00
      49,      9,	16384,	   0.00
      49,     10,	16384,	   0.00
      50,      1,	   13,	   0.10
      50,      2,	   20,	   0.06
      50,      3,	   17,	   0.07
      50,      4,	   20,	   0.22
      50,      5,	   10,	   0.06
      50,      6,	   12,	   0.03
      50,      7,	   26,	   0.06
      50,      8,	   11,	   0.07
      50,      9,	   20,	   0.06
      50,     10,	   17,	   0.25
      51,      1,	   23,	   0.03
      51,      2,	   14,	   0.03
      51,      3,	   15,	   0.03
      51,      4,	   17,	   0.06
      51,      5,	   29,	   0.03
      51,      6,	    9,	   0.03
      51,      7,	   21,	   0.03
      51,      8,	   14,	   0.03
      51,      9,	   19,	   0.03
      51,     10,	   12,	   0.04
      52,      1,	   11,	   0.03
      52,      2,	   18,	   0.03
      52,      3,	    8,	   0.03
      52,      4,	   15,	   0.03
      52,      5,	   19,	   0.03
      52,      6,	   14,	   0.03
      52,      7,	   16,	   0.04
      52,      8,	   16,	   0.03
      52,      9,	   17,	   0.00
      52,     10,	   10,	   0.03
      53,      1,	   28,	   0.18
      53,      2,	   33,	   0.25
      53,      3,	   26,	   0.09
      53,      4,	   23,	   0.12
      53,      5,	   26,	   0.07
      53,      6,	   29,	   0.06
      53,      7,	   18,	   0.09
      53,      8,	   25,	   0.16
      53,      9,	   29,	   0.38
      53,     10,	   30,	   0.09
      54,      1,	   18,	   0.54
      54,      2,	   22,	   0.06
      54,      3,	   23,	   0.16
      54,      4,	   18,	   0.44
      54,      5,	   14,	   0.13
      54,      6,	   14,	   0.12
      54,      7,	   21,	   0.18
      54,      8,	   18,	   0.28
      54,      9,	   22,	   0.16
      54,     10,	   19,	   0.19
      55,      1,	   18,	   0.03
      55,      2,	   14,	   0.12
      55,      3,	   13,	   0.04
      55,      4,	   24,	   0.03
      55,      5,	   13,	   0.07
      55,      6,	   19,	   0.19
      55,      7,	   16,	   0.06
      55,      8,	   20,	   0.63
      55,      9,	   13,	   0.13
      55,     10,	   17,	   0.03
      56,      1,	   14,	   0.00
      56,      2,	   13,	   0.00
      56,      3,	   23,	   0.03
      56,      4,	   10,	   0.03
      56,      5,	   14,	   0.03
      56,      6,	   13,	   0.03
      56,      7,	   18,	   0.04
      56,      8,	   15,	   0.03
      56,      9,	   14,	   0.04
      56,     10,	   14,	   0.00
      57,      1,	16384,	   0.00
      57,      2,	16384,	   0.00
      57,      3,	16384,	   0.00
      57,      4,	16384,	   0.00
      57,      5,	16384,	   0.00
      57,      6,	16384,	   0.00
      57,      7,	16384,	   0.00
      57,      8,	16384,	   0.00
      57,      9,	16384,	   0.00
      57,     10,	16384,	   0.00
      58,      1,	   19,	   0.06
      58,      2,	   14,	   0.03
      58,      3,	   19,	   0.16
      58,      4,	   17,	   0.03
      58,      5,	   16,	   0.13
      58,      6,	   14,	   0.25
      58,      7,	   13,	   0.03
      58,      8,	   16,	   0.03
      58,      9,	   11,	   0.06
      58,     10,	   15,	   0.09
      59,      1,	   13,	   0.03
      59,      2,	   15,	   0.03
      59,      3,	   16,	   0.03
      59,      4,	   14,	   0.03
      59,      5,	   13,	   0.03
      59,      6,	   12,	   0.03
      59,      7,	   11,	   0.03
      59,      8,	   12,	   0.03
      59,      9,	   15,	   0.03
      59,     10,	   17,	   0.03
      60,      1,	   13,	   0.03
      60,      2,	   19,	   0.04
      60,      3,	    9,	   0.03
      60,      4,	   13,	   0.00
      60,      5,	   18,	   0.00
      60,      6,	   10,	   0.00
      60,      7,	   15,	   0.03
      60,      8,	   13,	   0.03
      60,      9,	   15,	   0.00
      60,     10,	   15,	   0.03
      61,      1,	   24,	   0.31
      61,      2,	   24,	   0.10
      61,      3,	   32,	   0.19
      61,      4,	   27,	   0.09
      61,      5,	   23,	   0.09
      61,      6,	   30,	   0.06
      61,      7,	   23,	   0.09
      61,      8,	   33,	   0.15
      61,      9,	   26,	   0.09
      61,     10,	   41,	   0.06
      62,      1,	   15,	   0.12
      62,      2,	   23,	   0.44
      62,      3,	   16,	   0.79
      62,      4,	   15,	   0.31
      62,      5,	   18,	   0.03
      62,      6,	   20,	   0.15
      62,      7,	   14,	   0.15
      62,      8,	   19,	   0.13
      62,      9,	   18,	   0.13
      62,     10,	   20,	   0.31
      63,      1,	   17,	   0.06
      63,      2,	   17,	   0.03
      63,      3,	   17,	   0.06
      63,      4,	   12,	   0.06
      63,      5,	   12,	   0.12
      63,      6,	   15,	   0.13
      63,      7,	   15,	   0.06
      63,      8,	   10,	   0.06
      63,      9,	   14,	   0.16
      63,     10,	   10,	   0.13
      64,      1,	   16,	   0.03
      64,      2,	   15,	   0.03
      64,      3,	   14,	   0.03
      64,      4,	   13,	   0.00
      64,      5,	   16,	   0.00
      64,      6,	   11,	   0.03
      64,      7,	   12,	   0.03
      64,      8,	   14,	   0.04
      64,      9,	   17,	   0.00
      64,     10,	   15,	   0.03
