/*
 Navicat Premium Data Transfer

 Source Server         : 229本地服务
 Source Server Type    : PostgreSQL
 Source Server Version : 160014 (160014)
 Source Host           : 192.168.1.229:5432
 Source Catalog        : daxpay-dev
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 160014 (160014)
 File Encoding         : 65001

 Date: 09/07/2026 10:45:29
*/


-- ----------------------------
-- Table structure for iam_perm_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_perm_menu";
CREATE TABLE "public"."iam_perm_menu" (
  "id" int8 NOT NULL,
  "pid" int8,
  "menu_code" varchar(100) COLLATE "pg_catalog"."default",
  "client_code" varchar(100) COLLATE "pg_catalog"."default",
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "title_cn" varchar(200) COLLATE "pg_catalog"."default",
  "title_en" varchar(200) COLLATE "pg_catalog"."default",
  "i18n_key" varchar(200) COLLATE "pg_catalog"."default",
  "icon" varchar(200) COLLATE "pg_catalog"."default",
  "hidden" bool DEFAULT false,
  "hide_children_menu" bool DEFAULT false,
  "component" varchar(500) COLLATE "pg_catalog"."default",
  "path" varchar(500) COLLATE "pg_catalog"."default",
  "redirect" varchar(500) COLLATE "pg_catalog"."default",
  "sort_no" float8,
  "root" bool DEFAULT false,
  "keep_alive" bool DEFAULT false,
  "affix_tab" bool DEFAULT false,
  "creator" int8,
  "last_modifier" int8,
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "menu_type" varchar(20) COLLATE "pg_catalog"."default",
  "active_icon" varchar(100) COLLATE "pg_catalog"."default",
  "badge" varchar(50) COLLATE "pg_catalog"."default",
  "badge_type" varchar(20) COLLATE "pg_catalog"."default",
  "badge_variants" varchar(50) COLLATE "pg_catalog"."default",
  "iframe_src" varchar(500) COLLATE "pg_catalog"."default",
  "link" varchar(500) COLLATE "pg_catalog"."default",
  "create_time" timestamptz(6),
  "last_modified_time" timestamptz(6)
)
;
COMMENT ON COLUMN "public"."iam_perm_menu"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_perm_menu"."pid" IS '父菜单ID,0表示根菜单';
COMMENT ON COLUMN "public"."iam_perm_menu"."menu_code" IS '菜单编码';
COMMENT ON COLUMN "public"."iam_perm_menu"."client_code" IS '关联终端code';
COMMENT ON COLUMN "public"."iam_perm_menu"."name" IS '路由名称，建议唯一';
COMMENT ON COLUMN "public"."iam_perm_menu"."title_cn" IS '菜单标题-中文';
COMMENT ON COLUMN "public"."iam_perm_menu"."title_en" IS '菜单标题-英文';
COMMENT ON COLUMN "public"."iam_perm_menu"."i18n_key" IS '国际化key';
COMMENT ON COLUMN "public"."iam_perm_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "public"."iam_perm_menu"."hidden" IS '是否隐藏';
COMMENT ON COLUMN "public"."iam_perm_menu"."hide_children_menu" IS '是否隐藏子菜单';
COMMENT ON COLUMN "public"."iam_perm_menu"."component" IS '组件';
COMMENT ON COLUMN "public"."iam_perm_menu"."path" IS '访问路径';
COMMENT ON COLUMN "public"."iam_perm_menu"."redirect" IS '菜单跳转地址(重定向)';
COMMENT ON COLUMN "public"."iam_perm_menu"."sort_no" IS '菜单排序';
COMMENT ON COLUMN "public"."iam_perm_menu"."root" IS '是否是一级菜单';
COMMENT ON COLUMN "public"."iam_perm_menu"."keep_alive" IS '是否开启页面缓存';
COMMENT ON COLUMN "public"."iam_perm_menu"."affix_tab" IS '是否固定标签页';
COMMENT ON COLUMN "public"."iam_perm_menu"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_perm_menu"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_perm_menu"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_perm_menu"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_perm_menu"."menu_type" IS '菜单类型: catalog-目录, menu-菜单, embedded-内嵌, link-外链';
COMMENT ON COLUMN "public"."iam_perm_menu"."active_icon" IS '激活状态图标';
COMMENT ON COLUMN "public"."iam_perm_menu"."badge" IS '徽章显示文本';
COMMENT ON COLUMN "public"."iam_perm_menu"."badge_type" IS '徽章类型: dot-圆点, normal-文本';
COMMENT ON COLUMN "public"."iam_perm_menu"."badge_variants" IS '徽章样式变体';
COMMENT ON COLUMN "public"."iam_perm_menu"."iframe_src" IS '内嵌页面URL地址';
COMMENT ON COLUMN "public"."iam_perm_menu"."link" IS '外部链接URL地址';
COMMENT ON TABLE "public"."iam_perm_menu" IS '菜单权限配置';

