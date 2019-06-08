-- auto Generated on 2019-06-09 00:50:24 
-- DROP TABLE IF EXISTS `device_access_minute_heat`; 
CREATE TABLE device_access_minute_heat(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `imei` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '�豸ID Unique',
    `current_minute_time` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '�����Ӽ���',
    `current_minute_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '���ʴ���',
    `current_minute_in_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '���ŷ��ʴ���',
    `current_minute_out_count` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '���ŷ��ʴ���',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_deleted` INTEGER(12) NOT NULL DEFAULT -1 COMMENT 'ɾ�����',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'device_access_minute_heat';
