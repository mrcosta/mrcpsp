package mrcpsp.process

import spock.lang.Specification

/**
 * Created by mateus on 2/17/14.
 */
class ResultProcessorSpec extends Specification {

   /* [id: 1, modesAmount: 1, successorsAmount: 3, predecessorsAmount: 0, successors: [2, 3, 4], predecessors: [], staggeredPredecessors: [], {Start, End} =  { 0, 0 }
    [id: 2, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [10, 11], predecessors: [1], staggeredPredecessors: [1], {Start, End} =  { 0, 5 }
    [id: 3, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [5, 9], predecessors: [1], staggeredPredecessors: [1], {Start, End} =  { 1, 5 }
    [id: 4, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [7, 10], predecessors: [1], staggeredPredecessors: [1], {Start, End} =  { 0, 1 }
    [id: 5, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 1, successors: [6], predecessors: [3], staggeredPredecessors: [3], {Start, End} =  { 5, 12 }
    [id: 6, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [7, 8], predecessors: [5], staggeredPredecessors: [5], {Start, End} =  { 12, 16 }
    [id: 7, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 2, successors: [11], predecessors: [6, 4], staggeredPredecessors: [4, 6], {Start, End} =  { 18, 20 }
    [id: 8, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [10, 11], predecessors: [6], staggeredPredecessors: [6], {Start, End} =  { 16, 18 }
    [id: 9, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 1, successors: [12], predecessors: [3], staggeredPredecessors: [3], {Start, End} =  { 5, 7 }
    [id: 10, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 3, successors: [12], predecessors: [8, 4, 2], staggeredPredecessors: [2, 4, 8], {Start, End} =  { 5, 12 }
    [id: 11, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 3, successors: [12], predecessors: [8, 7, 2], staggeredPredecessors: [2, 8, 7], {Start, End} =  { 20, 24 }
    [id: 12, modesAmount: 1, successorsAmount: 0, predecessorsAmount: 3, successors: [], predecessors: [11, 10, 9], staggeredPredecessors: [9, 10, 11], {Start, End} =  { 24, 24 }
    criticalPath: 11, 7, 6, 5, 3, 1

    Job [id: 1, modesAmount: 1, successorsAmount: 3, predecessorsAmount: 0, successors: [2, 3, 4], predecessors: [], staggeredPredecessors: [], {Start, End} =  { 0, 0 }
    Job [id: 2, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [10, 11], predecessors: [1], staggeredPredecessors: [1], {Start, End} =  { 0, 5 }
    Job [id: 3, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [5, 9], predecessors: [1], staggeredPredecessors: [1], {Start, End} =  { 1, 5 }
    Job [id: 4, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [7, 10], predecessors: [1], staggeredPredecessors: [1], {Start, End} =  { 0, 1 }
    Job [id: 5, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 1, successors: [6], predecessors: [3], staggeredPredecessors: [3], {Start, End} =  { 5, 12 }
    Job [id: 6, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [7, 8], predecessors: [5], staggeredPredecessors: [5], {Start, End} =  { 12, 16 }
    Job [id: 7, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 2, successors: [11], predecessors: [6, 4], staggeredPredecessors: [4, 6], {Start, End} =  { 16, 18 }
    Job [id: 8, modesAmount: 3, successorsAmount: 2, predecessorsAmount: 1, successors: [10, 11], predecessors: [6], staggeredPredecessors: [6], {Start, End} =  { 18, 20 }
    Job [id: 9, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 1, successors: [12], predecessors: [3], staggeredPredecessors: [3], {Start, End} =  { 5, 7 }
    Job [id: 10, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 3, successors: [12], predecessors: [8, 4, 2], staggeredPredecessors: [2, 4, 8], {Start, End} =  { 20, 27 }
    Job [id: 11, modesAmount: 3, successorsAmount: 1, predecessorsAmount: 3, successors: [12], predecessors: [8, 7, 2], staggeredPredecessors: [2, 7, 8], {Start, End} =  { 20, 24 }
    Job [id: 12, modesAmount: 1, successorsAmount: 0, predecessorsAmount: 3, successors: [], predecessors: [11, 10, 9], staggeredPredecessors: [9, 10, 11], {Start, End} =  { 27, 27 }
    criticalPath: 10, 8, 6, 5, 3, 1 */
}