-- ----------------------------
-- Records of iam_perm_menu
-- ----------------------------
INSERT INTO "public"."iam_perm_menu" VALUES (202, 2, NULL, 'admin', 'FileUploadDemo', '文件上传演示', 'File Upload Demo', 'menu.demos.fileUpload', 'lucide:upload', 'f', 'f', '/demos/file-upload/FileUploadDemo', '/demos/file-upload', NULL, 2, 'f', 't', 'f', 0, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-09 16:00:00+00', '2026-04-09 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (307, 3, 'system:monitor', 'admin', 'SystemMonitor', '系统监控', 'System Monitor', 'menu.system.monitor', 'lucide:monitor', 'f', 'f', NULL, '/system/monitor', NULL, 50, 'f', 't', 'f', 1, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-10 16:00:00+00', '2026-04-12 12:53:45.790453+00');
INSERT INTO "public"."iam_perm_menu" VALUES (305, 3, 'iam:perm', 'admin', 'SystemPerm', '权限管理', 'Permission Management', 'menu.system.perm', 'lucide:shield', 'f', 'f', NULL, '/system/perm', NULL, 2, 'f', 't', 'f', 1, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-08 16:00:00+00', '2026-04-09 15:10:44.651238+00');
INSERT INTO "public"."iam_perm_menu" VALUES (302, 3, NULL, 'admin', 'SystemLog', '日志管理', 'Log Management', 'menu.system.log', 'lucide:file-text', 'f', 'f', NULL, '/system/log', NULL, 99, 'f', 't', 'f', 0, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-04-05 08:56:11.97756+00');
INSERT INTO "public"."iam_perm_menu" VALUES (304, 3, 'system:config', 'admin', 'SystemConfig', '系统配置', 'System Config', 'menu.system.config', 'lucide:settings-2', 'f', 'f', NULL, '/system/config', NULL, 10, 'f', 't', 'f', 0, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-04 16:00:00+00', '2026-04-09 15:11:00.840153+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30701, 307, 'iam:online:user', 'admin', 'OnlineUser', '在线用户', 'Online User', 'menu.system.monitor.online', 'lucide:users', 'f', 'f', '/system/monitor/online/OnlineUserList', '/system/monitor/online', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-11 16:00:00+00', '2026-04-11 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (401, 4, 'payment:platform', 'admin', 'PaymentPlatform', '支付主数据', 'Payment Master Data', 'menu.payment.platform', 'lucide:building', 'f', 'f', NULL, '/payment/platform', NULL, 1, 'f', 't', 'f', 0, 1, 3, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-05 16:00:00+00', '2026-05-28 06:25:42.880461+00');
INSERT INTO "public"."iam_perm_menu" VALUES (203, 2, 'demos:region', 'admin', 'RegionCascaderDemo', '行政区划选择器', 'Region Cascader', 'menu.demos.region', 'lucide:map-pin', 'f', 'f', '/demos/region/RegionCascaderDemo', '/demos/region', NULL, 3, 'f', 't', 'f', 0, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-24 16:00:00+00', '2026-04-24 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (6, NULL, 'trade', 'admin', 'TransactionManagement', '交易管理', 'Transaction Management', 'menu.trade', 'lucide:arrow-left-right', 'f', 'f', NULL, '/trade', '/trade/index', 3.5, 'f', 't', 'f', 1, 1, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 16:00:00+00', '2026-05-24 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (3, NULL, NULL, 'admin', 'System', '系统管理', 'System', 'menu.system', 'lucide:sliders-horizontal', 'f', 'f', NULL, '/system', NULL, 0, 'f', 't', 'f', 0, 1, 2, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.22348+00');
INSERT INTO "public"."iam_perm_menu" VALUES (301, 3, NULL, 'admin', 'SystemBasic', '基础数据', 'Basic Data', 'menu.system.basic', 'lucide:boxes', 'f', 'f', NULL, '/system/basic', NULL, 1, 'f', 't', 'f', 0, NULL, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.236321+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30101, 301, 'system:dict', 'admin', 'SystemDict', '字典管理', 'Dictionary', 'menu.system.basic.dict', 'lucide:book-open', 'f', 'f', '/system/basic/dict/DictList', '/system/basic/dict', NULL, 1, 'f', 't', 'f', 0, 1, 2, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.243488+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30401, 304, 'system:security_config', 'admin', 'SecurityConfig', '安全配置', 'Security Config', 'menu.system.config.security', 'lucide:shield-check', 'f', 'f', '/system/config/security/SecurityConfig', '/system/config/security', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-05 02:00:00+00', '2026-06-27 13:01:24.303469+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30402, 304, 'system:platform_config', 'admin', 'PlatformConfig', '平台配置', 'Platform Config', 'menu.system.config.platform', 'lucide:settings', 'f', 'f', '/system/config/platform/PlatformConfig', '/system/config/platform', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-07 16:00:00+00', '2026-06-27 13:01:24.311831+00');
INSERT INTO "public"."iam_perm_menu" VALUES (2, NULL, NULL, 'admin', 'Demos', '演示', 'Demos', 'menu.demos', 'lucide:blocks', 'f', 'f', NULL, '/demos', NULL, 1000, 'f', 't', 'f', 0, NULL, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.27727+00');
INSERT INTO "public"."iam_perm_menu" VALUES (1, NULL, NULL, 'admin', 'Dashboard', '仪表板', 'Dashboard', 'menu.dashboard', 'lucide:layout-dashboard', 'f', 'f', NULL, '/dashboard', '/workspace', -1, 'f', 'f', 'f', 0, NULL, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-27 13:46:52.151771+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30202, 302, 'system:log:operate', 'admin', 'SystemOperateLog', '操作日志', 'Operate Log', 'menu.system.log.operate', 'lucide:activity', 'f', 'f', '/system/log/operate/OperateLogList', '/system/log/operate', NULL, 2, 'f', 't', 'f', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-03-30 15:24:57.076166+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30601, 307, 'system:file:platform', 'admin', 'StorageFile', '存储文件', 'Storage File', 'menu.system.monitor.file', 'lucide:files', 'f', 'f', '/system/monitor/file/PlatformFileList', '/system/monitor/file', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-09 16:00:00+00', '2026-06-25 02:43:43.71511+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4, NULL, 'payment', 'admin', 'PaymentSystem', '平台管理', 'Platform Management', 'menu.platform', 'lucide:credit-card', 'f', 'f', NULL, '/payment', NULL, 3, 'f', 't', 'f', 0, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-05 16:00:00+00', '2026-06-13 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30201, 302, 'system:log:login', 'admin', 'SystemLoginLog', '登录日志', 'Login Log', 'menu.system.log.login', 'lucide:log-in', 'f', 'f', '/system/log/login/LoginLogList', '/system/log/login', NULL, 1, 'f', 't', 'f', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-03-30 15:25:09.855555+00');
INSERT INTO "public"."iam_perm_menu" VALUES (102, 1, 'dashboard:workspace', 'admin', 'Workspace', '工作台', 'Workspace', 'menu.dashboard.workspace', 'lucide:panels-top-left', 'f', 'f', '/dashboard/workspace/index', '/workspace', NULL, 1, 'f', 'f', 't', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-28 02:50:37.055128+00');
INSERT INTO "public"."iam_perm_menu" VALUES (101, 1, 'dashboard:analytics', 'admin', 'Analytics', '分析页', 'Analytics', 'menu.dashboard.analytics', 'lucide:area-chart', 'f', 'f', '/dashboard/analytics/index', '/analytics', NULL, 2, 'f', 'f', 'f', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-28 02:50:43.072618+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30103, 305, 'iam:role', 'admin', 'SystemRole', '角色管理', 'Role Management', 'menu.system.perm.role', 'lucide:shield-user', 'f', 'f', '/iam/perm/role/RoleList', '/iam/perm/role', NULL, 3, 'f', 't', 'f', 0, 1, 2, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.246504+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30501, 305, 'iam:user:manager', 'admin', 'UserList', '用户管理', 'User Management', 'menu.system.perm.user', 'lucide:users-round', 'f', 'f', 'views/iam/user/UserList', '/iam/user', NULL, 10, 'f', 't', 'f', 1, 1, 4, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-31 00:23:04.37507+00', '2026-06-25 02:00:30.279649+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30102, 301, 'iam:perm:menu', 'admin', 'SystemMenu', '菜单管理', 'Menu Management', 'menu.system.perm.menu', 'lucide:panel-top', 'f', 'f', '/iam/perm/menu/MenuList', '/system/basic/menu', NULL, 0, 'f', 't', 'f', 0, 1, 3, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-27 10:29:51.371435+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40401, 404, 'merchant:info', 'admin', 'MerchantInfo', '商户列表', 'Merchant List', 'menu.payment.merchant.list', 'lucide:shopping-bag', 'f', 'f', '/payment/merchant/info/MerchantList', '/payment/merchant/info', NULL, 1, 'f', 't', 'f', 1, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-04-13 16:00:00+00', '2026-06-25 02:00:30.290399+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40104, 401, 'payment:platform:capability', 'admin', 'PayCapabilityList', '支付能力', 'Payment Capability', 'menu.payment.platform.capability', 'lucide:zap', 'f', 'f', '/payment/masterdata/capability/PayCapabilityList', '/payment/platform/pay-capability', NULL, 3, 'f', 't', 'f', 1, 1, 4, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-26 16:00:00+00', '2026-05-28 06:43:27.505831+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040114, 40401, 'channel:alipay:app', 'admin', 'AlipayMchAppManage', '支付宝应用', 'Alipay App', 'menu.payment.merchant.channelMerchant.alipayApp', NULL, 't', 'f', '/payment/channel/alipay/manage/mch/app/AlipayMchAppManage', '/payment/merchant/channel-merchant/alipay-app-manage', NULL, 12, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-11 22:28:11.274785+00', '2026-06-11 22:28:11.274785+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40102, 401, 'payment:platform:provider', 'admin', 'PayProviderList', '支付方式', 'Payment Method', 'menu.payment.platform.provider', 'lucide:list-tree', 'f', 'f', '/payment/masterdata/provider/PayMethodList', '/payment/platform/pay-method', NULL, 1, 'f', 't', 'f', 1, 1, 5, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-27 20:18:01.383008+00', '2026-05-30 08:21:19.464024+00');
INSERT INTO "public"."iam_perm_menu" VALUES (404, NULL, 'merchant', 'admin', 'PaymentMerchant', '商户管理', 'Merchant Management', 'menu.merchant', 'lucide:store', 'f', 'f', NULL, '/payment/merchant', NULL, 4, 't', 't', 'f', 0, NULL, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-05 16:00:00+00', '2026-06-13 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40103, 401, 'payment:platform:pay_channel', 'admin', 'PayChannelList', '支付通道', 'Payment Channel', 'menu.payment.platform.channel', 'lucide:radio-tower', 'f', 'f', '/payment/masterdata/channel/PayChannelList', '/payment/platform/pay-channel', NULL, 0, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-28 16:00:00+00', '2026-06-27 13:01:24.315365+00');
INSERT INTO "public"."iam_perm_menu" VALUES (601, 6, 'payment:order', 'admin', 'NormalOrderList', '支付订单', 'Payment Orders', 'menu.trade.normalPay', 'lucide:receipt', 'f', 'f', '/payment/order/NormalOrderList', '/trade/normal-pay', NULL, 1, 'f', 'f', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 16:00:00+00', '2026-07-01 15:04:14.721955+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40101, 401, 'payment:platform:product', 'admin', 'ProductList', '支付产品', 'Payment Product', 'menu.payment.platform.product', 'lucide:package', 'f', 'f', '/payment/masterdata/product/PayProductList', '/payment/platform/product', NULL, 2, 'f', 't', 'f', 1, 1, 3, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-23 16:00:00+00', '2026-05-28 06:25:42.892396+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040101, 40401, 'merchant:workbench', 'admin', 'MerchantManage', '商户工作台', 'Merchant Workbench', 'menu.payment.merchant.workbench', '', 't', 'f', '/payment/merchant/manage/workbench/MerchantManage', '/payment/merchant/manage', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-13 16:00:00+00', '2026-05-02 06:57:49.052168+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040102, 40401, 'merchant:credential', 'admin', 'MerchantCredentialConfig', '对接配置', 'Credential Config', 'menu.payment.merchant.credential', NULL, 't', 'f', '/payment/merchant/manage/credential/MerchantCredentialConfig', '/payment/merchant/manage/credential', NULL, 3, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-01 16:00:00+00', '2026-05-01 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040111, 40401, 'merchant:app:route', 'admin', 'PayRouteConfig', '通道路由', 'Channel Routing', 'menu.payment.merchant.payRoute', NULL, 't', 'f', '/payment/merchant/route/PayRouteConfig', '/payment/merchant/route', NULL, 2, 'f', 't', 'f', 1, 1, 1, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 22:23:46.483985+00', '2026-05-26 20:03:06.445803+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040112, 40401, 'channel:merchant:detail', 'admin', 'ChannelMerchantDetailDispatch', '通道商户管理', 'Channel Merchant Management', 'menu.payment.merchant.channelMerchant.detail', NULL, 't', 'f', '/payment/merchant/channel-merchant/detail/ChannelMerchantDetailDispatch', '/payment/merchant/channel-merchant/detail', NULL, 10, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-07 19:48:34.936565+00', '2026-06-07 19:48:34.936565+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040113, 40401, 'channel:wechat:app', 'admin', 'WechatMchAppManage', '微信应用', 'WeChat App', 'menu.payment.merchant.channelMerchant.wechatApp', NULL, 't', 'f', '/payment/channel/wechat/manage/mch/app/WechatMchAppManage', '/payment/merchant/channel-merchant/wechat-app-manage', NULL, 11, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-07 23:57:13.620205+00', '2026-06-07 23:57:13.620205+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040108, 40401, 'merchant:user', 'admin', 'MerchantUser', '商户用户', 'Merchant User', 'menu.payment.merchant.user', '', 't', 'f', '/payment/merchant/user/MerchantUserList', '/payment/merchant/user', NULL, 8, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-04 16:00:00+00', '2026-05-04 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040110, 40401, 'merchant:app', 'admin', 'MchAppInfoList', '应用管理', 'App Management', 'menu.payment.merchant.app', NULL, 't', 'f', '/payment/merchant/app/MchAppInfoList', '/payment/merchant/app', NULL, 10, 'f', 't', 'f', 1, 1, 1, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 16:00:00+00', '2026-05-24 19:54:00.095239+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040109, 40401, 'channel:merchant:create', 'admin', 'ChannelMerchantCreate', '创建通道商户', 'Create Channel Merchant', 'menu.payment.merchant.channelMerchant.create', NULL, 't', 'f', '/payment/merchant/channel-merchant/ChannelMerchantCreate', '/payment/merchant/channel-merchant/create', NULL, 9, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-05 16:00:00+00', '2026-05-05 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (405, 4, 'payment:config', 'admin', 'PaymentConfig', '支付配置', 'Payment Config', 'menu.payment.config', 'lucide:settings-2', 'f', 'f', NULL, '/payment/config', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-14 00:00:00+00', '2026-06-14 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40503, 40105, 'payment:wechat:isv', 'admin', 'WechatIsvAppManage', '微信服务商应用', 'WeChat ISV Application', 'menu.payment.config.wechatApp', NULL, 't', 'f', '/payment/channel/wechat/manage/app/WechatIsvAppManage', '/payment/config/product/wechat-app-manage', NULL, 4, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-14 00:00:00+00', '2026-06-14 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (8, NULL, 'develop', 'admin', 'Develop', '开发调试', 'Develop', 'menu.develop', 'lucide:wrench', 'f', 'f', NULL, '/develop', NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-23 16:00:00+00', '2026-06-23 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (901, 9, 'device:qrcode', 'admin', 'DeviceQrCode', '码牌管理', 'QR Code Management', 'menu.device.qrcode', 'lucide:qr-code', 'f', 'f', '/payment/device/qrcode/DeviceQrCode', '/device/qrcode', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-25 01:21:34.634639+00', '2026-06-25 12:51:25.24304+00');
INSERT INTO "public"."iam_perm_menu" VALUES (204, 2, 'demos:artemis', 'admin', 'ArtemisDemo', '消息队列演示', 'Message Queue Demo', 'menu.demos.artemis', 'lucide:send', 'f', 'f', '/demos/artemis/ArtemisDemo', '/demos/artemis', NULL, 4, 'f', 't', 'f', 0, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-18 00:00:00+00', '2026-06-18 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (900001, 304, 'iam:social:login-config', 'admin', 'ThirdPlatform', '三方平台管理', 'Third-party Platform Management', 'menu.system.config.thirdPlatform', 'lucide:share-2', 'f', 'f', 'views/system/config/third-platform/ThirdPlatform', '/system/config/third-platform', NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (30403, 304, 'system:protocol', 'admin', 'UserProtocol', '用户协议', 'User Protocol', 'menu.system.config.protocol', 'lucide:file-text', 'f', 'f', '/system/protocol/UserProtocolList', '/system/config/protocol', NULL, 10, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-24 10:32:13.183371+00', '2026-06-24 10:32:13.183371+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040106, 40401, 'channel:merchant', 'admin', 'ChannelMerchant', '通道商户', 'Channel Merchant', 'menu.payment.merchant.channelMerchant', NULL, 't', 'f', '/payment/merchant/channel-merchant/ChannelMerchantList', '/payment/merchant/channel-merchant', NULL, 6, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-03 16:00:00+00', '2026-05-03 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40502, 40105, 'payment:alipay:isv', 'admin', 'AlipayIsvAppManage', '支付宝服务商应用', 'Alipay ISV Application', 'menu.payment.config.alipayApp', NULL, 't', 'f', '/payment/channel/alipay/manage/app/AlipayIsvAppManage', '/payment/config/product/app-manage', NULL, 3, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-14 00:00:00+00', '2026-06-14 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (801, 8, 'develop:trade', 'admin', 'DevelopTrade', '支付调试', 'Payment Develop', 'menu.develop.trade', 'lucide:credit-card', 'f', 'f', '/payment/develop/trade/DevelopTrade', '/develop/trade', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-23 16:00:00+00', '2026-06-23 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (802, 8, 'develop:sign', 'admin', 'DevelopSign', '签名调试', 'Sign Develop', 'menu.develop.sign', 'lucide:file-signature', 'f', 'f', '/payment/develop/sign/DevelopSign', '/develop/sign', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-23 16:00:00+00', '2026-06-23 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (803, 8, 'develop:auth', 'admin', 'ChannelAuth', '认证调试', 'Auth Develop', 'menu.develop.auth', 'lucide:key-round', 'f', 'f', '/payment/develop/auth/ChannelAuth', '/develop/auth', NULL, 3, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-09 16:00:00+00', '2026-07-09 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040117, 40401, 'merchant:store', 'admin', 'MchStoreInfoList', '门店管理', 'Store Management', 'menu.payment.merchant.store', NULL, 't', 'f', '/payment/merchant/store/MchStoreInfoList', '/payment/merchant/store', NULL, 15, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-24 16:00:00+00', '2026-06-24 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040103, 40401, 'merchant:mchInfo', 'admin', 'MchInfoManage', '商户资料', 'Merchant Profile', 'menu.payment.merchant.profile', NULL, 't', 'f', '/payment/merchant/manage/info/MchInfoManage', '/payment/merchant/manage/info', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (4040119, 40401, 'channel:wechat:app', 'admin', 'WechatIsvMchAppManage', '微信服务商子商户应用', 'WeChat ISV Sub-merchant App', 'menu.payment.merchant.channelMerchant.wechatIsvApp', NULL, 't', 'f', '/payment/channel/wechat/manage/mch/isv-app/WechatIsvMchAppManage', '/payment/merchant/channel-merchant/wechat-isv-mch-app-manage', NULL, 14, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 01:49:32.557374+00', '2026-07-05 01:49:32.557374+00');
INSERT INTO "public"."iam_perm_menu" VALUES (602, 6, 'payment:trade', 'admin', 'PayTradeList', '资金交易', 'Fund Trades', 'menu.trade.payTrade', 'lucide:arrow-left-right', 'f', 'f', '/payment/order/PayTradeList', '/trade/pay-trade', NULL, 2, 'f', 't', 'f', NULL, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-01 15:09:04.769906+00', '2026-07-01 15:09:04.769906+00');
INSERT INTO "public"."iam_perm_menu" VALUES (9, NULL, 'device', 'admin', 'DeviceManagement', '设备管理', 'Device Management', 'menu.device', 'lucide:qr-code', 'f', 'f', NULL, '/device', NULL, 4.2, 'f', 't', 'f', 1, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-25 01:21:34.631155+00', '2026-06-25 12:51:25.231569+00');
INSERT INTO "public"."iam_perm_menu" VALUES (904, 9, 'device:assistant', 'admin', 'DeviceAssistant', '辅助支付终端', 'Payment Assistant Terminal', 'menu.device.assistant', 'lucide:monitor-smartphone', 'f', 'f', '/_core/fallback/coming-soon', '/device/assistant', NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-25 12:51:25.252086+00', '2026-06-25 12:51:25.252086+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40105, 405, 'payment:config:product_config', 'admin', 'ProductConfig', '支付产品管理', 'Payment Product Management', 'menu.payment.config.productConfig', 'lucide:layout-grid', 'f', 'f', '/payment/config/product/ProductConfig', '/payment/config/product', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-13 00:00:00+00', '2026-06-27 13:01:24.317032+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040116, 401, 'payment:platform:provider_manage', 'admin', 'PayProviderManage', '支付渠道', 'Payment Provider', 'menu.payment.platform.provider.manage', 'lucide:wallet', 'f', 'f', '/payment/masterdata/provider/PayProviderList', '/payment/platform/pay-provider', NULL, 0.5, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-17 10:00:31.640482+00', '2026-06-27 13:01:24.318649+00');
INSERT INTO "public"."iam_perm_menu" VALUES (309, 308, 'system:notify:notice', 'admin', 'SystemNotify', '通知公告', 'Notification', 'menu.system.notify.notice', 'lucide:megaphone', 'f', 'f', '/system/notify/notice/NoticeList', '/system/notify/notice', NULL, 20, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, '2026-06-24 16:00:00+00', '2026-06-24 16:00:00+00', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (310, 308, 'system:notify:wechat-config', 'admin', 'WechatNotify', '微信消息通知', 'Wechat Message Notification', 'menu.system.notify.wechatConfig', 'lucide:message-circle', 'f', 'f', '/system/notify/wechat/index', '/system/notify/wechat', NULL, 30, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-09 00:00:00+00', '2026-07-09 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (308, 3, 'system:notify', 'admin', 'SystemNotice', '通知中心', 'Notification Center', 'menu.system.notify', 'lucide:bell', 'f', 'f', NULL, '/system/notify', '/system/notify/notice', 20, 'f', 't', 'f', 1, 1, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-01 07:16:30.927645+00', '2026-07-01 07:16:30.927645+00');
INSERT INTO "public"."iam_perm_menu" VALUES (603, 6, 'payment:refund', 'admin', 'RefundOrderList', '退款订单', 'Refund Orders', 'menu.trade.refundOrder', 'lucide:rotate-ccw', 'f', 'f', '/payment/order/RefundOrderList', '/trade/refund-order', NULL, 3, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-03 16:00:00+00', '2026-07-03 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040115, 40401, 'channel:douyin:app', 'admin', 'DouyinMchAppManage', '抖音应用', 'Douyin App', 'menu.payment.merchant.channelMerchant.douyinApp', NULL, 't', 'f', '/payment/channel/douyin/manage/app/DouyinMchAppManage', '/payment/merchant/channel-merchant/douyin-app-manage', NULL, 13, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-15 16:00:00+00', '2026-06-15 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40501, 40105, 'payment:config:product_detail', 'admin', 'ProductDetailDispatch', '产品配置详情', 'Product Config Detail', 'menu.payment.config.detail', NULL, 't', 'f', '/payment/config/product/detail/ProductDetailDispatch', '/payment/product-detail', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-14 00:00:00+00', '2026-06-27 13:01:24.321883+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40504, 304, 'system:config:mobile_app', 'admin', 'MobileAppConfig', '移动端应用管理', 'Mobile App Management', 'menu.system.config.mobileApp', 'lucide:smartphone', 'f', 'f', '/system/config/mobileApp/MobileAppConfig', '/system/config/mobile-app', NULL, 6, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-05 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40505, 40504, 'system:config:mobile_app_detail', 'admin', 'MobileAppDetail', '移动端应用详情', 'Mobile App Detail', 'menu.system.config.mobileAppDetail', NULL, 't', 'f', '/system/config/mobileApp/detail/MobileAppDetail', '/system/config/mobile-app/detail/:appType', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-05 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40506, 40105, 'payment:lakala:isv', 'admin', 'LakalaManage', '拉卡拉服务商配置', 'Lakala ISV Configuration', 'menu.payment.lakala.config', NULL, 't', 'f', '/payment/channel/lakala/manage/LakalaManage', '/payment/config/product/lakala-manage', NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-05 00:00:00+00');

-- ----------------------------
-- Primary Key structure for table iam_perm_menu
-- ----------------------------
ALTER TABLE "public"."iam_perm_menu" ADD CONSTRAINT "iam_perm_menu_pkey" PRIMARY KEY ("id");
