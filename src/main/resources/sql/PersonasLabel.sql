-- auto Generated on 2019-05-08 10:49:15 
-- DROP TABLE IF EXISTS `personas_label`; 
CREATE TABLE personas_label(
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `label_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '标签ID Unique',
    `label_name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '标签名称',
    `label_type` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '标签类别',
    `label_desc` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '标签描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'personas_label';


ALTER TABLE personas_label
ADD COLUMN `label_desc` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '标签描述' AFTER `label_type`;
