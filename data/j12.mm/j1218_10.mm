************************************************************************
file with basedata            : md82_.bas
initial value random generator: 1757221434
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  14
horizon                       :  99
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     12      0       18        9       18
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          3           6   7   9
   3        3          3           5   6   9
   4        3          1          11
   5        3          3           8  10  12
   6        3          3           8  10  12
   7        3          2          10  11
   8        3          2          11  13
   9        3          2          12  13
  10        3          1          13
  11        3          1          14
  12        3          1          14
  13        3          1          14
  14        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     3       8    0    0    6
         2     4       0    4    0    5
         3    10       5    0    7    0
  3      1     1       0    8    0    5
         2     2      10    0    0    3
         3     7       9    0    2    0
  4      1     2       9    0    8    0
         2     4       0    5    8    0
         3     9       0    3    8    0
  5      1     5       6    0    0    7
         2     5       5    0    5    0
         3     8       5    0    0    7
  6      1     2       7    0    0    8
         2     6       5    0    5    0
         3     8       0    5    0    7
  7      1     4       0   10    7    0
         2     7       8    0    7    0
         3     9       4    0    0    4
  8      1     3       0    6   10    0
         2     9       7    0   10    0
         3     9       0    4   10    0
  9      1     5       0    7    0    1
         2     6       7    0    0    1
         3     9       6    0    0    1
 10      1     9       3    0    8    0
         2     9       0    6    0   10
         3    10       2    0    6    0
 11      1     3       4    0    0    7
         2     5       0    7    6    0
         3     7       2    0    0    5
 12      1     1       5    0    0   10
         2     2       0    9   10    0
         3     9       0    8    9    0
 13      1     2       6    0    0    8
         2     2       8    0    0    7
         3     4       0    8    8    0
 14      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   17   11   62   50
************************************************************************
