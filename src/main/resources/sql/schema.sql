create sequence articles_seq increment by 1;
create sequence comments_seq increment by 1;
create sequence revinfo_seq increment by 50;
create sequence roles_seq increment by 1;
create sequence tags_seq increment by 1;
create sequence users_roles_seq increment by 1;
create sequence users_seq increment by 1;

create table roles
(
    id          bigint not null primary key,
    description varchar(255),
    name        varchar(255)
);

create table users
(
    id                    bigint       not null primary key,
    created_at            timestamp(6),
    created_by            varchar(255) not null,
    public_id             varchar(255) not null unique,
    updated_at            timestamp(6),
    updated_by            varchar(255),
    version               smallint     not null,
    birthday              date,
    email                 varchar(255) unique,
    enabled               boolean      not null,
    failed_login_attempts integer      not null,
    first_name            varchar(255),
    gender                integer,
    last_name             varchar(255),
    last_successful_login timestamp(6),
    password              varchar(255),
    phone                 varchar(255) unique,
    profile_image         varchar(255),
    username              varchar(50)  not null unique,
    verification_token    varchar(255)
);

create table users_roles
(
    id         bigint       not null primary key,
    created_at timestamp(6),
    created_by varchar(255) not null,
    public_id  varchar(255) not null unique,
    updated_at timestamp(6),
    updated_by varchar(255),
    version    smallint     not null,
    role_id    bigint references roles,
    user_id    bigint references users
);

create table follows
(
    follow_id bigint not null,
    user_id   bigint not null,
    primary key (follow_id, user_id)
);

create table articles
(
    id          bigint       not null primary key,
    created_at  timestamp(6),
    created_by  varchar(255) not null,
    public_id   varchar(255) not null unique,
    updated_at  timestamp(6),
    updated_by  varchar(255),
    version     smallint     not null,
    body        varchar(255),
    description varchar(255),
    slug        varchar(255) unique,
    title       varchar(255),
    user_id     bigint references users
);

create table articles_favorites
(
    article_id bigint not null,
    user_id    bigint not null,
    primary key (article_id, user_id)
);

create table tags
(
    id   bigint       not null primary key,
    name varchar(255) not null
);

create table articles_tags
(
    article_id bigint not null,
    tag_id     bigint not null,
    primary key (article_id, tag_id)
);

create table comments
(
    id         bigint       not null primary key,
    created_at timestamp(6),
    created_by varchar(255) not null,
    public_id  varchar(255) not null unique,
    updated_at timestamp(6),
    updated_by varchar(255),
    version    smallint     not null,
    body       varchar(255),
    article_id bigint references articles,
    user_id    bigint references users
);

create table revinfo
(
    rev      integer not null primary key,
    revtstmp bigint
);

create table users_aud
(
    id            bigint  not null,
    rev           integer not null references revinfo,
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
