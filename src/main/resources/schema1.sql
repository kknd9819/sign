DROP TABLE IF EXISTS `request_params`;
CREATE TABLE `request_params` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `modify_date` datetime DEFAULT NULL,
  `mobile` varchar(255) NOT NULL COMMENT '手机',
  `nick_name` VARCHAR (255) DEFAULT NULL COMMENT '昵称',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `token` varchar(255) NOT NULL COMMENT '令牌',
  `version` varchar(255) NOT NULL COMMENT '版本号',
  `version_code` varchar(255) NOT NULL COMMENT '版本名',
  `regid_login` varchar(255) NOT NULL COMMENT '注册ID',
  `portrait_path` varchar(255) DEFAULT NULL COMMENT '头像相对路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


INSERT INTO `request_params` VALUES (1,null,'18684639007',null,'2f9572a5d053bfedeed5c106923c65f9','BKDFVS57MQ6B6ARDUFY6Q209XWRHZETL','36','1.0.4','SgHkCjnB/YubvxwrYLdE1+kU5diSjOqfRQMG0zUUWi4=',null);