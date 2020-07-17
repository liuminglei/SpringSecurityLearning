/*==============================================================*/
/* Table: SYS_ROLE                                              */
/*==============================================================*/
create table SYS_ROLE
(
    ID                   varchar(32) not null comment '主键',
    NAME                 varchar(60) comment '角色名称',
    CODE                 varchar(60) comment '角色标识',
    GMT_CREATE           timestamp default CURRENT_TIMESTAMP comment '新增时间，默认当前时间，不随数据改变而改变',
    GMT_MODIFIED         timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间，默认当前时间，随数据改变而改变',
    primary key (ID)
);

alter table SYS_ROLE comment '系统角色';

/* 默认数据  */
INSERT INTO SYS_ROLE (`ID`, `NAME`, `CODE`, `GMT_CREATE`, `GMT_MODIFIED`) VALUES ('fb3ecfd7c70311ea996b2cfda1ba69cd', '普通用户', 'User', '2020-07-16 09:31:41', '2020-07-16 09:31:41');


/*==============================================================*/
/* Table: SYS_USER_ROLE                                         */
/*==============================================================*/
create table SYS_USER_ROLE
(
    ID                   varchar(32) not null comment '主键',
    USER_ID              varchar(32) comment '用户ID',
    ROLE_ID              varchar(32) comment '角色ID',
    primary key (ID)
);

alter table SYS_USER_ROLE comment '用户角色';

alter table SYS_USER_ROLE add constraint FK_Reference_1 foreign key (USER_ID)
    references SYS_USER (ID) on delete cascade on update cascade;

alter table SYS_USER_ROLE add constraint FK_Reference_2 foreign key (ROLE_ID)
    references SYS_ROLE (ID) on delete cascade on update cascade;


/* 默认数据  */
INSERT INTO SYS_USER_ROLE (`ID`, `USER_ID`, `ROLE_ID`) VALUES ('34650841c70411ea996b2cfda1ba69cd', '2031a4adc78942d59188cea7927e6304', 'fb3ecfd7c70311ea996b2cfda1ba69cd');

