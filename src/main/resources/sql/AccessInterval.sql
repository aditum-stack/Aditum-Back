-- auto Generated on 2019-05-08 10:49:03 
-- DROP TABLE IF EXISTS `access_interval`; 
CREATE TABLE access_interval(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `personnel_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户ID Unique',
    `mean_time_retention` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '平均滞留时间(从入到出)',
    `first_address_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '次数',
    `mean_time_out` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '平均外出时间(从出到入)',
    `second_address_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '次数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'access_interval';
