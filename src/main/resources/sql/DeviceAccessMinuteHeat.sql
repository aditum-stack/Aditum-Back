-- auto Generated on 2019-06-09 00:50:24 
-- DROP TABLE IF EXISTS `device_access_minute_heat`; 
CREATE TABLE device_access_minute_heat(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `imei` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '设备ID Unique',
    `current_minute_time` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '按分钟计算',
    `current_minute_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '访问次数',
    `current_minute_in_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '进门访问次数',
    `current_minute_out_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '出门访问次数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_deleted` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '删除标记',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'device_access_minute_heat';
