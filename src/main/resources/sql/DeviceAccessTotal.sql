-- auto Generated on 2019-05-08 18:16:57 
-- DROP TABLE IF EXISTS `device_access_total`; 
CREATE TABLE device_access_total(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `imei` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '设备ID Unique',
    `total_access_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '总访问次数',
    `total_day_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '使用天数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'device_access_total';
