-- ============================================================
-- 升级 SQL - 数据增量
-- 数据库: PostgreSQL 14+
-- ============================================================

-- ------------------------------------------------------------
-- 微信域名验证文件管理 - 菜单初始化
-- 当前系统仅有 admin 端，商户级验证文件由运营在商户工作台代为管理（通过 mchNo 指定商户）
-- 平台级验证文件挂「支付配置」目录，商户级验证文件为「商户列表」下的隐藏子页面（从工作台卡片跳转）
--
-- 权限码约定（与后端 @PermCode menuCode 对齐，按钮码 = menuCode:view|manage）:
--   平台级: payment:config:wx_verify   (前端 PermCodes.Payment.Config.WxDomainVerify)
--   商户级: merchant:wx_verify          (前端 PermCodes.Merchant.WxDomainVerify)
-- 组件路径对应前端视图:
--   平台级: views/payment/config/wx-verify/PlatformWxDomainVerifyList.vue
--   商户级: views/payment/merchant/manage/wx-verify/MchWxDomainVerifyList.vue
-- ------------------------------------------------------------

-- 平台级菜单（client_code='admin'）：挂「支付配置」(id=405) 目录下，全局视图管理所有验证文件（平台+商户）
-- 注: 40506 已被「拉卡拉服务商配置」占用，改用 40507
DELETE FROM "public"."iam_perm_menu" WHERE id = 40507;
INSERT INTO "public"."iam_perm_menu" VALUES (
  40507, 405, 'payment:config:wx_verify', 'admin', 'PlatformWxDomainVerifyList',
  '微信域名验证', 'WeChat Domain Verify', 'menu.payment.config.wxVerify',
  'lucide:shield-check', 'f', 'f',
  '/payment/config/wx-verify/PlatformWxDomainVerifyList', '/payment/config/wx-verify', NULL,
  6, 'f', 't', 'f', 1, 1, 0, 'f', 'menu',
  NULL, NULL, NULL, NULL, NULL, NULL,
  '2026-07-09 16:00:00+00', '2026-07-09 16:00:00+00'
);

-- 商户级子页面（client_code='admin'）：挂「商户列表」(id=40401) 下，隐藏菜单，由商户工作台「业务配置」组卡片跳转进入
-- 工作台跳转: router.push({ path: '/payment/merchant/manage/wx-verify', query: { mchNo } })
DELETE FROM "public"."iam_perm_menu" WHERE id = 4040120;
INSERT INTO "public"."iam_perm_menu" VALUES (
  4040120, 40401, 'merchant:wx_verify', 'admin', 'MchWxDomainVerifyList',
  '微信域名验证', 'WeChat Domain Verify', 'menu.payment.merchant.wxVerify',
  NULL, 't', 'f',
  '/payment/merchant/manage/wx-verify/MchWxDomainVerifyList', '/payment/merchant/manage/wx-verify', NULL,
  20, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage',
  NULL, NULL, NULL, NULL, NULL, NULL,
  '2026-07-09 16:00:00+00', '2026-07-09 16:00:00+00'
);

-- ===== social login config migration: menu_code/name/i18n_key/component/path =====
UPDATE iam_perm_menu SET
  menu_code = 'iam:social:login-config',
  name = 'SocialLoginConfig',
  i18n_key = 'menu.system.config.socialLogin',
  component = 'views/iam/social/social-login-config',
  path = '/system/config/social-login'
WHERE id = 900001;

-- ===== 第三方平台管理改造: 原"三方平台登录"升级为 tab 形式的"三方平台管理" =====
-- 整合: 登录平台配置(标准 OAuth2 卡片) + 支付宝开放平台(平台级配置) + (后续)微信公众号/抖音
-- menu_code 保留 iam:social:login-config, SocialLoginConfigController 与 PlatformAlipayAuthConfigController 共享菜单权限
UPDATE iam_perm_menu SET
  name = 'ThirdPlatform',
  name_cn = '三方平台管理',
  name_en = 'Third-party Platform Management',
  i18n_key = 'menu.system.config.thirdPlatform',
  component = 'views/system/config/third-platform/ThirdPlatform',
  path = '/system/config/third-platform'
WHERE id = 900001;

-- ===== 微信公众号/抖音 H5 退出三方登录体系 =====
-- wechatMpPublic / douyinH5 不再属于 SocialSourceEnum, 清理历史登录配置占位行
-- 微信公众号平台级凭据(wechat_mp_auth)保留; 抖音 H5 平台授权(douyin_auth)已下线
DELETE FROM iam_social_login_config WHERE source IN ('wechatMpPublic', 'douyinH5');
-- 历史绑定关系一并清理(若有)
DELETE FROM iam_user_social WHERE source IN ('wechatMpPublic', 'douyinH5');

-- ===== 抖音 H5 平台授权配置下线 =====
-- 三方平台管理中的抖音应用配置(H5 OAuth)已移除, 清理加密配置残留
-- 三方登录抖音扫码(iam_social_login_config source=douyin)不受影响
DELETE FROM system_platform_encrypt_config WHERE config_type = 'douyin_auth';

-- ===== 开发调试 - 认证调试菜单 =====
-- menuCode=develop:auth, 组件 views/payment/develop/auth/ChannelAuth
-- 权限: develop:auth:view (后端 @PermCode 自动注册)
DELETE FROM "public"."iam_perm_menu" WHERE id = 803;
INSERT INTO "public"."iam_perm_menu" VALUES (
  803, 8, 'develop:auth', 'admin', 'ChannelAuth',
  '认证调试', 'Auth Develop', 'menu.develop.auth',
  'lucide:key-round', 'f', 'f',
  '/payment/develop/auth/ChannelAuth', '/develop/auth', NULL,
  3, 'f', 't', 'f', 1, 1, 0, 'f', 'menu',
  NULL, NULL, NULL, NULL, NULL, NULL,
  '2026-07-09 16:00:00+00', '2026-07-09 16:00:00+00'
);
