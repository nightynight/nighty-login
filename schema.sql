-- 使用Mysql数据库

-- 数据库初始化脚本

-- 创建数据库
DROP DATABASE IF EXISTS db_nighty;
CREATE DATABASE db_nighty;
-- 使用数据库
use db_nighty;


/******************** Add Table: t_resource ************************/

/* Build Table Structure */
CREATE TABLE t_resource
(
	id VARCHAR(36) NOT NULL,
	name VARCHAR(20)
		COMMENT '资源名称' NOT NULL,
	type VARCHAR(1)
		COMMENT '资源类型,"0"表示用户权限，"1"表示管理员权限,可扩展' NOT NULL,
	parent_id VARCHAR(36)
		COMMENT '为"-1"表示该节点是用户权限，
不是"-1"表示为管理员权限，为"0"表示该节点是顶层节点' NOT NULL,
	description NVARCHAR(250) NULL
) ENGINE=InnoDB;

/* Add Primary Key */
ALTER TABLE t_resource ADD CONSTRAINT pkt_resource
	PRIMARY KEY (id);

/* Add Comments */
ALTER TABLE t_resource COMMENT = '资源表,树形结构';


/******************** Add Table: t_role ************************/

/* Build Table Structure */
CREATE TABLE t_role
(
	id VARCHAR(36) NOT NULL,
	name VARCHAR(20) NOT NULL,
	type VARCHAR(1)
	COMMENT '角色类型,"0"表示用户角色，"1"表示管理员角色,可扩展' NOT NULL,
	description NVARCHAR(250) NULL
) ENGINE=InnoDB;

/* Add Primary Key */
ALTER TABLE t_role ADD CONSTRAINT pkt_role
PRIMARY KEY (id);

/* Add Comments */
ALTER TABLE t_role COMMENT = '角色表';


/******************** Add Table: t_user ************************/

/* Build Table Structure */
CREATE TABLE t_user
(
	id VARCHAR(36) NOT NULL,
	username VARCHAR(20)
	COMMENT '登录时的用户名，不能包含中文' NOT NULL,
	nickname VARCHAR(20)
	COMMENT '昵称' NOT NULL,
	password VARCHAR(128)
	COMMENT '登录密码，存的是MD5加密后的密码' NOT NULL,
	salt VARCHAR(50)
	COMMENT '密码盐' NOT NULL,
	email VARCHAR(50)
	COMMENT '邮箱,可以用来登录或重置密码' NOT NULL,
	phone VARCHAR(20)
	COMMENT '手机号' NULL,
	create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	COMMENT '创建时间' NOT NULL,
	last_login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	COMMENT '最近一次登录时间' NOT NULL
) ENGINE=InnoDB;

/* Add Primary Key */
ALTER TABLE t_user ADD CONSTRAINT pkt_user
PRIMARY KEY (id);

/* Add Comments */
ALTER TABLE t_user COMMENT = '用户表';


/******************** Add Table: t_user_role ************************/

/* Build Table Structure */
CREATE TABLE t_user_role
(
	id VARCHAR(36) NOT NULL,
	user_id VARCHAR(36) NULL,
	role_id VARCHAR(36) NULL
) ENGINE=InnoDB;

/* Add Primary Key */
ALTER TABLE t_user_role ADD CONSTRAINT pkt_user_role
PRIMARY KEY (id);

/* Add Comments */
ALTER TABLE t_user_role COMMENT = '用户角色关系表';


/******************** Add Table: t_role_resource ************************/

/* Build Table Structure */
CREATE TABLE t_role_resource
(
	id VARCHAR(36) NOT NULL,
	role_id VARCHAR(36) NOT NULL,
	resource_id VARCHAR(36) NOT NULL
) ENGINE=InnoDB;

/* Add Primary Key */
ALTER TABLE t_role_resource ADD CONSTRAINT pkt_role_resource
PRIMARY KEY (id);

/* Add Comments */
ALTER TABLE t_role_resource COMMENT = '角色资源关系表';


INSERT INTO t_resource
(id, name, type, parent_id, description)
VALUES
	('1', 'visit', '0', '-1', '游客权限');


INSERT INTO t_role
(id, name, type, description)
VALUES
	('20170326164711', 'guest', '0', '还未登录的访问者');
INSERT INTO t_role
(id, name, type, description)
VALUES
	('20170326164710', 'admin', '1', '系统管理员');


INSERT INTO t_user (id,nickname,username,password,salt,email,phone)
VALUES ('1','admin','admin','D03C14521760164A3711E24082A297CD','76CgONmqOb','nightynight_cc@163.com','15522331234');
INSERT INTO t_user (id,nickname,username,password,salt,email,phone)
VALUES ('2','guest','guest','B144CC01B937A34A01AB81ACE6B74FFF','Ex1B2ToG9X','1024234001@qq.com','12312345678');


INSERT INTO t_role_resource (id, role_id, resource_id) VALUES ('20170418230900','20170326164711','1');


INSERT INTO t_user_role (id, role_id, user_id) VALUES ('20170418231000','20170326164710','1');
INSERT INTO t_user_role (id, role_id, user_id) VALUES ('20170418231001','20170326164711','6b4ff786-401c-40f4-a439-87c9decabd89');