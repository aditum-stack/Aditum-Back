-- auto Generated on 2018-11-21 12:52:20 
-- DROP TABLE IF EXISTS `person`; 
CREATE TABLE person(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `personnel_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户ID Unique',
    `personnel_name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户姓名',
    `community_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '社区ID',
    `personnel_address` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户社区住址',
    `personnel_phone` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户手机号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY(`personnel_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'person';
