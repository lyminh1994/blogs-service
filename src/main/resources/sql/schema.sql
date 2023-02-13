create sequence articles_seq increment by 50;
alter sequence articles_seq owner to postgres;
create sequence comments_seq increment by 50;
alter sequence comments_seq owner to postgres;
create sequence revinfo_seq increment by 50;
alter sequence revinfo_seq owner to postgres;
create sequence roles_seq increment by 50;
alter sequence roles_seq owner to postgres;
create sequence tags_seq increment by 50;
alter sequence tags_seq owner to postgres;
create sequence users_roles_seq increment by 50;
alter sequence users_roles_seq owner to postgres;
create sequence users_seq increment by 50;
alter sequence users_seq owner to postgres;

create table articles_favorites
(
    article_id bigint not null,
    user_id    bigint not null,
    primary key (article_id, user_id)
);

alter table articles_favorites
    owner to postgres;

create table articles_tags
(
    article_id bigint not null,
    tag_id     bigint not null,
    primary key (article_id, tag_id)
);

alter table articles_tags
    owner to postgres;

create table follows
(
    follow_id bigint not null,
    user_id   bigint not null,
    primary key (follow_id, user_id)
);

alter table follows
    owner to postgres;

create table revinfo
(
    rev      integer not null
        primary key,
    revtstmp bigint
);

alter table revinfo
    owner to postgres;

create table roles
(
    id          bigint not null
        primary key,
    description varchar(255),
    name        varchar(255)
);

alter table roles
    owner to postgres;

create table tags
(
    id   bigint       not null
        primary key,
    name varchar(255) not null
);

alter table tags
    owner to postgres;

create table users
(
    id                    bigint       not null
        primary key,
    created_at            timestamp(6),
    created_by            varchar(255) not null,
    public_id             varchar(255) not null
        constraint uk_s24bux761rbgowsl7a4b386ba
            unique,
    updated_at            timestamp(6),
    updated_by            varchar(255),
    version               smallint     not null,
    birthday              date,
    email                 varchar(255)
        constraint uk_6dotkott2kjsp8vw4d0m25fb7
            unique,
    enabled               boolean      not null,
    failed_login_attempts integer      not null,
    first_name            varchar(255),
    gender                integer,
    last_name             varchar(255),
    last_successful_login timestamp(6),
    password              varchar(255),
    phone                 varchar(255)
        constraint uk_du5v5sr43g5bfnji4vb8hg5s3
            unique,
    profile_image         varchar(255),
    username              varchar(50)  not null
        constraint uk_r43af9ap4edm43mmtq01oddj6
            unique,
    verification_token    varchar(255)
);

alter table users
    owner to postgres;

create table articles
(
    id          bigint       not null
        primary key,
    created_at  timestamp(6),
    created_by  varchar(255) not null,
    public_id   varchar(255) not null
        constraint uk_qsi7ot2lswfqp9a772eqclp83
            unique,
    updated_at  timestamp(6),
    updated_by  varchar(255),
    version     smallint     not null,
    body        varchar(255),
    description varchar(255),
    slug        varchar(255)
        constraint uk_sn7al9fwhgtf98rvn8nxhjt4f
            unique,
    title       varchar(255),
    user_id     bigint
        constraint fklc3sm3utetrj1sx4v9ahwopnr
            references users
);

alter table articles
    owner to postgres;

create table comments
(
    id         bigint       not null
        primary key,
    created_at timestamp(6),
    created_by varchar(255) not null,
    public_id  varchar(255) not null
        constraint uk_6pbs13bct5e53pu6e9dk99tuv
            unique,
    updated_at timestamp(6),
    updated_by varchar(255),
    version    smallint     not null,
    body       varchar(255),
    article_id bigint
        constraint fkk4ib6syde10dalk7r7xdl0m5p
            references articles,
    user_id    bigint
        constraint fk8omq0tc18jd43bu5tjh6jvraq
            references users
);

alter table comments
    owner to postgres;

create table users_aud
(
    id            bigint  not null,
    rev           integer not null
        constraint fkc4vk4tui2la36415jpgm9leoq
            references revinfo,
    revtype       smallint,
    birthday      date,
    email         varchar(255),
    enabled       boolean,
    first_name    varchar(255),
    gender        integer,
    last_name     varchar(255),
    password      varchar(255),
    phone         varchar(255),
    profile_image varchar(255),
    username      varchar(255),
    primary key (rev, id)
);

alter table users_aud
    owner to postgres;

create table users_roles
(
    id         bigint       not null
        primary key,
    created_at timestamp(6),
    created_by varchar(255) not null,
    public_id  varchar(255) not null
        constraint uk_lwk08wy68nq6irqmwjyyddnld
            unique,
    updated_at timestamp(6),
    updated_by varchar(255),
    version    smallint     not null,
    role_id    bigint
        constraint fkj6m8fwv7oqv74fcehir1a9ffy references roles,
    user_id    bigint
        constraint fk2o0jvgh89lemvvo17cbqvdxaa references users
);

alter table users_roles
    owner to postgres;