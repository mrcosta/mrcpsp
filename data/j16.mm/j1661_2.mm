************************************************************************
file with basedata            : md253_.bas
initial value random generator: 317867498
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  18
horizon                       :  127
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     16      0       16        3       16
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          3          11  13  16
   3        3          3           5   8  11
   4        3          3           8  12  13
   5        3          3           6   7  14
   6        3          2           9  10
   7        3          2           9  16
   8        3          3           9  14  17
   9        3          1          15
  10        3          1          13
  11        3          1          17
  12        3          2          14  15
  13        3          2          15  17
  14        3          1          16
  15        3          1          18
  16        3          1          18
  17        3          1          18
  18        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     1       8    5    6    6
         2     3       7    5    5    6
         3    10       3    4    4    2
  3      1     3       7    3    5    3
         2     5       7    2    5    3
         3     7       7    1    5    2
  4      1     2       5    8   10    3
         2     2       6    8    7    4
         3     6       2    8    6    3
  5      1     2       2   10    5    6
         2     8       1   10    5    6
         3     9       1   10    2    5
  6      1     2       9    5    8    8
         2     4       8    4    5    7
         3     7       7    4    5    6
  7      1     5       5    4    6    2
         2     8       4    4    6    1
         3     9       4    4    5    1
  8      1     3       9    8    4    7
         2     5       9    8    3    6
         3     9       8    5    3    5
  9      1     3       4    7    8    4
         2     7       4    6    8    4
         3     8       3    4    8    4
 10      1     2       8    7    8    8
         2     2       9    7    6    8
         3     5       6    7    6    3
 11      1     3       9    5    7    9
         2     4       7    5    5    9
         3     8       6    3    4    9
 12      1     5       7    7    6    4
         2     7       7    6    5    4
         3     9       7    2    5    1
 13      1     4       5    9    7    9
         2     6       5    9    7    6
         3     9       3    9    7    4
 14      1     2       8    7    8    2
         2     3       7    5    8    1
         3     9       6    1    8    1
 15      1     1       6    9    8    8
         2     3       4    4    2    8
         3     3       5    2    3    7
 16      1     6       5    7    9    6
         2     8       4    7    9    5
         3    10       3    4    8    3
 17      1     1       6    9   10    7
         2     3       6    8    8    6
         3     9       3    8    7    3
 18      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   16   15  115   93
************************************************************************
