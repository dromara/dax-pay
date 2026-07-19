-- ============================================================
-- 门店/应用默认项唯一性约束（升级脚本）
-- ============================================================
-- 背景:
--   商户应用(MchAppInfo)和门店(MchStoreInfo)均引入「默认项」机制,
--   要求同一商户下至多一个默认应用 / 至多一个默认门店。
--   原有约束仅由 Service 层 setDefault(先 clearDefault 再 set)保证,
--   并发或绕过 Service 直接改库时存在隐患。
-- 策略:
--   数据库层加 PostgreSQL 部分唯一索引(Partial Unique Index)兜底,
--   与 Service 层逻辑形成双保险。
-- 影响范围:
--   - 升级前请先执行 update-datas.sql 中的诊断查询,
--     如发现「同商户多个默认应用/门店」需先人工修复, 否则建索引会失败。
-- ============================================================

-- 商户默认应用唯一索引 (同一商户至多一个 default_app=true 且未删除的记录)
CREATE UNIQUE INDEX IF NOT EXISTS uk_mch_app_info_default
    ON mch_app_info (mch_no)
    WHERE default_app = TRUE AND deleted = FALSE;

-- 商户默认门店唯一索引 (同一商户至多一个 default_store=true 且未删除的记录)
CREATE UNIQUE INDEX IF NOT EXISTS uk_mch_store_info_default
    ON mch_store_info (mch_no)
    WHERE default_store = TRUE AND deleted = FALSE;

-- ============================================================
-- pay_trade 冗余支付渠道字段（升级脚本）
-- ============================================================
-- 背景:
--   渠道分布报表/资金列表筛选需要按支付渠道(provider)维度统计,
--   原 pay_trade 仅冗余 channelMchNo, 无渠道字段, 报表 SQL 报错或需 JOIN 容器表.
--   按"轻量组织冗余"哲学(source/channelMchNo/storeNo/provider), 加 provider 列,
--   与现有冗余字段并列; 权威仍在业务容器(NormalPayOrder/GatewayPayOrder).
-- 写入时机:
--   支付成功 sync 回执时由 PayUniHandleService.applyXxxSyncReceipts 同步写容器+资金凭证.
-- 历史数据:
--   通过 container_id JOIN 容器表回填(normal/gateway 两种主流 trade_type).
--   其他 trade_type 历史无 provider 数据可回填, 留 NULL, 不影响新交易.
-- ============================================================

-- 1. 加列(允许 NULL, 历史数据回填前为空)
ALTER TABLE pay_trade ADD COLUMN IF NOT EXISTS provider varchar(32);

COMMENT ON COLUMN pay_trade.provider IS '支付渠道(冗余自容器, 支付成功sync后回填; 权威在容器 provider)';

-- 2. 历史数据回填: normal 容器
UPDATE pay_trade t
SET provider = o.provider
FROM pay_normal_order o
WHERE t.container_id = o.id
  AND t.trade_type = 'normal'
  AND t.provider IS NULL
  AND o.provider IS NOT NULL;

-- 3. 历史数据回填: gateway 容器
UPDATE pay_trade t
SET provider = o.provider
FROM pay_gateway_order o
WHERE t.container_id = o.id
  AND t.trade_type = 'gateway'
  AND t.provider IS NULL
  AND o.provider IS NOT NULL;

-- ============================================================
-- pay_md_product_config 删除 configured 字段
-- ============================================================
-- 背景:
--   该字段仅用于前端卡片"未配置"标签展示, 由前端保存时手动设置,
--   无任何后端业务逻辑读取(通道模块只读 activeEnv).
--   每个支付产品的配置存储方式各不相同(20+ 张独立 key_config/channel_merchant 表,
--   字段差异极大), 无法用统一规则判定"是否已配置", 故该字段不可靠, 一并移除.
-- 影响:
--   - 升级后前端不再展示"未配置"标签, 卡片仅显示产品图标与名称.
--   - 无任何业务逻辑受影响(路由匹配/订单处理/通道选择均不读此字段).
-- ============================================================
ALTER TABLE pay_md_product_config DROP COLUMN IF EXISTS configured;
