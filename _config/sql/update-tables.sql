-- 表结构更新(2026-08-30): notify_mail_record 两列注释精简为紧凑枚举值风格
COMMENT ON COLUMN public.notify_mail_record.business_type IS '业务场景(test/manual等)';
COMMENT ON COLUMN public.notify_mail_record.status IS '发送状态(sending/success/fail)';

-- 表结构更新(2026-09-06): 风控精简, 移除门店地理围栏(移交商业版)
-- 命中记录去掉围栏快照列(client_city 保留, 省市地区黑名单命中仍记录 IP 归属城市)
ALTER TABLE pay_risk_hit DROP COLUMN IF EXISTS store_city;
ALTER TABLE pay_risk_hit DROP COLUMN IF EXISTS store_no;
ALTER TABLE pay_risk_hit DROP COLUMN IF EXISTS geo_fence_strategy;
-- 商户级围栏 opt-in 配置表随围栏功能整体移除
DROP TABLE IF EXISTS mch_risk_config;


-- 风控精简: 移除城市接壤关系表(围栏 balanced 邻市策略专用, 随门店地理围栏一并移交商业版)
DROP TABLE IF EXISTS base_city_adjacent;
DROP SEQUENCE IF EXISTS base_city_adjacent_id_seq;

-- 表结构更新(2026-09-10): 分账接收方新增本地别名(纯本地备注字段, 不上送通道, 可随时修改)
-- 五张通道档案表同步加列, 仅用于平台侧识别管理, 与通道侧接收方名称(receiver_name)无关
ALTER TABLE public.wechat_direct_alloc_receiver ADD COLUMN IF NOT EXISTS alias varchar(50);
COMMENT ON COLUMN public.wechat_direct_alloc_receiver.alias IS '接收方别名(本地备注, 不上送通道)';
ALTER TABLE public.wechat_isv_alloc_receiver ADD COLUMN IF NOT EXISTS alias varchar(50);
COMMENT ON COLUMN public.wechat_isv_alloc_receiver.alias IS '接收方别名(本地备注, 不上送通道)';
ALTER TABLE public.alipay_direct_alloc_receiver ADD COLUMN IF NOT EXISTS alias varchar(50);
COMMENT ON COLUMN public.alipay_direct_alloc_receiver.alias IS '接收方别名(本地备注, 不上送通道)';
ALTER TABLE public.alipay_isv_alloc_receiver ADD COLUMN IF NOT EXISTS alias varchar(50);
COMMENT ON COLUMN public.alipay_isv_alloc_receiver.alias IS '接收方别名(本地备注, 不上送通道)';
ALTER TABLE public.douyin_direct_alloc_receiver ADD COLUMN IF NOT EXISTS alias varchar(50);
COMMENT ON COLUMN public.douyin_direct_alloc_receiver.alias IS '接收方别名(本地备注, 不上送通道)';


-- 表结构更新(2026-09-11): 账号/商户号/角色编码唯一性下沉到数据库
-- 背景: 唯一性此前仅由应用层保证, check-then-insert 存在并发竞态; 且一旦出现重复账号,
--       登录按 client_code+account 查单条会抛多结果异常, 导致新旧账号一并不可用。
--       仓内其余业务键(app_id/store_no/code/terminal_no)均已用唯一索引兜底, 此处补齐这三张表。
-- 前置: 库中须无重复数据(升级前先自查, 有重复需先清理)
DROP INDEX IF EXISTS public.idx_iam_user_info_client_account;
CREATE UNIQUE INDEX uk_iam_user_info_client_account ON public.iam_user_info (client_code, account) WHERE deleted = false;
COMMENT ON INDEX uk_iam_user_info_client_account IS '同一身份域下账号全局唯一(登录凭据, 重复会导致登录异常)';
DROP INDEX IF EXISTS public.idx_mch_info_mch_no;
CREATE UNIQUE INDEX uk_mch_info_mch_no ON public.mch_info (mch_no) WHERE deleted = false;
COMMENT ON INDEX uk_mch_info_mch_no IS '商户号全局唯一(商户业务主键, 重复会导致按号查询异常)';
CREATE UNIQUE INDEX IF NOT EXISTS uk_iam_role_code ON public.iam_role (code) WHERE deleted = false;
COMMENT ON INDEX uk_iam_role_code IS '角色编码全局唯一(按 code 解析内置角色, 重复会导致角色解析异常)';
