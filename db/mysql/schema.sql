drop table if exists shrike_vendor;
create table shrike_vendor
(
    app_key     char(16)    not null
        primary key,
    app_secret  binary(32)  not null comment '32 Bytes AppSecret',
    description varchar(64) not null,
    enabled     tinyint(1)  not null
);
