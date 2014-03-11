************************************************************************
file with basedata            : md175_.bas
initial value random generator: 1019669108
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  16
horizon                       :  116
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     14      0       19       12       19
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          2           5  13
   3        3          3           5   7  13
   4        3          3           6   9  11
   5        3          2           6  11
   6        3          2          12  15
   7        3          3           8  10  12
   8        3          1          11
   9        3          3          10  13  14
  10        3          1          15
  11        3          2          14  15
  12        3          1          14
  13        3          1          16
  14        3          1          16
  15        3          1          16
  16        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     3       8    2    6    8
         2     4       7    2    4    7
         3     5       7    2    1    7
  3      1     2       6    8    6    9
         2     3       5    8    5    7
         3     8       3    8    4    6
  4      1     7       9    7    9    8
         2     7       9   10    8    8
         3     8       8    6    6    7
  5      1     1       8    8    7    6
         2     2       7    5    7    6
         3     8       3    4    6    4
  6      1     5       6    5    8    5
         2     7       2    5    6    5
         3     7       5    3    5    4
  7      1     2       9    9    7    7
         2    10       1    9    6    1
         3    10       5    8    3    5
  8      1     3       9   10    8    9
         2     4       4    6    7    6
         3    10       4    3    7    4
  9      1     3       7   10    7    7
         2     5       3    9    7    3
         3     6       1    8    6    1
 10      1     2       8    6    5    9
         2     5       6    6    5    5
         3    10       5    1    4    3
 11      1     3      10    5    1    9
         2     5       8    5    1    9
         3    10       8    4    1    6
 12      1     6       4    3    7    5
         2     7       3    2    7    5
         3     8       1    1    6    5
 13      1     7       5    3    4    8
         2     7       5    3    5    7
         3     7       5    6    6    6
 14      1     1       8    9    6   10
         2     6       8    9    4    8
         3    10       7    8    3    8
 15      1     1       8    7    4    5
         2     8       5    6    3    3
         3     9       5    6    1    3
 16      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   22   22   72   85
************************************************************************
