-- auto Generated on 2019-05-08 21:59:56 
-- DROP TABLE IF EXISTS `device_access_heat`; 
CREATE TABLE device_access_heat(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `imei` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '设备ID Unique',
    `current_hour_time` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '按小时计算',
    `current_hour_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '访问次数',
    `current_hour_in_count` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '进门访问次数',
    `current_hour_out_count` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '出门访问次数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'device_access_heat';


