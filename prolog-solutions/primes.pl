init(MAX_N) :- sieve(MAX_N, 2).

composite_table(1).
sieve(AX, D) :- D * D =< AX, \+composite_table(D),  R is D * D, make_table(AX, D, R).
sieve(AX, D) :- D * D =< AX, D1 is D + 1, sieve(AX, D1).
make_table(AX, D, R) :- R =< AX, assert(composite_table(R)), R1 is R + D, make_table(AX, D, R1).
prime(2) :- !.
prime(N) :- N>1, \+composite_table(N).
composite(N) :- composite_table(N).
next_prime(P, N) :- P1 is P + 1, prime(P1), !, N is P1.
next_prime(P, N) :- P1 is P + 1, next_prime(P1, N).


prime_divisors(1, []) :- !.
prime_divisors(N, Divisors) :- number(N), N > 1, find_divisors(N, Divisors, 2), !.
find_divisors(N,[T],_) :- prime(N), !, T is N.
find_divisors(N, [H|T], D) :- D * D =< N,(N mod D) =:= 0, prime(D), N1 is div(N, D), H is D, find_divisors(N1, T, D).
find_divisors(N, [H|T], D) :- D * D =< N, (N mod D) =\= 0, next_prime(D, M), find_divisors(N, [H|T], M), !.

prime_divisors(N, Divisors) :- sort(Divisors), isPrime(Divisors), multiply(Divisors, N1, 1), N is N1, !.
prime_divisors(N, []) :- N is 1, !.

sort([H1, H2|T]) :- H1 =< H2, sort([H2|T]).
sort([H]) :- !.

isPrime([H|T]) :- prime(H), isPrime(T), !.
isPrime([H]) :- prime(H), !.

multiply([H|T], N1, M) :- M1 is M * H, multiply(T, N1, M1).
multiply([H], N1, M) :- N1 is H * M, !.

lcm(A, 1, A) :- !.
lcm(1, B, B) :- !.
lcm(A, B, LCM) :- prime_divisors(A, DIV1), prime_divisors(B, DIV2), find_lcm(DIV1, DIV2, 1, R), LCM is R.

find_lcm([H1|T1],[H1|T2], ANS, R) :- ANS1 is ANS * H1, find_lcm(T1, T2, ANS1, R), !.
find_lcm([H1|T1],[H2|T2], ANS, R) :- H2 < H1, ANS1 is H2 * ANS, find_lcm([H1|T1], T2, ANS1, R), !.
find_lcm([H1|T1],[H2|T2], ANS, R) :- H2 > H1, ANS1 is H1 * ANS, find_lcm(T1, [H2|T2], ANS1, R), !.
find_lcm([], [H|T], ANS, R) :- ANS1 is H * ANS, find_lcm([], T, ANS1, R).
find_lcm([H|T],[],ANS,R) :- ANS1 is ANS * H, find_lcm(T, [], ANS1, R), !.
find_lcm([],[],ANS,R) :- R is ANS.


nth_prime(N, P) :- number(N), find(N, P, 1), !.
nth_prime(N, P) :- number(P), find_num(N, P, 1, 0), !.
find_num(N, P, NTH, R) :- P = NTH, N is R.
find_num(N, P, NTH, R) :- P > NTH, next_prime(NTH, M), R1 is R + 1, find_num(N, P, M, R1).
find(0, P, D) :- P is D, !.
find(N, P, D) :-  N > 0, next_prime(D, M), N1 is N - 1, find(N1, P, M), !.