create sequence account_seq start with 100;

create table account
(
    id bigint default account_seq.nextval primary key,
    balance decimal not null default 0
);
