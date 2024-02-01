CREATE TABLE users
(
    username character varying(255) PRIMARY KEY,
    password character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    user_role character varying(50) NOT NULL
);

CREATE TABLE artista
(
    id_artista serial PRIMARY KEY,
    nome character varying(255) NOT NULL,
    tipo_arte character varying(255) NOT NULL,
    estado boolean,
    imagem character varying(255),
    descricao character varying(255) NOT NULL,
    localizacao character varying(255),
    num_avaliacoes integer,
    rating double precision
);

CREATE TABLE atuacoes
(
    id_atuacao serial PRIMARY KEY,
    id_artista integer REFERENCES artista(id_artista),
    atuar boolean,
    data_atuacao date,
    coordenadas geometry(Point,4326)
);

CREATE TABLE donativos
(
    id_donativo serial PRIMARY KEY,
    id_artista integer REFERENCES artista(id_artista),
    username character varying(50) REFERENCES users(username),
    valor double precision NOT NULL
);
