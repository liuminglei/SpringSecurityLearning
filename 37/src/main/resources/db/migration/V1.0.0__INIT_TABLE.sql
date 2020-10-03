/*==============================================================*/
/* Table: SYS_USER                                              */
/*==============================================================*/
create table SYS_USER
(
   ID                   varchar(32) not null comment '主键',
   NAME                 varchar(60) comment '姓名',
   USERNAME              varchar(60) comment '账号',
   PASSWORD             varchar(100) comment '密码',
   AGE                  int comment '年龄',
   STATE                int comment '状态',
   SORT                 int comment '顺序号',
   GMT_CREATE           timestamp default CURRENT_TIMESTAMP comment '新增时间，默认当前时间，不随数据改变而改变',
   GMT_MODIFIED         timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间，默认当前时间，随数据改变而改变',
   primary key (ID)
);

alter table SYS_USER comment '系统用户';

-- 默认数据

INSERT INTO `sys_user`(`ID`, `NAME`, `USERNAME`, `PASSWORD`, `AGE`, `STATE`, `SORT`, `GMT_CREATE`, `GMT_MODIFIED`) VALUES ('2031a4adc78942d59188cea7927e6304', '张三', 'zhangsan', '$2a$10$JrW0b7PBgL9riq2mOAPbRuymlpCRJHVncbGnTjg7hB/PExyGy.YbW', 18, 1, 1, '2020-07-13 17:34:14', '2020-07-13 17:54:06');
