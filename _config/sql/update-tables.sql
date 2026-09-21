-- 表结构更新(2026-09-21): 转账域补应用ID列, 修复转账终态通知死链
-- 转账容器三表 + 公共资金凭证表加 app_id(建单从请求参数写入/凭证冗余自容器),
-- 出站通知派发器按 appId 校验必填, 此前转账终态通知因缺 appId 被整体跳过从未发出。
-- 存量数据不回填(历史转账单通知本就未发出过, 置 NULL 后订单级 notifyUrl 通知不受影响)。
ALTER TABLE public.pay_transfer_order_wechat ADD COLUMN IF NOT EXISTS app_id varchar(64);
COMMENT ON COLUMN public.pay_transfer_order_wechat.app_id IS '应用ID(建单从请求参数写入, 出站通知派发用)';
ALTER TABLE public.pay_transfer_order_alipay ADD COLUMN IF NOT EXISTS app_id varchar(64);
COMMENT ON COLUMN public.pay_transfer_order_alipay.app_id IS '应用ID(建单从请求参数写入, 出站通知派发用)';
ALTER TABLE public.pay_transfer_order_douyin ADD COLUMN IF NOT EXISTS app_id varchar(64);
COMMENT ON COLUMN public.pay_transfer_order_douyin.app_id IS '应用ID(建单从请求参数写入, 出站通知派发用)';
ALTER TABLE public.pay_transfer_trade ADD COLUMN IF NOT EXISTS app_id varchar(64);
COMMENT ON COLUMN public.pay_transfer_trade.app_id IS '应用ID(冗余自容器, 出站通知派发用; 权威在通道转账单容器)';

-- 表结构更新(2026-09-21): 微信转账单补报备信息列, 修复失败重试报备丢失
-- 微信容器此前不持久化 report_infos(支付宝/抖音两容器有), FAIL 复用原单重试时上下文恢复不到商户原始报备,
-- 通道侧 ensureReportInfos 兜底按场景模板填 "-" 占位, 报备内容静默失真。补列后建单即持久化, 重试原样恢复。
-- 存量数据不回填(历史单据的原始报备内容已不可恢复, 其重试仍走占位兜底)。
ALTER TABLE public.pay_transfer_order_wechat ADD COLUMN IF NOT EXISTS report_infos text;
COMMENT ON COLUMN public.pay_transfer_order_wechat.report_infos IS '转账场景报备信息(JSON序列化, FAIL重试时恢复报备用)';
