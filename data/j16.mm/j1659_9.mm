************************************************************************
file with basedata            : md251_.bas
initial value random generator: 2017832748
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  18
horizon                       :  146
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     16      0       14       11       14
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          3           6   7   9
   3        3          3           5   8  12
   4        3          3           5  12  16
   5        3          2          11  14
   6        3          2          10  15
   7        3          3          11  14  16
   8        3          3          13  15  16
   9        3          2          10  11
  10        3          2          12  13
  11        3          1          13
  12        3          1          17
  13        3          1          17
  14        3          1          15
  15        3          1          18
  16        3          1          18
  17        3          1          18
  18        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     2       5    0    8    3
         2     3       0   10    7    3
         3     8       0    9    5    1
  3      1     5       0    5    4    6
         2     8       0    5    3    5
         3     9       8    0    3    4
  4      1     3       0    8    6    2
         2     8       0    6    4    2
         3    10       4    0    3    2
  5      1     1       9    0    8    4
         2     5       4    0    7    3
         3     9       0    9    5    1
  6      1     3       0    8    5    6
         2    10       5    0    3    6
         3    10       0    6    2    6
  7      1     1       4    0    6    6
         2     5       2    0    5    4
         3     7       0    6    5    3
  8      1     2       3    0    7    9
         2     5       0    6    7    5
         3    10       3    0    7    3
  9      1     5       0   10    1    5
         2     5       7    0    1    8
         3    10       0   10    1    2
 10      1     1       9    0    9    9
         2     4       6    0    7    9
         3    10       0    6    6    9
 11      1     1       0    8    6   10
         2     8       0    7    4    9
         3    10       0    6    4    9
 12      1     5       0    2    8    4
         2     7       0    2    7    3
         3    10       0    2    5    1
 13      1     1       2    0    9    8
         2     2       0    9    8    8
         3    10       0    5    5    6
 14      1     5       0    7    7   10
         2     8       8    0    3    9
         3    10       8    0    1    8
 15      1     2       5    0    7    9
         2     3       0    7    7    9
         3     5       4    0    6    9
 16      1     2       8    0    9    6
         2     4       8    0    4    5
         3     8       0    5    4    3
 17      1     1       0    8    8    9
         2     7       9    0    8    9
         3    10       6    0    7    8
 18      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   21   31  108  109
************************************************************************
