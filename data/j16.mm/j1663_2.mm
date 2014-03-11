************************************************************************
file with basedata            : md255_.bas
initial value random generator: 89893444
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  18
horizon                       :  123
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     16      0       24       11       24
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          3           5   9  10
   3        3          3           9  10  13
   4        3          3           6  11  13
   5        3          3           7  12  14
   6        3          3           7   8  17
   7        3          1          16
   8        3          2           9  15
   9        3          1          14
  10        3          2          11  15
  11        3          2          16  17
  12        3          2          15  17
  13        3          1          14
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
  2      1     5       6    2    7    3
         2     5       2    6    7    5
         3     5       5    3    7    7
  3      1     1       5    1    5    8
         2     6       5    1    5    5
         3     9       4    1    4    2
  4      1     5       3    9    6    7
         2     6       3    7    5    7
         3     7       3    6    4    2
  5      1     1      10    9    7    6
         2     4      10    7    6    6
         3    10      10    4    5    4
  6      1     6       3   10    6    5
         2    10       3    9    4    2
         3    10       2    9    3    5
  7      1     5       8    5   10    4
         2     7       6    4    9    3
         3     9       5    1    9    3
  8      1     5       8    8   10    4
         2     7       8    7   10    3
         3     8       5    6   10    2
  9      1     1       9    6    8    7
         2     7       8    1    4    5
         3     7       9    1    5    4
 10      1     2       9   10    9    7
         2     3       6   10    4    7
         3     4       5   10    1    7
 11      1     4       9    7    1    8
         2     6       3    7    1    7
         3     6       4    6    1    7
 12      1     2       7    6    7    7
         2     3       6    5    5    5
         3     8       6    5    5    2
 13      1     7       2    4    3    9
         2     7       3    4    5    8
         3     8       2    4    2    7
 14      1     2       7    8    4   10
         2     2       7    6    5   10
         3     7       5    6    4    8
 15      1     1       4    3    5    8
         2     6       2    3    3    6
         3    10       2    2    3    6
 16      1     5       4   10    5    2
         2     5       4    9    7    3
         3     9       3    8    3    2
 17      1     4       5    9   10   10
         2     5       4    8    5    8
         3     6       3    8    5    7
 18      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   21   27  108  110
************************************************************************
