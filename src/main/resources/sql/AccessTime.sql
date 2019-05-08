-- auto Generated on 2019-05-08 10:49:10 
-- DROP TABLE IF EXISTS `access_time`; 
CREATE TABLE access_time(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `personnel_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户ID Unique',
    `average_earliest_access_time` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '平均最早访问时间',
    `average_earliest_access_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '次数',
    `average_latest_access_time` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '平均最晚访问时间',
    `average_latest_access_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '次数',
    `average_daily_frequency` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '平均每天访问频率',
    `average_daily_frequency_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '次数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'access_time';
