node(Priority, Key, Value, L, R).

merge(null, null, null) :- !.
merge(node(P1, K1,V1, L1, R1), null, node(P1, K1,V1, L1, R1)) :- !.
merge(null, node(P2, K2, V2, L2, R2), node(P2, K2, V2, L2, R2)) :- !.
merge(node(P1, K1, V1, L1, R1), node(P2, K2, V2, L2, R2), node(P1, K1, V1, L1, RR)) :- P1 > P2,
  merge(R1, node(P2, K2, V2, L2, R2), RR).
merge(node(P1, K1, V1, L1, R1), node(P2, K2, V2, L2, R2), node(P2, K2, V2, LL, R2)) :- P1 =< P2,
  merge(node(P1, K1, V1, L1, R1), L2, LL).

split(null, K, null, null) :- !.
split(node(P, K, V, L, R), Key, node(P, K, V, L, NR), R1) :- K < Key, split(R, Key, NR, R1), !.
split(node(P, K, Val,  L, R), Key, L1, node(P, K, Val, NL, R)) :- K >= Key, split(L, Key, L1, NL), !.

map_get(node(P, K, V, L, R), K, V) :- !.
map_get(node(P, K, V, L, R), FK,FV) :- FK < K, map_get(L, FK, FV), !.
map_get(node(P, K, V, L, R), FK,FV) :- FK > K, map_get(R, FK, FV), !.

map_put(TreeMap, Key, Value, Result) :-
  split(TreeMap, Key, L1, R1),
  map_remove(R1, Key, R2),
  rand_int(2147483647, P),
  merge(L1, node(P ,Key, Value, null, null), Left),
  merge(Left, R2, Result), !.

map_build([], null) :- !.
map_build([(K, V) | T], TreeMap) :-
  map_build(T, Result),
  map_put(Result, K, V, TreeMap), !.

map_remove(null, K, null).
map_remove(node(P, K, V, L, R), K, Res) :- merge(L, R, Res), !.
map_remove(node(P, K, V, L, R), Key, node(P, K, V, L1, R)) :- Key < K, map_remove(L, Key, L1), !.
map_remove(node(P, K, V, L, R), Key, node(P, K, V, L, R1)) :- Key > K, map_remove(R, Key, R1), !.

map_floorKey(node(P, Key, V, L, R), Key, FloorKey) :- FloorKey is Key, !.
map_floorKey(node(P, K, V, L, null), Key, FloorKey) :- K < Key, FloorKey is K, !.
map_floorKey(node(P, K, V, L, R), Key, FloorKey) :- K < Key, map_floorKey(R, Key, Res), Res > K, FloorKey is Res, !.
map_floorKey(node(P, K, V, L, R), Key, FloorKey) :- K < Key, map_floorKey(R, Key, Res), Res < K, FloorKey is K, !.
map_floorKey(node(P, K, V, L, R), Key, FloorKey) :- K < Key, map_floorKey(R, Key, Res), FloorKey is K, !.
map_floorKey(node(P, K, V, L, R), Key, FloorKey) :- K > Key, map_floorKey(L, Key, FloorKey), !.
map_floorKey(node(P, K, V, L, R), Key, FloorKey) :- K < Key, \+ map_floorKey(R, Key, Res), FloorKey is K, !.

map_replace(null, NK, NV, null) :- !.
map_replace(node(P, Key, Value, L, R), NK, NV, node(P, Key, Value, Result, R)) :- NK < Key, map_replace(L, NK, NV, Result), !.
map_replace(node(P, Key, Value, L, R), NK, NV, node(P, Key, Value, L, Result)) :- Key < NK, map_replace(R, NK, NV, Result), !.
map_replace(node(P, Key, Value, L, R), NK, NV, node(P, Key, NV, L, R)) :- Key = NK, !.
