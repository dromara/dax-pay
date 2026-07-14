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

 Date: 13/07/2026 20:29:41
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
COMMENT ON COLUMN "public"."iam_perm_menu"."menu_type" IS '菜单类型: catalog-目录, menu-菜单, subpage-子页面, subpage_group-子页面分组, embedded-内嵌, link-外链';
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
INSERT INTO "public"."iam_perm_menu" VALUES (202, 2, NULL, 'admin', 'FileUploadDemo', 'menu.demos.fileUpload', 'lucide:upload', 'f', 'f', '/demos/file-upload/FileUploadDemo', '/demos/file-upload', NULL, 2, 'f', 't', 'f', 0, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-09 16:00:00+00', '2026-04-09 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (307, 3, 'system:monitor', 'admin', 'SystemMonitor', 'menu.system.monitor', 'lucide:monitor', 'f', 'f', NULL, '/system/monitor', NULL, 50, 'f', 't', 'f', 1, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-10 16:00:00+00', '2026-04-12 12:53:45.790453+00');
INSERT INTO "public"."iam_perm_menu" VALUES (305, 3, 'iam:perm', 'admin', 'SystemPerm', 'menu.system.perm', 'lucide:shield', 'f', 'f', NULL, '/system/perm', NULL, 2, 'f', 't', 'f', 1, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-08 16:00:00+00', '2026-04-09 15:10:44.651238+00');
INSERT INTO "public"."iam_perm_menu" VALUES (302, 3, NULL, 'admin', 'SystemLog', 'menu.system.log', 'lucide:file-text', 'f', 'f', NULL, '/system/log', NULL, 99, 'f', 't', 'f', 0, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-04-05 08:56:11.97756+00');
INSERT INTO "public"."iam_perm_menu" VALUES (304, 3, 'system:config', 'admin', 'SystemConfig', 'menu.system.config', 'lucide:settings-2', 'f', 'f', NULL, '/system/config', NULL, 10, 'f', 't', 'f', 0, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-04 16:00:00+00', '2026-04-09 15:11:00.840153+00');
INSERT INTO "public"."iam_perm_menu" VALUES (401, 4, 'payment:platform', 'admin', 'PaymentPlatform', 'menu.payment.platform', 'lucide:building', 'f', 'f', NULL, '/payment/platform', NULL, 1, 'f', 't', 'f', 0, 1, 3, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-05 16:00:00+00', '2026-05-28 06:25:42.880461+00');
INSERT INTO "public"."iam_perm_menu" VALUES (203, 2, 'demos:region', 'admin', 'RegionCascaderDemo', 'menu.demos.region', 'lucide:map-pin', 'f', 'f', '/demos/region/RegionCascaderDemo', '/demos/region', NULL, 3, 'f', 't', 'f', 0, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-24 16:00:00+00', '2026-04-24 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (6, NULL, 'trade', 'admin', 'TransactionManagement', 'menu.trade', 'lucide:arrow-left-right', 'f', 'f', NULL, '/trade', '/trade/index', 4, 'f', 't', 'f', 1, 1, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 16:00:00+00', '2026-05-24 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (3, NULL, NULL, 'admin', 'System', 'menu.system', 'lucide:sliders-horizontal', 'f', 'f', NULL, '/system', NULL, 0, 'f', 't', 'f', 0, 1, 2, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.22348+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30102, 301, 'iam:menu', 'admin', 'SystemMenu', 'menu.system.perm.menu', 'lucide:panel-top', 'f', 'f', '/iam/perm/menu/MenuList', '/system/basic/menu', NULL, 0, 'f', 't', 'f', 0, 1, 3, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-27 10:29:51.371435+00');
INSERT INTO "public"."iam_perm_menu" VALUES (301, 3, NULL, 'admin', 'SystemBasic', 'menu.system.basic', 'lucide:boxes', 'f', 'f', NULL, '/system/basic', NULL, 1, 'f', 't', 'f', 0, NULL, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.236321+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30101, 301, 'system:dict', 'admin', 'SystemDict', 'menu.system.basic.dict', 'lucide:book-open', 'f', 'f', '/system/basic/dict/DictList', '/system/basic/dict', NULL, 1, 'f', 't', 'f', 0, 1, 2, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.243488+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30601, 307, 'system:file', 'admin', 'StorageFile', 'menu.system.monitor.file', 'lucide:files', 'f', 'f', '/system/monitor/file/PlatformFileList', '/system/monitor/file', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-09 16:00:00+00', '2026-06-25 02:43:43.71511+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30501, 305, 'iam:user', 'admin', 'UserList', 'menu.system.perm.user', 'lucide:users-round', 'f', 'f', '/iam/user/UserList', '/iam/user', NULL, 10, 'f', 't', 'f', 1, 1, 4, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-31 00:23:04.37507+00', '2026-07-13 02:15:41.494775+00');
INSERT INTO "public"."iam_perm_menu" VALUES (2, NULL, NULL, 'admin', 'Demos', 'menu.demos', 'lucide:blocks', 'f', 'f', NULL, '/demos', NULL, 1000, 'f', 't', 'f', 0, NULL, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.27727+00');
INSERT INTO "public"."iam_perm_menu" VALUES (1, NULL, NULL, 'admin', 'Dashboard', 'menu.dashboard', 'lucide:layout-dashboard', 'f', 'f', NULL, '/dashboard', '/workspace', -1, 'f', 'f', 'f', 0, NULL, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-06-27 13:46:52.151771+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30202, 302, 'system:log:operate', 'admin', 'SystemOperateLog', 'menu.system.log.operate', 'lucide:activity', 'f', 'f', '/system/log/operate/OperateLogList', '/system/log/operate', NULL, 2, 'f', 't', 'f', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-03-30 15:24:57.076166+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30401, 304, 'system:security-config', 'admin', 'SecurityConfig', 'menu.system.config.security', 'lucide:shield-check', 'f', 'f', '/system/config/security/SecurityConfig', '/system/config/security', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-05 02:00:00+00', '2026-06-27 13:01:24.303469+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4, NULL, 'payment', 'admin', 'PaymentSystem', 'menu.platform', 'lucide:credit-card', 'f', 'f', NULL, '/payment', NULL, 3, 'f', 't', 'f', 0, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-05 16:00:00+00', '2026-06-13 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30201, 302, 'system:log:login', 'admin', 'SystemLoginLog', 'menu.system.log.login', 'lucide:log-in', 'f', 'f', '/system/log/login/LoginLogList', '/system/log/login', NULL, 1, 'f', 't', 'f', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-20 03:11:13.134079+00', '2026-03-30 15:25:09.855555+00');
INSERT INTO "public"."iam_perm_menu" VALUES (102, 1, 'dashboard:workspace', 'admin', 'Workspace', 'menu.dashboard.workspace', 'lucide:panels-top-left', 'f', 'f', '/dashboard/workspace/index', '/workspace', NULL, 1, 'f', 'f', 't', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-28 02:50:37.055128+00');
INSERT INTO "public"."iam_perm_menu" VALUES (101, 1, 'dashboard:analytics', 'admin', 'Analytics', 'menu.dashboard.analytics', 'lucide:area-chart', 'f', 'f', '/dashboard/analytics/index', '/analytics', NULL, 2, 'f', 'f', 'f', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-28 02:50:43.072618+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30402, 304, 'system:platform-config', 'admin', 'PlatformConfig', 'menu.system.config.platform', 'lucide:settings', 'f', 'f', '/system/config/platform/PlatformConfig', '/system/config/platform', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-07 16:00:00+00', '2026-06-27 13:01:24.311831+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30103, 305, 'iam:role', 'admin', 'SystemRole', 'menu.system.perm.role', 'lucide:shield-user', 'f', 'f', '/iam/perm/role/RoleList', '/iam/perm/role', NULL, 3, 'f', 't', 'f', 0, 1, 2, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-03-20 03:11:13.134079+00', '2026-06-25 02:00:30.246504+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30701, 307, 'iam:online', 'admin', 'OnlineUser', 'menu.system.monitor.online', 'lucide:users', 'f', 'f', '/system/monitor/online/OnlineUserList', '/system/monitor/online', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-11 16:00:00+00', '2026-04-11 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40401, 404, 'merchant:info', 'admin', 'MerchantInfo', 'menu.payment.merchant.list', 'lucide:shopping-bag', 'f', 'f', '/payment/merchant/info/MerchantList', '/payment/merchant/info', NULL, 1, 'f', 't', 'f', 1, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-04-13 16:00:00+00', '2026-06-25 02:00:30.290399+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40402, 404, 'channel:merchant', 'admin', 'ChannelMerchantGlobal', 'menu.payment.merchant.channelMerchant.global', 'lucide:repeat', 'f', 'f', '/_core/fallback/coming-soon', '/payment/merchant/channel-merchants', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-13 00:00:00+00', '2026-07-13 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40104, 401, 'payment:platform:capability', 'admin', 'PayCapabilityList', 'menu.payment.platform.capability', 'lucide:zap', 'f', 'f', '/payment/masterdata/capability/PayCapabilityList', '/payment/platform/pay-capability', NULL, 3, 'f', 't', 'f', 1, 1, 4, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-26 16:00:00+00', '2026-05-28 06:43:27.505831+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040102, 4040130, 'merchant:credential', 'admin', 'MerchantCredentialConfig', 'menu.payment.merchant.credential', NULL, 't', 'f', '/payment/merchant/manage/credential/MerchantCredentialConfig', '/payment/merchant/manage/credential', NULL, 3, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-01 16:00:00+00', '2026-07-13 10:13:20.809377+00');
INSERT INTO "public"."iam_perm_menu" VALUES (404, NULL, 'merchant', 'admin', 'PaymentMerchant', 'menu.merchant', 'lucide:store', 'f', 'f', NULL, '/payment/merchant', NULL, 3.5, 't', 't', 'f', 0, NULL, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-05 16:00:00+00', '2026-06-13 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40101, 401, 'payment:platform:product', 'admin', 'ProductList', 'menu.payment.platform.product', 'lucide:package', 'f', 'f', '/payment/masterdata/product/PayProductList', '/payment/platform/product', NULL, 2, 'f', 't', 'f', 1, 1, 3, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-23 16:00:00+00', '2026-05-28 06:25:42.892396+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040101, 4040130, 'merchant:workbench', 'admin', 'MerchantManage', 'menu.payment.merchant.workbench', '', 't', 'f', '/payment/merchant/manage/workbench/MerchantManage', '/payment/merchant/manage', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-13 16:00:00+00', '2026-07-11 03:37:15.424776+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40103, 401, 'payment:platform:pay-channel', 'admin', 'PayChannelList', 'menu.payment.platform.channel', 'lucide:radio-tower', 'f', 'f', '/payment/masterdata/channel/PayChannelList', '/payment/platform/pay-channel', NULL, 0, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-28 16:00:00+00', '2026-06-27 13:01:24.315365+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40102, 401, 'payment:platform:pay-method', 'admin', 'PayProviderList', 'menu.payment.platform.provider', 'lucide:list-tree', 'f', 'f', '/payment/masterdata/provider/PayMethodList', '/payment/platform/pay-method', NULL, 1, 'f', 't', 'f', 1, 1, 5, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-27 20:18:01.383008+00', '2026-05-30 08:21:19.464024+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040108, 4040130, 'merchant:user', 'admin', 'MerchantUser', 'menu.payment.merchant.user', '', 't', 'f', '/payment/merchant/user/MerchantUserList', '/payment/merchant/user', NULL, 8, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-04 16:00:00+00', '2026-07-11 03:37:15.424776+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040110, 4040130, 'merchant:app', 'admin', 'MchAppInfoList', 'menu.payment.merchant.app', NULL, 't', 'f', '/payment/merchant/app/MchAppInfoList', '/payment/merchant/app', NULL, 10, 'f', 't', 'f', 1, 1, 1, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 16:00:00+00', '2026-07-11 03:37:15.424776+00');
INSERT INTO "public"."iam_perm_menu" VALUES (601, 6, 'trade:order', 'admin', 'NormalOrderList', 'menu.trade.normalPay', 'lucide:receipt', 'f', 'f', '/payment/order/NormalOrderList', '/trade/normal-pay', NULL, 1, 'f', 'f', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 16:00:00+00', '2026-07-01 15:04:14.721955+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040114, 4040132, 'channel:app', 'admin', 'AlipayMchAppManage', 'menu.payment.merchant.channelMerchant.alipayApp', NULL, 't', 'f', '/payment/channel/alipay/manage/mch/app/AlipayMchAppManage', '/payment/merchant/channel-merchant/alipay-app-manage', NULL, 12, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-11 22:28:11.274785+00', '2026-07-11 03:37:15.430392+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040112, 4040131, 'channel:merchant:detail', 'admin', 'ChannelMerchantDetailDispatch', 'menu.payment.merchant.channelMerchant.detail', NULL, 't', 'f', '/payment/merchant/channel-merchant/detail/ChannelMerchantDetailDispatch', '/payment/merchant/channel-merchant/detail', NULL, 10, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-07 19:48:34.936565+00', '2026-07-11 03:37:15.428444+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040113, 4040132, 'channel:app', 'admin', 'WechatMchAppManage', 'menu.payment.merchant.channelMerchant.wechatApp', NULL, 't', 'f', '/payment/channel/wechat/manage/mch/app/WechatMchAppManage', '/payment/merchant/channel-merchant/wechat-app-manage', NULL, 11, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-07 23:57:13.620205+00', '2026-07-11 03:37:15.430392+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040111, 4040130, 'merchant:app:route', 'admin', 'PayRouteConfig', 'menu.payment.merchant.payRoute', NULL, 't', 'f', '/payment/merchant/route/PayRouteConfig', '/payment/merchant/route', NULL, 2, 'f', 't', 'f', 1, 1, 1, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-24 22:23:46.483985+00', '2026-07-13 10:13:20.790253+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040109, 4040131, 'channel:merchant:create', 'admin', 'ChannelMerchantCreate', 'menu.payment.merchant.channelMerchant.create', NULL, 't', 'f', '/payment/merchant/channel-merchant/ChannelMerchantCreate', '/payment/merchant/channel-merchant/create', NULL, 9, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-05 16:00:00+00', '2026-07-11 03:37:15.428444+00');
INSERT INTO "public"."iam_perm_menu" VALUES (405, 4, 'payment:config', 'admin', 'PaymentConfig', 'menu.payment.config', 'lucide:settings-2', 't', 'f', NULL, '/payment/config', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-14 00:00:00+00', '2026-06-14 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040117, 4040130, 'merchant:store', 'admin', 'MchStoreInfoList', 'menu.payment.merchant.store', NULL, 't', 'f', '/payment/merchant/store/MchStoreInfoList', '/payment/merchant/store', NULL, 15, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-24 16:00:00+00', '2026-07-11 03:37:15.424776+00');
INSERT INTO "public"."iam_perm_menu" VALUES (603, 6, 'trade:refund', 'admin', 'RefundOrderList', 'menu.trade.refundOrder', 'lucide:rotate-ccw', 'f', 'f', '/payment/order/RefundOrderList', '/trade/refund-order', NULL, 3, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-03 16:00:00+00', '2026-07-03 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (8, NULL, 'develop', 'admin', 'Develop', 'menu.develop', 'lucide:wrench', 'f', 'f', NULL, '/develop', NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-23 16:00:00+00', '2026-06-23 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (9, NULL, 'device', 'admin', 'DeviceManagement', 'menu.device', 'lucide:qr-code', 'f', 'f', NULL, '/device', NULL, 4.2, 'f', 't', 'f', 1, 1, 1, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-25 01:21:34.631155+00', '2026-07-11 02:17:16.019646+00');
INSERT INTO "public"."iam_perm_menu" VALUES (204, 2, 'demos:artemis', 'admin', 'ArtemisDemo', 'menu.demos.artemis', 'lucide:send', 'f', 'f', '/demos/artemis/ArtemisDemo', '/demos/artemis', NULL, 4, 'f', 't', 'f', 0, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-18 00:00:00+00', '2026-06-18 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (900001, 304, 'iam:social', 'admin', 'ThirdPlatform', 'menu.system.config.thirdPlatform', 'lucide:share-2', 'f', 'f', '/system/config/third-platform/ThirdPlatform', '/system/config/third-platform', NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-13 02:15:41.505142+00');
INSERT INTO "public"."iam_perm_menu" VALUES (30403, 304, 'system:protocol', 'admin', 'UserProtocol', 'menu.system.config.protocol', 'lucide:file-text', 'f', 'f', '/system/protocol/UserProtocolList', '/system/config/protocol', NULL, 10, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-24 10:32:13.183371+00', '2026-06-24 10:32:13.183371+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040103, 4040130, 'merchant:profile', 'admin', 'MchInfoManage', 'menu.payment.merchant.profile', NULL, 't', 'f', '/payment/merchant/manage/info/MchInfoManage', '/payment/merchant/manage/info', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:37:15.424776+00');
INSERT INTO "public"."iam_perm_menu" VALUES (801, 8, 'develop:trade', 'admin', 'DevelopTrade', 'menu.develop.trade', 'lucide:credit-card', 'f', 'f', '/payment/develop/trade/DevelopTrade', '/develop/trade', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-23 16:00:00+00', '2026-06-23 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (802, 8, 'develop:sign', 'admin', 'DevelopSign', 'menu.develop.sign', 'lucide:file-signature', 'f', 'f', '/payment/develop/sign/DevelopSign', '/develop/sign', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-23 16:00:00+00', '2026-06-23 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040106, 4040131, 'channel:merchant', 'admin', 'ChannelMerchant', 'menu.payment.merchant.channelMerchant', NULL, 't', 'f', '/payment/merchant/channel-merchant/ChannelMerchantList', '/payment/merchant/channel-merchant', NULL, 6, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-03 16:00:00+00', '2026-07-11 03:37:15.428444+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040115, 4040132, 'channel:app', 'admin', 'DouyinMchAppManage', 'menu.payment.merchant.channelMerchant.douyinApp', NULL, 't', 'f', '/payment/channel/douyin/manage/app/DouyinMchAppManage', '/payment/merchant/channel-merchant/douyin-app-manage', NULL, 13, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-15 16:00:00+00', '2026-07-11 03:37:15.430392+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40105, 4, 'payment:config:product-config', 'admin', 'ProductConfig', 'menu.payment.config.productConfig', 'lucide:layout-grid', 'f', 'f', '/payment/config/product/ProductConfig', '/payment/config/product', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-13 00:00:00+00', '2026-07-13 10:13:20.799778+00');
INSERT INTO "public"."iam_perm_menu" VALUES (904, 9, 'device:assistant', 'admin', 'DeviceAssistant', 'menu.device.assistant', 'lucide:monitor-smartphone', 'f', 'f', '/_core/fallback/coming-soon', '/device/assistant', NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-25 12:51:25.252086+00', '2026-06-25 12:51:25.252086+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40503, 40508, 'payment:isv', 'admin', 'WechatIsvAppManage', 'menu.payment.config.wechatApp', NULL, 't', 'f', '/payment/channel/wechat/manage/app/WechatIsvAppManage', '/payment/config/product/wechat-app-manage', NULL, 4, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-14 00:00:00+00', '2026-07-11 03:37:15.43231+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040116, 401, 'payment:platform:provider', 'admin', 'PayProviderManage', 'menu.payment.platform.provider.manage', 'lucide:wallet', 'f', 'f', '/payment/masterdata/provider/PayProviderList', '/payment/platform/pay-provider', NULL, 0.5, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-17 10:00:31.640482+00', '2026-06-27 13:01:24.318649+00');
INSERT INTO "public"."iam_perm_menu" VALUES (602, 6, 'trade:fund', 'admin', 'PayTradeList', 'menu.trade.payTrade', 'lucide:arrow-left-right', 'f', 'f', '/payment/order/PayTradeList', '/trade/pay-trade', NULL, 2, 'f', 't', 'f', NULL, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-01 15:09:04.769906+00', '2026-07-01 15:09:04.769906+00');
INSERT INTO "public"."iam_perm_menu" VALUES (309, 308, 'system:notify:notice', 'admin', 'SystemNotify', 'menu.system.notify.notice', 'lucide:megaphone', 'f', 'f', '/system/notify/notice/NoticeList', '/system/notify/notice', NULL, 20, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, '2026-06-24 16:00:00+00', '2026-06-24 16:00:00+00', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (308, 3, 'system:notify', 'admin', 'SystemNotice', 'menu.system.notify', 'lucide:bell', 'f', 'f', NULL, '/system/notify', '/system/notify/notice', 20, 'f', 't', 'f', 1, 1, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-01 07:16:30.927645+00', '2026-07-01 07:16:30.927645+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40502, 40508, 'payment:isv', 'admin', 'AlipayIsvAppManage', 'menu.payment.config.alipayApp', NULL, 't', 'f', '/payment/channel/alipay/manage/app/AlipayIsvAppManage', '/payment/config/product/app-manage', NULL, 3, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-14 00:00:00+00', '2026-07-11 03:37:15.43231+00');
INSERT INTO "public"."iam_perm_menu" VALUES (901, 9, 'device:qrcode', 'admin', 'DeviceQrCode', 'menu.device.qrcode', 'lucide:qr-code', 'f', 'f', '/payment/device/qrcode/DeviceQrCode', '/device/qrcode', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-25 01:21:34.634639+00', '2026-06-25 12:51:25.24304+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040119, 4040132, 'channel:app', 'admin', 'WechatIsvMchAppManage', 'menu.payment.merchant.channelMerchant.wechatIsvApp', NULL, 't', 'f', '/payment/channel/wechat/manage/mch/isv-app/WechatIsvMchAppManage', '/payment/merchant/channel-merchant/wechat-isv-mch-app-manage', NULL, 14, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 01:49:32.557374+00', '2026-07-11 03:37:15.430392+00');
INSERT INTO "public"."iam_perm_menu" VALUES (803, 8, 'develop:auth', 'admin', 'ChannelAuth', 'menu.develop.auth', 'lucide:key-round', 'f', 'f', '/payment/develop/auth/ChannelAuth', '/develop/auth', NULL, 3, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-09 16:00:00+00', '2026-07-09 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (310, 308, 'system:notify:wechat-config', 'admin', 'WechatNotify', 'menu.system.notify.wechatConfig', 'lucide:message-circle', 'f', 'f', '/system/notify/wechat/index', '/system/notify/wechat', NULL, 30, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-09 00:00:00+00', '2026-07-09 00:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040121, 4040130, 'merchant:gateway-aggregate', 'admin', 'AggregateScanConfig', 'menu.payment.merchant.aggregateScan', NULL, 't', 'f', '/payment/merchant/aggregate/AggregateScanConfig', '/payment/merchant/aggregate', NULL, 3, 'f', 't', 'f', 1, 1, 1, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:00:13.374496+00', '2026-07-13 10:13:20.81126+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040130, 40401, NULL, 'admin', 'MchManageGroup', 'menu.payment.merchant.group.manage', 'lucide:settings-2', 't', 'f', NULL, NULL, NULL, 1, 'f', 'f', 'f', 1, 1, 0, 'f', 'subpage_group', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:37:15.421585+00', '2026-07-11 03:37:15.421585+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040131, 40401, NULL, 'admin', 'ChannelMerchantGroup', 'menu.payment.merchant.group.channelMerchant', 'lucide:repeat', 't', 'f', NULL, NULL, NULL, 2, 'f', 'f', 'f', 1, 1, 0, 'f', 'subpage_group', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:37:15.421585+00', '2026-07-11 03:37:15.421585+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040122, 4040130, 'merchant:gateway-cashier', 'admin', 'CashierConfig', 'menu.payment.merchant.cashierConfig', NULL, 't', 'f', '/payment/merchant/cashier/CashierConfig', '/payment/merchant/cashier', NULL, 4, 'f', 't', 'f', 1, 1, 1, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:00:13.374496+00', '2026-07-11 03:37:15.424776+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040120, 4040130, 'merchant:wx-verify', 'admin', 'MchWxDomainVerifyList', 'menu.payment.merchant.wxVerify', NULL, 't', 'f', '/payment/merchant/manage/wx-verify/MchWxDomainVerifyList', '/payment/merchant/manage/wx-verify', NULL, 20, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-09 16:00:00+00', '2026-07-11 03:37:15.424776+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40501, 40105, 'payment:config:product-detail', 'admin', 'ProductDetailDispatch', 'menu.payment.config.detail', NULL, 't', 'f', '/payment/config/product/detail/ProductDetailDispatch', '/payment/product-detail', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-14 00:00:00+00', '2026-06-27 13:01:24.321883+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40507, 304, 'payment:config:wx-verify', 'admin', 'PlatformWxDomainVerifyList', 'menu.payment.config.wxVerify', 'lucide:shield-check', 'f', 'f', '/payment/config/wx-verify/PlatformWxDomainVerifyList', '/payment/config/wx-verify', NULL, 7, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-09 16:00:00+00', '2026-07-09 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40504, 304, 'system:config:mobile-app', 'admin', 'MobileAppConfig', 'menu.system.config.mobileApp', 'lucide:smartphone', 'f', 'f', '/system/config/mobileApp/MobileAppConfig', '/system/config/mobile-app', NULL, 6, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-10 08:54:32.213917+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40505, 40504, 'system:config:mobile-app-detail', 'admin', 'MobileAppDetail', 'menu.system.config.mobileAppDetail', NULL, 't', 'f', '/system/config/mobileApp/detail/MobileAppDetail', '/system/config/mobile-app/detail/:appType', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-10 08:54:32.222476+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040123, 4040130, 'merchant:app:workbench', 'admin', 'MchAppWorkbench', 'menu.payment.merchant.appWorkbench', NULL, 't', 'f', '/payment/merchant/app/MchAppWorkbench', '/payment/merchant/app/manage', NULL, 11, 'f', 't', 'f', 1, 1, 1, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 08:00:00+00', '2026-07-11 08:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40506, 40508, 'payment:isv', 'admin', 'LakalaManage', 'menu.payment.lakala.config', NULL, 't', 'f', '/payment/channel/lakala/manage/LakalaManage', '/payment/config/product/lakala-manage', NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-11 03:37:15.43231+00');
INSERT INTO "public"."iam_perm_menu" VALUES (4040132, 40401, NULL, 'admin', 'ChannelAppGroup', 'menu.payment.merchant.group.channelApp', 'lucide:layout-grid', 't', 'f', NULL, NULL, NULL, 3, 'f', 'f', 'f', 1, 1, 0, 'f', 'subpage_group', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:37:15.421585+00', '2026-07-13 10:13:20.804554+00');
INSERT INTO "public"."iam_perm_menu" VALUES (40508, 40105, NULL, 'admin', 'ChannelIsvConfigGroup', 'menu.payment.config.group.channelIsv', 'lucide:server', 't', 'f', NULL, NULL, NULL, 1, 'f', 'f', 'f', 1, 1, 0, 'f', 'subpage_group', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-11 03:37:15.421585+00', '2026-07-13 10:13:20.806797+00');

-- ----------------------------
-- Primary Key structure for table iam_perm_menu
-- ----------------------------
ALTER TABLE "public"."iam_perm_menu" ADD CONSTRAINT "iam_perm_menu_pkey" PRIMARY KEY ("id");
