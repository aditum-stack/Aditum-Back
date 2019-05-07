-- auto Generated on 2018-11-21 12:52:26 
-- DROP TABLE IF EXISTS `record`; 
CREATE TABLE record(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `imei` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '设备ID',
    `personnel_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '访客ID',
    `visite_time` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '访问时间',
    `visite_status` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '访问状态 0进入社区 1离开社区 2识别失败 3响应超时',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'record';
