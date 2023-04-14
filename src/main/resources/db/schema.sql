create sequence if not exists articles_seq increment by 1;
create sequence if not exists comments_seq increment by 1;
create sequence if not exists revinfo_seq increment by 50;
create sequence if not exists roles_seq increment by 1;
create sequence if not exists tags_seq increment by 1;
create sequence if not exists users_roles_seq increment by 1;
create sequence if not exists users_seq increment by 1;

create table if not exists roles
(
    id          bigint not null primary key,
    description varchar(255),
    name        varchar(255)
);

create table if not exists users
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

create table if not exists users_roles
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

create table if not exists follows
(
    follow_id bigint not null,
    user_id   bigint not null,
    primary key (follow_id, user_id)
);

create table if not exists articles
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

create table if not exists articles_favorites
(
    article_id bigint not null,
    user_id    bigint not null,
    primary key (article_id, user_id)
);

create table if not exists tags
(
    id   bigint       not null primary key,
    name varchar(255) not null
);

create table if not exists articles_tags
(
    article_id bigint not null,
    tag_id     bigint not null,
    primary key (article_id, tag_id)
);

create table if not exists comments
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

create table if not exists revinfo
(
    rev      integer not null primary key,
    revtstmp bigint
);

create table if not exists users_aud
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
