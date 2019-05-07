-- auto Generated on 2018-11-21 12:52:13 
-- DROP TABLE IF EXISTS `device`; 
CREATE TABLE device(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `imei` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '设备ID Unique',
    `alias` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '设备别名',
    `community_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '社区ID',
    `device_status` INTEGER(12) UNSIGNED NOT NULL DEFAULT 0 COMMENT '设备状态 0未激活 1已激活(但未使用) 2在线(可使用状态) 3离线',
    `activate_time` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '设备激活时间',
    `last_online_time` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '设备最后在线时间',
    `last_offline_time` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '设备最后离线时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY(`imei`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'device';
