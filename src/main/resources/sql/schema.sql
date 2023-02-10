create sequence hibernate_sequence;

alter sequence hibernate_sequence owner to postgres;

create table bank_statements
(
    id                  serial primary key,
    transaction_date    date,
    ref_no              varchar(50),
    change              varchar(1),
    debit               numeric,
    credit              numeric,
    balance             numeric,
    transaction_details text,
    amount              numeric
);

alter table bank_statements
    owner to postgres;

create table users
(
    id                    serial primary key,
    public_id             varchar(255) not null unique,
    username              varchar(50)  not null unique,
    email                 varchar(255) unique,
    phone                 varchar(20) unique,
    password              varchar(255),
    first_name            varchar(255),
    last_name             varchar(255),
    gender                integer,
    birthday              date,
    profile_image         varchar(255),
    verification_token    varchar(255),
    enabled               boolean      not null,
    failed_login_attempts integer      not null,
    last_successful_login timestamp,
    version               smallint     not null,
    created_at            timestamp,
    created_by            varchar(255) not null,
    updated_at            timestamp,
    updated_by            varchar(255)
);

alter table users
    owner to postgres;

create table roles
(
    id          serial primary key,
    description varchar(255),
    name        varchar(255)
);

alter table roles
    owner to postgres;

create table articles
(
    id          serial primary key,
    public_id   varchar(255) not null unique,
    slug        varchar(255) unique,
    title       varchar(255),
    body        text,
    description text,
    version     smallint     not null,
    created_at  timestamp,
    created_by  varchar(255) not null,
    updated_at  timestamp,
    updated_by  varchar(255),
    user_id     bigint references users
);

alter table articles
    owner to postgres;

create table tags
(
    id   serial primary key,
    name varchar(255) not null
);

alter table tags
    owner to postgres;

create table comments
(
    id         serial primary key,
    public_id  varchar(255) not null unique,
    body       text,
    version    smallint     not null,
    created_at timestamp,
    created_by varchar(255) not null,
    updated_at timestamp,
    updated_by varchar(255),
    article_id bigint references articles,
    user_id    bigint references users
);

alter table comments
    owner to postgres;

create table users_roles
(
    id         serial primary key,
    public_id  varchar(255) not null unique,
    version    smallint     not null,
    created_at timestamp,
    created_by varchar(255) not null,
    updated_at timestamp,
    updated_by varchar(255),
    role_id    bigint references roles,
    user_id    bigint references users
);

alter table users_roles
    owner to postgres;

create table follows
(
    follow_id bigint not null,
    user_id   bigint not null,
    primary key (follow_id, user_id)
);

alter table follows
    owner to postgres;

create table articles_favorites
(
    article_id bigint not null references users,
    user_id    bigint not null references users,
    primary key (article_id, user_id)
);

alter table articles_favorites
    owner to postgres;

create table articles_tags
(
    article_id bigint not null references articles,
    tag_id     bigint not null references tags,
    primary key (article_id, tag_id)
);

alter table articles_tags
    owner to postgres;

create table revinfo
(
    rev      integer not null primary key,
    revtstmp bigint
);

alter table revinfo
    owner to postgres;

create table users_aud
(
    id                 bigint  not null,
    rev                integer not null references revinfo,
    revtype            smallint,
    birthday           date,
    email              varchar(255),
    enabled            boolean,
    first_name         varchar(255),
    gender             integer,
    last_name          varchar(255),
    password           varchar(255),
    phone              varchar(255),
    profile_image      varchar(255),
    username           varchar(255),
    verification_token varchar(255),
    primary key (id, rev)
);

alter table users_aud
    owner to postgres;

