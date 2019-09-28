create sequence account_seq start with 100;

create table account
(
    id bigint default account_seq.nextval primary key,
    balance decimal not null default 0
);

/* [jooq ignore start] */
insert into account (id, balance) values
    (1, 13.336),
    (2, 34.349);
/* [jooq ignore stop] */
