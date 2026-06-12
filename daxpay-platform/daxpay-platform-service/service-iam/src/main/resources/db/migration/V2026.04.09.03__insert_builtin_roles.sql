-- 插入系统内置角色
INSERT INTO iam_role (id, code, name_cn, name_en, client_code, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted)
VALUES 
    (1928374650192837466, 'merchant_admin', '商户管理员', 'Merchant Admin', 'merchant', true, '系统内置商户管理员角色', 1, NOW(), 1, NOW(), 0, false),
    (1928374650192837467, 'isv_admin', '服务商管理员', 'ISV Admin', 'isv', true, '系统内置服务商管理员角色', 1, NOW(), 1, NOW(), 0, false);
