create table account
(
    id bigint primary key,
    balance decimal not null default 0
);

create sequence account_seq start with 1;
