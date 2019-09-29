create sequence account_seq start with 100;

create table account
(
    id bigint default account_seq.nextval primary key,
    balance decimal not null default 0,
    modified timestamp not null default current_timestamp
);


create sequence transfer_seq start with 100;

create table transfer
(
    id bigint default transfer_seq.nextval primary key,
    from_id bigint not null references account (id),
    to_id bigint not null references account (id),
    amount decimal not null default 0,
    status varchar (255) not null default 'CREATED' check (status in ('CREATED', 'SUCCEEDED', 'FAILED'))
);
