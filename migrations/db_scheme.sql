--liquibase formatted sql

--changeset ilyadiev:1
create table if not exists chat
(
    chat_id         bigint not null primary key
);

create table if not exists link
(
    link_id         bigint generated always as identity,
    url             text                        not null,
    last_update     timestamp with time zone    not null,
    last_check      timestamp                   not null,
    meta_info       text,

    primary key (link_id),
    unique(url)
    );

create table if not exists chat_link
(
    chat_id         bigint not null,
    link_id         bigint not null,
    primary key (chat_id, link_id)
    );

alter table chat_link
    add constraint chat_link_chat_fkey foreign key (chat_id)
        references chat (chat_id),
    add constraint chat_link_link_fkey foreign key (link_id)
        references link (link_id)

--rollback drop table chat, link, chat_link;
