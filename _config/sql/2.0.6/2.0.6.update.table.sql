ALTER TABLE `iam_perm_menu`
    CHANGE COLUMN `admin` `internal` bit(1) NOT NULL COMMENT '系统菜单' AFTER `hidden_header_content`;
