homem(joao).
homem(rui).
homem(manuel).
homem(ricardo).
homem(andre).

mulher(maria).
mulher(ana).
mulher(rita).
mulher(silvia).
mulher(joana).
mulher(sofia).
mulher(ines).
mulher(lidia).

progenitor(joao, maria).
progenitor(joao, rui).
progenitor(joao, ines).
progenitor(manuel, joao).
progenitor(manuel, andre).
progenitor(ricardo, manuel).
progenitor(ana, rui).
progenitor(joana, ines).
progenitor(joana, maria).
progenitor(rita, joao).
progenitor(sofia, joana).
progenitor(rita, silvia).
progenitor(silvia,lidia).


% EX1 : Quem é o pai duma pessoa qualquer?

pai(X, Y) :- progenitor(X, Y), homem(X).

% EX2 : Quem é a mãe de uma pessoa qualquer?

mae(X,Y) :- progenitor(X,Y), mulher(X).

% EX3 : Como definir irmão (genérico: entenda-se irmão ou irmã)?

irmao(X,Y) :- progenitor(Z,Y), progenitor(Z,X), X \= Y.

% EX4: Como definir irmã?

irma(X,Y) :- irmao(X,Y), mulher(Y).

% EX5: Como definir avós?

avo(X,Y) :- progenitor(Z,Y), progenitor(X,Z).

% EX6: Como definir tio?

tio(X,Y) :- irmao(X,Z), progenitor(Z,Y).

% EX7: Como definir primo (direto)?

primo(X, Y) :- progenitor(Z, X), progenitor(W, Y), irmao(Z, W), X \= Y.

% EX8: Como dizer que alguém é antepassado de outra pessoa?

antepassado(X,Y) :- progenitor(X,Y).
antepassado(X,Y) :- progenitor(X,Z), antepassado(Z,Y).

% EX9: Como dizer que alguém é descendente de outra pessoa?

descendente(X, Y) :- descendente(Y, X).

% EX10: Como dizer que duas pessoas são meias-irmãs?

meias_irmas(X,Y) :- pai(Z,X), pai(Z,Y), mae(W,X), mae(V,Y), W \= V, X \= Y.
meias_irmas(X,Y) :- pai(W,X), pai(Z,Y), mae(V,X), mae(V,Y), W \= Z, X \= Y.

% EX11: Como exprimir a relação de "primo em 2° grau"?

primo_segundo_grau(X, Y) :- primo(X, Y).
primo_segundo_grau(X, Y) :- antepassado(Z, X), primo(Z, Y).
primo_segundo_grau(X, Y) :- antepassado(Z, Y), primo(Z, X).

% EX12: Como ver se duas pessoas têm algum parentesco?

parentesco(X, Y) :-
    pai(X, Y).
parentesco(X, Y) :-
    mae(X, Y).
parentesco(X, Y) :-
    irmao(X, Y).
parentesco(X, Y) :-
    irma(X, Y).
parentesco(X, Y) :-
    antepassado(Z, X),
    parentesco(Z, Y).

% Da mesma forma, faça o exercício das estradas, conforme as aulas teóricas, ie. com a relação:

e(lisboa, santarem).
e(santarem, coimbra).
e(santarem, caldas).
e(caldas, lisboa).
e(coimbra, porto).
e(lisboa, evora).
e(evora, beja).
e(lisboa, faro).
e(beja, faro).

% Implemente as relações a/2, cam/2 e auxiliares como discutido nas aulas teóricas.

a(X,Y) :- e(X,Y).
a(X,Y) :- e(Y,X).

figura(N, N).
figura(N, c(N,_)).
figura(N, c(_,K)) :- figura(N, K).

nao_figura(N,K) :- \+ figura(N,K).

cam(X,Y) :- a(X,Y,Z).
cam(X,Y,Z) :- a(X,Y), nao_figura(Y,Z).
cam(X,Y,Z) :- a(X,W), nao_figura(W,Z), cam(W,Y,c(W,z)).
