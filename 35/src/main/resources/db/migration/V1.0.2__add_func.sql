/*==============================================================*/
/* Table: SYS_FUNC                                              */
/*==============================================================*/
create table SYS_FUNC
(
    ID                   varchar(32) not null comment '主键',
    NAME                 varchar(60) comment '功能名称',
    URL                  varchar(60) comment '功能地址',
    PID                  varchar(32) comment '父功能id',
    SORT                 int comment '顺序号',
    GMT_CREATE           timestamp default CURRENT_TIMESTAMP comment '新增时间，默认当前时间，不随数据改变而改变',
    GMT_MODIFIED         timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间，默认当前时间，随数据改变而改变',
    primary key (ID)
);

alter table SYS_FUNC comment '系统功能';

/* 初始化数据 */
INSERT INTO SYS_FUNC(`ID`, `NAME`, `URL`, `PID`, `SORT`, `GMT_CREATE`, `GMT_MODIFIED`) VALUES ('8fc7c56295e542aaa436c4dbf0048578', '个人中心', '/user/index', NULL, 2, '2020-07-17 11:44:28', '2020-07-17 11:47:53');


/*==============================================================*/
/* Table: SYS_ROLE_FUNC                                         */
/*==============================================================*/
create table SYS_ROLE_FUNC
(
    ID                   varchar(32) not null comment '主键',
    ROLE_ID              varchar(32) comment '角色ID',
    FUNC_ID              varchar(32) comment '功能ID',
    primary key (ID)
);

alter table SYS_ROLE_FUNC comment '角色功能';

alter table SYS_ROLE_FUNC add constraint FK_Reference_4 foreign key (ROLE_ID)
    references SYS_ROLE (ID) on delete restrict on update cascade;

alter table SYS_ROLE_FUNC add constraint FK_Reference_5 foreign key (FUNC_ID)
    references SYS_FUNC (ID) on delete cascade on update cascade;

/* 初始化数据 */
INSERT INTO SYS_ROLE_FUNC(`ID`, `ROLE_ID`, `FUNC_ID`) VALUES ('7fab248318fe44d5bd1a07fa2e80c4a6', 'fb3ecfd7c70311ea996b2cfda1ba69cd', '8fc7c56295e542aaa436c4dbf0048578');

