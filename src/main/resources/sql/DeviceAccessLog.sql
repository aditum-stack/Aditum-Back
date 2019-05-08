-- auto Generated on 2019-05-08 18:16:47 
-- DROP TABLE IF EXISTS `device_access_log`; 
CREATE TABLE device_access_log(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `imei` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '设备ID Unique',
    `record_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '日志ID Unique',
    `access_time` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '访问时间',
    `access_type` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '访问类型',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'device_access_log';


ALTER TABLE device_access_log
ADD COLUMN `record_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '日志ID Unique' AFTER `imei`;
