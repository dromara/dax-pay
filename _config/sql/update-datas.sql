-- 设备管理菜单: iot → device 命名修正 + 目录层级合并(去除云音箱/云打印中间目录层)
-- 执行顺序: update-tables.sql -> update-datas.sql
-- 基于现有 iam_perm_menu 记录 UPDATE/DELETE(菜单已存在, 非新增)

-- 1. 设备管理 根目录 (id=9)
UPDATE "iam_perm_menu" SET
    menu_code          = 'device',
    name               = 'DeviceManagement',
    i18n_key           = 'menu.device',
    path               = '/device',
    last_modified_time = now()
WHERE id = 9;

-- 2. 码牌 (id=901)
UPDATE "iam_perm_menu" SET
    menu_code          = 'device:qrcode',
    name               = 'DeviceQrCode',
    i18n_key           = 'menu.device.qrcode',
    path               = '/device/qrcode',
    last_modified_time = now()
WHERE id = 901;

-- 3. 删除 云音箱 目录 (id=902)
--    去除中间目录层, 子菜单 90202 提升为顶层并继承 title/key/path
DELETE FROM "iam_perm_menu" WHERE id = 902;

-- 4. 删除 云打印 目录 (id=903)
--    去除中间目录层, 子菜单 90302 提升为顶层并继承 title/key/path
DELETE FROM "iam_perm_menu" WHERE id = 903;

-- 5. 设备厂商管理 (id=90201) 提升为设备管理顶层(pid 902→9)
--    厂商配置为平台级通用表(device_vendor_config), 统一入口, 不再按设备类型分散
UPDATE "iam_perm_menu" SET
    pid                = 9,
    menu_code          = 'payment:device:vendorConfig',
    name               = 'VendorManage',
    title_cn           = '设备厂商管理',
    title_en           = 'Device Vendor Management',
    i18n_key           = 'menu.device.vendor',
    component          = '/payment/device/vendor/VendorManage',
    path               = '/device/vendor',
    sort_no            = 4,
    last_modified_time = now()
WHERE id = 90201;

-- 6. 云音箱 (id=90202) 提升为顶层, 合并原目录 title/key/path
--    原: pid=902, title=云音箱管理, key=menu.device.speaker.manage, path=/device/speaker/manage
--    现: pid=9,     title=云音箱,     key=menu.device.speaker,         path=/device/speaker
UPDATE "iam_perm_menu" SET
    pid                = 9,
    menu_code          = 'payment:device:speaker',
    name               = 'DeviceSpeakerList',
    title_cn           = '云音箱',
    title_en           = 'Cloud Speaker',
    i18n_key           = 'menu.device.speaker',
    component          = '/payment/device/speaker/DeviceSpeakerList',
    path               = '/device/speaker',
    sort_no            = 2,
    last_modified_time = now()
WHERE id = 90202;

-- 7. 云打印 (id=90302) 提升为顶层, 合并原目录 title/key/path, 并接入已实现的列表组件
--    原: pid=903, title=云打印管理, key=menu.device.printer.manage, path=/device/printer/device
--    现: pid=9,     title=云打印,     key=menu.device.printer,         path=/device/printer
UPDATE "iam_perm_menu" SET
    pid                = 9,
    menu_code          = 'payment:device:printer',
    name               = 'DevicePrinterList',
    title_cn           = '云打印',
    title_en           = 'Cloud Printer',
    i18n_key           = 'menu.device.printer',
    component          = '/payment/device/printer/DevicePrinterList',
    path               = '/device/printer',
    sort_no            = 3,
    last_modified_time = now()
WHERE id = 90302;

-- 8. 删除 云打印 > 厂商管理 (id=90301)
--    厂商配置统一到顶层 90201, 各设备类型下不再单设厂商管理入口
DELETE FROM "iam_perm_menu" WHERE id = 90301;

-- 9. Dashboard 目录 (id=1): 默认进入工作台 (/analytics → /workspace)
UPDATE "iam_perm_menu" SET
    redirect            = '/workspace',
    last_modified_time  = now()
WHERE id = 1;

-- 10. 工作台 (id=102): 补菜单编码占位 + 排序提前到分析页之前
UPDATE "iam_perm_menu" SET
    menu_code           = 'dashboard:workspace',
    sort_no             = 1,
    last_modified_time  = now()
WHERE id = 102;

-- 11. 分析页 (id=101): 补菜单编码占位 + 排序后移到工作台之后
UPDATE "iam_perm_menu" SET
    menu_code           = 'dashboard:analytics',
    sort_no             = 2,
    last_modified_time  = now()
WHERE id = 101;
