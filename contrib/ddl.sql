create extension "uuid-ossp";
create extension "pg_trgm";

create table "pessoas"
(
    "id"         uuid               not null default uuid_generate_v4(),
    "apelido"    varchar(32) unique not null,
    "nome"       varchar(100)       not null,
    "nascimento" date               not null,
    "stack"      text,
    primary key ("id")
);

create index pessoas_idx_apelido on pessoas (apelido);
create index pessoas_idx_term on pessoas using gin (lower(apelido||' '||nome||' '||stack) gin_trgm_ops);