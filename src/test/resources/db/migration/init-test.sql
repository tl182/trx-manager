create table account
(
    id bigint primary key,
    balance decimal not null default 0
);

create sequence account_seq start with 1;

/* [jooq ignore start] */
insert into account (id, balance) values
    (nextval('account_seq'), 13.336),
    (nextval('account_seq'), 34.349);
/* [jooq ignore stop] */
