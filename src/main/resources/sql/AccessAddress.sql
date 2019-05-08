-- auto Generated on 2019-05-08 10:48:57 
-- DROP TABLE IF EXISTS `access_address`; 
CREATE TABLE access_address(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `personnel_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户ID Unique',
    `first_address` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '最常访问地址',
    `first_address_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '次数',
    `second_address` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '第二访问地址',
    `second_address_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '次数',
    `total_address` VARCHAR(500) NOT NULL DEFAULT '' COMMENT '累计访问地点',
    `total_address_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '累计访问地点数量',
    `total_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '累计访问次数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'access_address';
